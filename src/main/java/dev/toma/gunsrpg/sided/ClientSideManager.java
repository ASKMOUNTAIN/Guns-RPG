package dev.toma.gunsrpg.sided;

import dev.toma.gunsrpg.client.ModKeybinds;
import dev.toma.gunsrpg.client.animation.ScriptLoader;
import dev.toma.gunsrpg.client.render.RenderAirdrop;
import dev.toma.gunsrpg.client.render.RenderExplosiveArrow;
import dev.toma.gunsrpg.client.render.RenderExplosiveSkeleton;
import dev.toma.gunsrpg.common.entity.EntityAirdrop;
import dev.toma.gunsrpg.common.entity.EntityExplosiveArrow;
import dev.toma.gunsrpg.common.entity.EntityExplosiveSkeleton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientSideManager extends SideManager {

    public static ScriptLoader scriptLoader = new ScriptLoader();

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityAirdrop.class, RenderAirdrop::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExplosiveSkeleton.class, RenderExplosiveSkeleton::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExplosiveArrow.class, RenderExplosiveArrow::new);
        ModKeybinds.registerKeybinds();
        MinecraftForge.EVENT_BUS.register(new ModKeybinds());
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(scriptLoader);
    }
}
