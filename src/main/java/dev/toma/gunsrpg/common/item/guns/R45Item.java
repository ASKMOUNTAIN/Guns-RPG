package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.R45Renderer;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class R45Item extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("r45/reload");
    private static final ResourceLocation RELOAD_BULLET = GunsRPG.makeResource("r45/load_bullet");

    public R45Item(String name) {
        super(name, new Properties().setISTER(() -> R45Renderer::new));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.r45)
                .ammo(WeaponCategory.PISTOL)
                    .define(AmmoMaterials.WOOD, 0)
                .build();
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return 6;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider) {
        return 17;
    }

    @Override
    public IReloadManager getReloadManager(PlayerEntity player) {
        return ReloadManagers.singleBulletLoading(26, player, this, player.getMainHandItem(), RELOAD_BULLET);
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.M1911_ASSEMBLY;
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.R45_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.R45_RIGHT;
    }
}
