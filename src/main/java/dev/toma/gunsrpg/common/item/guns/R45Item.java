package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.R45Renderer;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.IDualWieldGun;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class R45Item extends GunItem implements IDualWieldGun {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("r45/reload");
    private static final ResourceLocation RELOAD_BULLET = GunsRPG.makeResource("r45/load_bullet");

    public R45Item(String name) {
        super(name, new Properties().setISTER(() -> R45Renderer::new));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.r45)
                .caliber(AmmoType.AMMO_45ACP)
                .ammo(WeaponCategory.PISTOL)
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 2)
                    .define(AmmoMaterials.IRON, 4)
                    .define(AmmoMaterials.LAPIS, 4)
                    .define(AmmoMaterials.GOLD, 6)
                    .define(AmmoMaterials.REDSTONE, 6)
                    .define(AmmoMaterials.DIAMOND, 9)
                    .define(AmmoMaterials.QUARTZ, 9)
                    .define(AmmoMaterials.EMERALD, 11)
                    .define(AmmoMaterials.AMETHYST, 14)
                    .define(AmmoMaterials.NETHERITE, 17)
                .build();
    }

    @Override
    public SkillType<?> getSkillForDualWield() {
        return Skills.R45_DUAL_WIELD;
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
    public IReloadManager getReloadManager(PlayerEntity player, IAttributeProvider attributeProvider) {
        return ReloadManagers.singleBulletLoading(26, player, this, player.getMainHandItem(), RELOAD_BULLET);
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.R45_ASSEMBLY;
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD;
    }

    @Override
    public IRenderConfig left() {
        return isDualWieldActive() ? RenderConfigs.R45_LEFT_DUAL : RenderConfigs.R45_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return isDualWieldActive() ? RenderConfigs.R45_RIGHT_DUAL : RenderConfigs.R45_RIGHT;
    }

    @OnlyIn(Dist.CLIENT)
    private boolean isDualWieldActive() {
        return PlayerData.hasActiveSkill(Minecraft.getInstance().player, Skills.R45_DUAL_WIELD);
    }
}
