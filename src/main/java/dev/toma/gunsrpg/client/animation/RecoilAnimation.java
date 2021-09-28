package dev.toma.gunsrpg.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.TickableAnimation;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Vector3f;

public class RecoilAnimation extends TickableAnimation {

    private final float x;
    private final float y;
    private final float z;

    public RecoilAnimation(float x, float y, float scale) {
        super(3);
        float modifier = 0.5F + 0.5F * scale;
        this.x = x * modifier;
        this.y = y * modifier;
        this.z = 0.09F * modifier;
    }

    @Override
    public void animate(AnimationStage stage, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int light, int overlay) {
        if (stage != AnimationStage.ITEM_AND_HANDS) return;
        float interpolated = getInterpolatedProgress();
        float progress = getPartial(interpolated);
        float xRot = x * progress;
        float yRot = y * progress;
        float zKick = z * progress;
        matrixStack.translate(0.0, 0.0, zKick);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(xRot));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(yRot));
    }

    private float getPartial(float progress) {
        return progress <= 0.5F ? progress / 0.5F : 1.0F - (progress - 0.5F) / 0.5F;
    }
}