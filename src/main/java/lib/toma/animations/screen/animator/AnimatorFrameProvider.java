package lib.toma.animations.screen.animator;

import lib.toma.animations.AnimationUtils;
import lib.toma.animations.QuickSort;
import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.IAnimation;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.pipeline.frame.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Predicate;

public class AnimatorFrameProvider implements IKeyframeProvider {

    private final Map<AnimationStage, List<MutableKeyframe>> frameMap = new TreeMap<>(AnimationStage::compareTo);
    private final List<IAnimationEvent> eventList = new ArrayList<>();
    private final boolean events;

    // stuff which is normally handled in FrameProviderInstances
    private final Map<AnimationStage, Integer> frameCache = new HashMap<>();
    private byte eventIndex;

    public AnimatorFrameProvider(IKeyframeProvider provider) {
        this.events = provider.getType().areEventsSupported();
        if (events) {
            eventList.addAll(Arrays.asList(provider.getEvents()));
            eventList.sort(Comparator.comparingDouble(IAnimationEvent::invokeAt));
        }
        Map<AnimationStage, IKeyframe[]> rawFrames = provider.getFrameMap();
        for (Map.Entry<AnimationStage, IKeyframe[]> entry : rawFrames.entrySet()) {
            AnimationStage stage = entry.getKey();
            IKeyframe[] array = entry.getValue();
            if(array.length == 0) continue;
            List<MutableKeyframe> list = new ArrayList<>();
            Arrays.stream(array).map(MutableKeyframe::fullCopyOf).forEach(list::add);
            frameMap.put(stage, list);
        }
        provider.initCache(frameCache);
    }

    @Override
    public boolean shouldAdvance(AnimationStage stage, float progress, int frameIndex) {
        List<MutableKeyframe> list = frameMap.get(stage);
        if (list == null || frameIndex >= list.size() - 1) return false;
        MutableKeyframe frame = list.get(frameIndex);
        return frame.endpoint() <= progress;
    }

    @Override
    public IKeyframe getCurrentFrame(AnimationStage stage, float progress, int frameIndex) {
        return frameMap.get(stage).get(frameIndex);
    }

    @Override
    public IKeyframe getOldFrame(AnimationStage stage, int frameIndex) {
        return frameIndex == 0 ? Keyframes.none() : frameMap.get(stage).get(frameIndex - 1);
    }

    @Override
    public IAnimationEvent[] getEvents() {
        return eventList.toArray(IAnimationEvent.NO_EVENTS);
    }

    @Override
    public Map<AnimationStage, IKeyframe[]> getFrameMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FrameProviderType<?> getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void initCache(Map<AnimationStage, Integer> cache) {
        throw new UnsupportedOperationException();
    }

    public void recompile(AnimationStage stage) {
        List<MutableKeyframe> list = frameMap.get(stage);
        if (list != null && list.size() > 1) {
            for (int i = 1; i < list.size(); i++) {
                IKeyframe parent = list.get(i - 1);
                IKeyframe child = list.get(i);
                child.baseOn(parent);
            }
        }
    }

    public void finish() {
        for (List<MutableKeyframe> list : frameMap.values()) {
            if (list.isEmpty()) continue;
            MutableKeyframe last = list.get(list.size() - 1);
            if (last.endpoint() == 1.0F) continue;
            MutableKeyframe ending = new MutableKeyframe();
            ending.baseOn(last);

            Vector3d position = ending.initialPosition();
            ending.setPosition(position.multiply(-1, -1, -1));
            Vector3f scale = ending.initialScale();
            if (!scale.equals(AnimationUtils.DEFAULT_SCALE_VECTOR)) {
                ending.setScale(new Vector3f(1.0F - scale.x(), 1.0F - scale.y(), 1.0F - scale.z()));
            }
            Pair<Float, Vector3f> rotation = AnimationUtils.getVectorWithRotation(ending.initialRotation());
            float degrees = rotation.getKey();
            ending.setRotation(new Quaternion(rotation.getValue().copy(), -degrees, true));
            ending.setEndpoint(1.0F);

            list.add(ending);
        }
    }

    public void deleteStage(AnimationStage stage) {
        frameMap.remove(stage);
        frameCache.remove(stage);
    }

    public Map<AnimationStage, List<MutableKeyframe>> getFrames() {
        return frameMap;
    }

    public void onProgressed(float progress, float progressOld, IAnimation source) {
        invokeEventsRecursive(source, progress, progressOld);
    }

    public void forceProgress(float progress) {
        Set<AnimationStage> set = frameMap.keySet();
        clrAndAdjustCache(progress, set);
    }

    public boolean blocksStageAnimation(AnimationStage stage) {
        return !frameCache.containsKey(stage);
    }

    public IKeyframe getActualFrame(AnimationStage stage, float progress) {
        int index = frameCache.get(stage);
        if (shouldAdvance(stage, progress, index)) {
            frameCache.put(stage, ++index);
        }
        return getCurrentFrame(stage, progress, index);
    }

    public IKeyframe getLastFrame(AnimationStage stage) {
        return getOldFrame(stage, frameCache.get(stage));
    }

    public IKeyframeProvider toSerializable() {
        if (frameMap.isEmpty())
            return NoFramesProvider.empty();
        boolean events = !eventList.isEmpty();
        boolean singleFrame = !events && checkSingleFrame();
        boolean tAndBack = !events && checkTargetAndBack();
        if (singleFrame) {
            return constructSingleFrameProvider();
        } else if (tAndBack) {
            return constructTargetAndBackProvider();
        } else {
            return constructFullProvider();
        }
    }

    public boolean canAddEvents() {
        return events;
    }

    public void addFrame(AnimationStage stage, MutableKeyframe frame) {
        List<MutableKeyframe> list = frameMap.computeIfAbsent(stage, key -> new ArrayList<>());
        list.add(frame);
        sortAndCompile(list);
        onStageAdded(stage);
    }

    public void removeFrame(AnimationStage stage, MutableKeyframe frame) {
        List<MutableKeyframe> list = frameMap.get(stage);
        if (list != null) {
            if (list.remove(frame)) {
                sortAndCompile(list);
                int index = frameCache.get(stage);
                if (index >= list.size()) {
                    frameCache.put(stage, list.size() - 1);
                }
            }
            if (list.isEmpty()) {
                deleteStage(stage);
            }
        }
    }

    public void sort(AnimationStage stage) {
        List<? extends IKeyframe> list = frameMap.get(stage);
        if (list == null) return;
        sortAndCompile(list);
    }

    private boolean checkSingleFrame() {
        return isWithinFrameCount(size -> size <= 1);
    }

    private boolean checkTargetAndBack() {
        return frameMap.size() == 1 && isWithinFrameCount(size -> size == 2);
    }

    private IKeyframeProvider constructSingleFrameProvider() {
        Map<AnimationStage, IKeyframe> map = new HashMap<>();
        for (Map.Entry<AnimationStage, List<MutableKeyframe>> entry : frameMap.entrySet()) {
            List<MutableKeyframe> list = entry.getValue();
            if (!list.isEmpty()) {
                map.put(entry.getKey(), list.get(0));
            }
        }
        return SingleFrameProvider.fromExistingMap(map);
    }

    private IKeyframeProvider constructTargetAndBackProvider() {
        for (Map.Entry<AnimationStage, List<MutableKeyframe>> entry : frameMap.entrySet()) {
            AnimationStage target = entry.getKey();
            List<MutableKeyframe> list = entry.getValue();
            MutableKeyframe toTarget = list.get(0);
            MutableKeyframe fromTarget = list.get(1);
            return new TargetAndBackFrameProvider(target, toTarget, fromTarget);
        }
        throw new IllegalStateException("Frame map was empty. How is this possible?");
    }

    private IKeyframeProvider constructFullProvider() {
        Map<AnimationStage, IKeyframe[]> map = new HashMap<>();
        for (Map.Entry<AnimationStage, List<MutableKeyframe>> entry : frameMap.entrySet()) {
            AnimationStage stage = entry.getKey();
            List<MutableKeyframe> frames = entry.getValue();
            IKeyframe[] array = frames.toArray(new IKeyframe[0]);
            QuickSort.sort(array, Comparator.comparingDouble(IKeyframe::endpoint));
            map.put(stage, array);
        }
        return new KeyframeProvider(map, eventList.isEmpty() ? IAnimationEvent.NO_EVENTS : eventList.toArray(IAnimationEvent.NO_EVENTS));
    }

    private boolean isWithinFrameCount(Predicate<Integer> sizePredicate) {
        for (Map.Entry<AnimationStage, List<MutableKeyframe>> entry : frameMap.entrySet()) {
            int size = entry.getValue().size();
            if (!sizePredicate.test(size)) {
                return false;
            }
        }
        return true;
    }

    private void onStageAdded(AnimationStage stage) {
        frameCache.put(stage, 0);
    }

    private void clrAndAdjustCache(float progress, Set<AnimationStage> set) {
        set.forEach(stage -> frameCache.put(stage, 0));
        while (true) {
            if (!hasFrameAdvanced(progress, set)) {
                break;
            }
        }
    }

    private boolean hasFrameAdvanced(float progress, Set<AnimationStage> set) {
        boolean changed = false;
        for (AnimationStage stage : set) {
            int index = frameCache.get(stage);
            if (shouldAdvance(stage, progress, index)) {
                frameCache.put(stage, index + 1);
                changed = true;
            }
        }
        return changed;
    }

    private void invokeEventsRecursive(IAnimation source, float progress, float progressOld) {
        if (eventIndex >= eventList.size()) return;
        IAnimationEvent event = eventList.get(eventIndex);
        float target = event.invokeAt();
        if (progress >= target && progressOld < target) {
            ++eventIndex;
            event.dispatch(Minecraft.getInstance(), source);
            invokeEventsRecursive(source, progress, progressOld);
        }
    }

    private void sortAndCompile(List<? extends IKeyframe> list) {
        list.sort(Comparator.comparingDouble(IKeyframe::endpoint));
        for (int i = 1; i < list.size(); i++) {
            IKeyframe parent = list.get(i - 1);
            IKeyframe child = list.get(i);
            child.baseOn(parent);
        }
    }
}