package lib.toma.animations.pipeline.frame;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.QuickSort;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;

public class Keyframes {

    private static final IKeyframe NULL_FRAME = new EmptyKeyframe(0.0F);

    public static void sortFrames(IKeyframe[] array) {
        QuickSort.sort(array, Comparator.comparingDouble(IKeyframe::endpoint));
    }

    public static Vector3d getInitialPosition(IKeyframe parent) {
        return parent.initialPosition().add(parent.positionTarget());
    }

    public static Quaternion getInitialRotation(IKeyframe parent) {
        Quaternion q1 = parent.initialRotation();
        Quaternion q2 = parent.rotationTarget();
        Quaternion q3 = q1.copy();
        q3.mul(q2);
        return q3;
    }

    public static void processFrame(IKeyframe keyframe, float percent, MatrixStack matrixStack) {
        Vector3d move1 = keyframe.initialPosition();
        Vector3d move2 = keyframe.positionTarget();
        Quaternion rot1 = keyframe.initialRotation();
        Quaternion rot2 = keyframe.rotationTarget();
        matrixStack.translate(move1.x + move2.x * percent, move1.y + move2.y * percent, move1.z + move2.z * percent);
        matrixStack.mulPose(mul(rot1, rot2, percent));
    }

    protected static Quaternion mul(Quaternion q1, Quaternion q2, float f) {
        Quaternion q3 = q2.copy();
        q3.mul(f);
        q3.mul(q1);
        return q3;
    }

    public static IKeyframe none() {
        return NULL_FRAME;
    }

    public static IKeyframe wait(float endpoint) {
        return new EmptyKeyframe(endpoint);
    }

    public static IKeyframe position(Vector3d position, float endpoint) {
        return new PositionKeyframe(position, endpoint);
    }

    public static IKeyframe keyframe(Vector3d position, Quaternion rotation, float endpoint) {
        return new Keyframe(position, rotation, endpoint);
    }
}
