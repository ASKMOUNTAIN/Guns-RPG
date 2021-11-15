package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.VectorRenderer;
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

public class VectorItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("vector/reload");

    public VectorItem(String name) {
        super(name, new Properties().setISTER(() -> VectorRenderer::new));
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.UMP45_ASSEMBLY;
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.vector)
                .caliber(AmmoType.AMMO_9MM)
                .ammo(WeaponCategory.SMG)
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 1)
                    .define(AmmoMaterials.IRON, 2)
                    .define(AmmoMaterials.LAPIS, 2)
                    .define(AmmoMaterials.GOLD, 3)
                    .define(AmmoMaterials.REDSTONE, 3)
                    .define(AmmoMaterials.DIAMOND, 5)
                    .define(AmmoMaterials.QUARTZ, 5)
                    .define(AmmoMaterials.EMERALD, 6)
                    .define(AmmoMaterials.AMETHYST, 7)
                    .define(AmmoMaterials.NETHERITE, 9)
                .build();
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.VECTOR_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.VECTOR_RIGHT;
    }
}
