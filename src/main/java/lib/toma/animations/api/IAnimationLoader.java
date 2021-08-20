package lib.toma.animations.api;

import com.google.gson.stream.JsonReader;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public interface IAnimationLoader {

    IKeyframeProvider getProvider(ResourceLocation path);

    ILoader getResourceReader();

    String serializeResource(IKeyframeProvider provider);

    void addLoadingListener(ILoadingFinished listener);

    @FunctionalInterface
    interface ILoadingFinished {
        void onLoaded(Map<ResourceLocation, IKeyframeProvider> providerMap, ILoader resourceLoader);
    }

    @FunctionalInterface
    interface ILoader {
        IKeyframeProvider loadResource(JsonReader reader);
    }
}