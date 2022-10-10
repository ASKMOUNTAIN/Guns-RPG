package dev.toma.gunsrpg.asm;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.client.ClientEventHandler;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.block.ICustomizableDrops;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.skills.MotherlodeSkill;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.util.Lifecycle;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.object.LazyLoader;
import dev.toma.gunsrpg.util.object.Pair;
import dev.toma.gunsrpg.world.MobSpawnManager;
import dev.toma.gunsrpg.world.cap.WorldData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.biome.ParticleEffectAmbience;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Hooks {

    public static double modifyAttackDelay(PlayerEntity player) {
        double value = player.getAttributeValue(Attributes.ATTACK_SPEED);
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        if (optional.isPresent()) {
            IPlayerData data = optional.orElse(null);
            IAttributeProvider attributeProvider = data.getAttributes();
            return value * attributeProvider.getAttributeValue(Attribs.MELEE_COOLDOWN);
        }
        return value;
    }

    public static double modifyFollowDistance(MobEntity mob) {
        double attributeValue = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        World world = mob.level;
        if (WorldData.isBloodMoon(world)) {
            return Math.max(attributeValue, ModConfig.worldConfig.bloodMoonMobAgroRange.get());
        }
        return attributeValue;
    }

    public static List<ItemStack> modifyBlockDrops(LootTable table, LootContext context) {
        List<ItemStack> drops = table.getRandomItems(context);
        BlockState state = context.getParamOrNull(LootParameters.BLOCK_STATE);
        Entity entity = context.getParamOrNull(LootParameters.THIS_ENTITY);
        if (state == null || !(entity instanceof PlayerEntity)) {
            return drops;
        }
        Block block = state.getBlock();
        if (block instanceof ICustomizableDrops) {
            return ((ICustomizableDrops) block).getCustomDrops(table, context);
        }
        PlayerEntity player = (PlayerEntity) entity;
        IPlayerData data = PlayerData.get(player).orElse(null);
        if (data == null)
            return drops;
        ISkillProvider provider = data.getSkillProvider();
        if (block.getTags().contains(BlockTags.LOGS.getName())) { // is log
            MinecraftServer server = entity.getServer();
            if (server == null || !provider.hasSkill(Skills.LUMBERJACK_I))
                return drops;
            RecipeManager manager = server.getRecipeManager();
            List<ICraftingRecipe> craftingRecipes = manager.getAllRecipesFor(IRecipeType.CRAFTING);
            for (ICraftingRecipe recipe : craftingRecipes) {
                NonNullList<Ingredient> ingredients = recipe.getIngredients();
                if (ingredients.size() == 1) {
                    Ingredient ingredient = ingredients.get(0);
                    if (ingredient.test(new ItemStack(block))) {
                        ItemStack result = recipe.getResultItem();
                        Pair<Float, Float> chances = SkillUtil.getTopHierarchySkill(Skills.LUMBERJACK_I, provider).getDropChances();
                        if (player.getRandom().nextFloat() < chances.getLeft()) {
                            drops.add(new ItemStack(result.getItem(), 1));
                        }
                        if (player.getRandom().nextFloat() < chances.getRight()) {
                            drops.add(new ItemStack(Items.STICK, 2));
                        }
                        break;
                    }
                }
            }
        } else if (block.getTags().contains(Tags.Blocks.ORES.getName())) {
            MotherlodeSkill skill = SkillUtil.getTopHierarchySkill(Skills.MOTHER_LODE_I, provider);
            int dropMultiplier = 1;
            if (skill != null) {
                dropMultiplier = skill.getDropMultiplier(player.getRandom(), data);
            }
            Iterator<ItemStack> iterator = drops.iterator();
            List<ItemStack> pendingDrops = new ArrayList<>();
            Lifecycle lifecycle = GunsRPG.getModLifecycle();
            while (iterator.hasNext()) {
                ItemStack stack = iterator.next();
                Item replacement = lifecycle.getOreDropReplacement(stack.getItem());
                if (replacement != null) {
                    pendingDrops.add(new ItemStack(replacement, Math.min(64, stack.getCount() * dropMultiplier)));
                    iterator.remove();
                } else if (stack.getItem() != block.asItem()) {
                    stack.setCount(Math.min(64, stack.getCount() * dropMultiplier));
                }
            }
            drops.addAll(pendingDrops);
        }
        return drops;
    }

    public static boolean spawnEntity(ServerWorld world, Entity entity) {
        if (entity instanceof MonsterEntity || entity instanceof IAngerable) {
            LivingEntity livingEntity = (LivingEntity) entity;
            boolean bloodmoon = WorldData.isBloodMoon(world);
            if (!MobSpawnManager.instance().processSpawn(livingEntity, world, bloodmoon)) {
                return false;
            }
        }
        return world.addEntity(entity);
    }

    public static Optional<ParticleEffectAmbience> getAmbientParticle(BiomeAmbience ambience) {
        if (ClientEventHandler.bloodmoon) {
            long time = Minecraft.getInstance().level.dayTime() % 24000L;
            return time > 13500 && time < 22500 ? Optional.of(BloodmoonAmbience.AMBIENCE.get().effectAmbience) : ambience.getAmbientParticleSettings();
        }
        return ambience.getAmbientParticleSettings();
    }

    public static Optional<SoundEvent> getAmbientSoundLoop(BiomeAmbience ambience) {
        return ClientEventHandler.bloodmoon ? Optional.of(BloodmoonAmbience.AMBIENCE.get().loopSound) : ambience.getAmbientLoopSoundEvent();
    }

    public static Optional<MoodSoundAmbience> getAmbientMood(BiomeAmbience ambience) {
        return ClientEventHandler.bloodmoon ? Optional.of(BloodmoonAmbience.AMBIENCE.get().ambience) : ambience.getAmbientMoodSettings();
    }

    private static class BloodmoonAmbience {

        private static final LazyLoader<BloodmoonAmbience> AMBIENCE = new LazyLoader<>(() -> new BloodmoonAmbience(new ParticleEffectAmbience(ParticleTypes.WHITE_ASH, 0.10f), SoundEvents.AMBIENT_BASALT_DELTAS_LOOP, new MoodSoundAmbience(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 500, 8, 2)));

        private final ParticleEffectAmbience effectAmbience;
        private final SoundEvent loopSound;
        private final MoodSoundAmbience ambience;

        public BloodmoonAmbience(ParticleEffectAmbience effectAmbience, SoundEvent loopSound, MoodSoundAmbience ambience) {
            this.effectAmbience = effectAmbience;
            this.loopSound = loopSound;
            this.ambience = ambience;
        }
    }
}
