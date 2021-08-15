package dev.toma.gunsrpg.sided;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.ClientEventHandler;
import dev.toma.gunsrpg.client.ModKeybinds;
import dev.toma.gunsrpg.client.animation.GRPGAnimations;
import dev.toma.gunsrpg.client.render.*;
import dev.toma.gunsrpg.client.screen.AirdropScreen;
import dev.toma.gunsrpg.client.screen.BlastFurnaceScreen;
import dev.toma.gunsrpg.client.screen.DeathCrateScreen;
import dev.toma.gunsrpg.client.screen.SmithingTableScreen;
import dev.toma.gunsrpg.client.screen.skills.PlayerSkillsScreen;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.init.Skills;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.IRenderConfig;
import lib.toma.animations.api.IAnimationPipeline;
import lib.toma.animations.api.IItemRenderer;
import lib.toma.animations.api.IRenderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Function;

public class ClientSideManager {

    private static final ClientSideManager INSTANCE = new ClientSideManager();

    public static ClientSideManager instance() {
        return INSTANCE;
    }

    public void clientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.AIRDROP.get(), AirdropRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.EXPLOSIVE_SKELETON.get(), ExplosiveSkeletonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.EXPLOSIVE_ARROW.get(), ExplosiveArrowRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_GUNNER.get(), ZombieGunnerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BLOODMOON_GOLEM.get(), BloodmoonGolemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.CROSSBOW_BOLT.get(), CrossbowBoltRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.GRENADE.get(), GrenadeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ROCKET_ANGEL.get(), RocketAngelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.GOLD_DRAGON.get(), GoldenDragonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SHOTGUN_PELLET.get(), NoOpRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BULLET.get(), NoOpRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.FLARE.get(), NoOpRenderer::new);
        ModKeybinds.registerKeybinds();
        MinecraftForge.EVENT_BUS.register(new ModKeybinds());
        event.enqueueWork(this::screenSetup);

        setupRenderPipeline();
    }

    public void playDelayedSound(BlockPos pos, float volume, float pitch, SoundEvent event, SoundCategory category, int tickDelay) {
        Minecraft mc = Minecraft.getInstance();
        SoundHandler handler = mc.getSoundManager();
        handler.playDelayed(new SimpleSound(event, category, volume, pitch, pos), tickDelay);
    }

    public IPlayerData.ISynchCallback onDataSync() {
        return () -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen instanceof PlayerSkillsScreen) {
                mc.screen.init(mc, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
            }
            if (!PlayerData.getUnsafe(mc.player).getAimInfo().aiming) {
                ClientEventHandler.preAimFov.ifPresent(value -> mc.options.fov = value);
                ClientEventHandler.preAimSens.ifPresent(value -> mc.options.sensitivity = value);
            }
        };
    }

    private void screenSetup() {
        ScreenManager.register(ModContainers.AIRDROP.get(), AirdropScreen::new);
        ScreenManager.register(ModContainers.BLAST_FURNACE.get(), BlastFurnaceScreen::new);
        ScreenManager.register(ModContainers.DEATH_CRATE.get(), DeathCrateScreen::new);
        ScreenManager.register(ModContainers.SMITHING_TABLE.get(), SmithingTableScreen::new);
    }

    private void setupRenderPipeline() {
        IRenderPipeline pipeline = AnimationEngine.get().renderPipeline();
        pipeline.setPostAnimateCallback(this::animateDualWield);
    }

    private void animateDualWield(MatrixStack poseStack, IRenderTypeBuffer buffer, int light, float swing, float equip, Function<HandSide, IRenderConfig> selector,
                                  IAnimationPipeline pipeline, FirstPersonRenderer fpRenderer, PlayerEntity player, ItemStack stack, ItemCameraTransforms.TransformType type,
                                  boolean mainHand) {

        IRenderPipeline renderPipeline = AnimationEngine.get().renderPipeline();
        IItemRenderer itemRenderer = renderPipeline.getItemRenderer();
        if (stack.getItem() == ModItems.M1911 && PlayerData.hasActiveSkill(player, Skills.M1911_DUAL_WIELD)) {
            poseStack.pushPose();
            {
                pipeline.animateStage(GRPGAnimations.DUAL_WIELD_ITEM, poseStack);
                itemRenderer.renderItem(fpRenderer, player, stack, ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, true, poseStack, buffer, light, swing, equip);
            }
            poseStack.popPose();
        }
    }
}
