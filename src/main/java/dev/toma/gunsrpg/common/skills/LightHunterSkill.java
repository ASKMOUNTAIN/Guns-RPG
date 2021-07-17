package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.interfaces.TickableSkill;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;

public class LightHunterSkill extends BasicSkill implements TickableSkill {

    public LightHunterSkill(SkillType<?> type) {
        super(type);
    }

    @Override
    public boolean apply(PlayerEntity user) {
        return true;
    }

    @Override
    public void onUpdate(PlayerEntity player) {
        PlayerDataFactory.get(player).ifPresent(data -> {
            PlayerSkills skills = data.getSkills();
            if (hasArmor(player)) {
                skills.lightHunterMovementSpeed = 0.015F;
            } else {
                skills.lightHunterMovementSpeed = 0.0F;
            }
        });
    }

    public boolean hasArmor(PlayerEntity player) {
        return player.getItemBySlot(EquipmentSlotType.HEAD).getItem() == Items.LEATHER_HELMET && player.getItemBySlot(EquipmentSlotType.CHEST).getItem() == Items.LEATHER_CHESTPLATE && player.getItemBySlot(EquipmentSlotType.LEGS).getItem() == Items.LEATHER_LEGGINGS && player.getItemBySlot(EquipmentSlotType.FEET).getItem() == Items.LEATHER_BOOTS;
    }
}
