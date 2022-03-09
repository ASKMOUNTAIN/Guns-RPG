package dev.toma.gunsrpg.common.item.perk;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.common.perk.PerkRegistry;
import dev.toma.gunsrpg.common.perk.PerkType;
import dev.toma.gunsrpg.resource.perks.CrystalConfiguration;
import dev.toma.gunsrpg.resource.perks.PerkConfiguration;
import dev.toma.gunsrpg.util.Lifecycle;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

import java.util.*;

public final class Crystal {

    private final int level;
    private final Collection<CrystalAttribute> attributes;

    public Crystal(int level, Collection<CrystalAttribute> attributes) {
        this.level = level;
        this.attributes = attributes;
    }

    public static Crystal generate() {
        PerkConfiguration perkConfig = GunsRPG.getModLifecycle().getPerkManager().configLoader.getConfiguration();
        CrystalConfiguration crystalConfig = perkConfig.getCrystalConfig();
        CrystalConfiguration.Spawns spawns = crystalConfig.getSpawns();
        CrystalConfiguration.Spawn spawn = spawns.getRandomSpawn();
        CrystalConfiguration.Types types = spawns.getTypeRanges();
        int crystalLevel = spawn.getLevel();
        PerkRegistry registry = PerkRegistry.getRegistry();
        Set<CrystalAttribute> attributeCollection = new HashSet<>();
        for (int i = 0; i < types.getBuffCount(); i++) {
            Perk perk = registry.getRandomPerk();
            attributeCollection.add(new CrystalAttribute(perk, PerkType.BUFF, crystalLevel));
        }
        for (int i = 0; i < types.getDebuffCount(); i++) {
            Perk perk = registry.getRandomPerk();
            attributeCollection.add(new CrystalAttribute(perk, PerkType.DEBUFF, crystalLevel));
        }
        return new Crystal(crystalLevel, attributeCollection);
    }

    public int getLevel() {
        return level;
    }

    public Collection<CrystalAttribute> listAttributes() {
        return attributes;
    }

    public CompoundNBT toNbt() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("level", level);
        ListNBT list = new ListNBT();
        attributes.forEach(attr -> list.add(attr.toNbt()));
        nbt.put("attributes", list);
        return nbt;
    }

    public static Crystal fromNbt(CompoundNBT nbt) {
        int level = nbt.getInt("level");
        ListNBT list = nbt.getList("attributes", Constants.NBT.TAG_COMPOUND);
        List<CrystalAttribute> collection = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT attrNbt = list.getCompound(i);
            collection.add(CrystalAttribute.fromNbt(attrNbt));
        }
        Comparator<CrystalAttribute> comp = Comparator.comparing(CrystalAttribute::getType, (o1, o2) -> o2.ordinal() - o1.ordinal()).thenComparing(CrystalAttribute::getValue).reversed();
        collection.sort(comp);
        return new Crystal(level, collection);
    }
}