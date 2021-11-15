package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.ThompsonRenderer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class ThompsonItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("thompson/reload");

    public ThompsonItem(String name) {
        super(name, new Properties().setISTER(() -> ThompsonRenderer::new));
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.UMP45_ASSEMBLY;
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.SMG)
                .config(ModConfig.weaponConfig.thompson)
                .caliber(AmmoType.AMMO_556MM)
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 2)
                    .define(AmmoMaterials.IRON, 4)
                    .define(AmmoMaterials.LAPIS, 4)
                    .define(AmmoMaterials.GOLD, 6)
                    .define(AmmoMaterials.REDSTONE, 6)
                    .define(AmmoMaterials.DIAMOND, 8)
                    .define(AmmoMaterials.QUARTZ, 8)
                    .define(AmmoMaterials.EMERALD, 10)
                    .define(AmmoMaterials.AMETHYST, 12)
                    .define(AmmoMaterials.NETHERITE, 14)
                .build();
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.THOMPSON_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.THOMPSON_RIGHT;
    }
}
