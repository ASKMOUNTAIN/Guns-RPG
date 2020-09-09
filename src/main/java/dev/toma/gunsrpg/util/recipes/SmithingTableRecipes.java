package dev.toma.gunsrpg.util.recipes;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.tileentity.TileEntitySmithingTable;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SmithingTableRecipes {

    private static final List<SmithingRecipe> RECIPES = new ArrayList<>();

    /*
    0 1 2
    3 4 5
    6 7 8
     */
    public static void register() {
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.BACON_BURGER).add(0, Items.COOKED_BEEF).add(new int[]{1,3}, Items.BREAD).add(4, Items.COOKED_PORKCHOP).req(ModRegistry.Skills.LOCAL_CHEF).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.FISH_AND_CHIPS).add(new int[]{3,5}, Items.BAKED_POTATO).add(4, Items.COOKED_FISH).add(7, Items.CARROT).req(ModRegistry.Skills.LOCAL_CHEF).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.GARDEN_SOUP).add(1, Blocks.BROWN_MUSHROOM).add(new int[]{3,5}, Items.CARROT).add(4, Items.BEETROOT).add(7, Items.WATER_BUCKET).add(new int[]{6,8}, Items.POTATO).req(ModRegistry.Skills.LOCAL_CHEF).bucket().recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.CHICKEN_DINNER).add(new int[]{3,5}, Items.COOKED_CHICKEN).add(new int[]{4,7}, Items.BAKED_POTATO).add(new int[]{6,8}, Items.CARROT).req(ModRegistry.Skills.LOCAL_CHEF).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.DELUXE_MEAL).add(new int[]{0, 2}, Items.BAKED_POTATO).add(1, Items.CARROT).add(3, Items.COOKED_PORKCHOP).add(4, Items.COOKED_BEEF).add(5, Items.COOKED_CHICKEN).add(new int[]{6,7,8}, Items.BREAD).req(ModRegistry.Skills.MASTER_CHEF).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.MEATY_STEW_XXL).add(new int[]{0,3}, Items.COOKED_MUTTON).add(new int[]{1,4}, Items.COOKED_PORKCHOP).add(new int[]{2,5}, Items.COOKED_BEEF).add(new int[]{6,8}, Items.POTATO).add(7, Items.WATER_BUCKET).req(ModRegistry.Skills.MASTER_CHEF).bucket().recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.RABBIT_CREAMY_SOUP).add(new int[]{0,2}, Items.POTATO).add(1, Items.MILK_BUCKET).add(new int[]{3,5}, Items.CARROT).add(4, Blocks.BROWN_MUSHROOM).add(new int[]{6,8}, Items.BREAD).add(7, Items.COOKED_RABBIT).req(ModRegistry.Skills.MASTER_CHEF).bucket().recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.SHEPHERDS_PIE).add(new int[]{0,1,2}, Blocks.BROWN_MUSHROOM).add(new int[]{3,5}, Items.COOKED_MUTTON).add(4, Items.BAKED_POTATO).add(new int[]{6,7,8}, Items.BREAD).req(ModRegistry.Skills.MASTER_CHEF).recipe());
        register(new RecipeBuilder().out(Items.GUNPOWDER, SkillUtil::getGunpowderCraftAmount).add(new int[]{0, 4}, 1, Items.COAL).add(1, Items.DYE, 15).add(3, Items.SUGAR).req(ModRegistry.Skills.GUNPOWDER_NOVICE).recipe());
        register(new RecipeBuilder().out(new ItemStack(Items.DYE, 1, 15), SkillUtil::getBonemealCraftAmount).add(0, Items.BONE).req(ModRegistry.Skills.BONE_GRINDER_I).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.LARGE_BULLET_CASING).add(new int[]{0, 1}, ModRegistry.GRPGItems.SMALL_BULLET_CASING).req(ModRegistry.Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.SHOTGUN_SHELL).add(0, Items.DYE, 1).add(1, ModRegistry.GRPGItems.LARGE_BULLET_CASING).add(2, Items.PAPER).req(ModRegistry.Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.BOLT_FLETCHING).add(0, Items.IRON_NUGGET).add(3, Items.FEATHER).add(6, Items.GOLD_NUGGET).req(ModRegistry.Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.SMALL_BULLET_CASING, 16).add(0, Items.COAL, 0).add(1, Items.IRON_INGOT).add(2, Items.GOLD_INGOT).req(ModRegistry.Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.WOODEN_AMMO_9MM, SkillUtil::getAmmoAmount).add(0, Blocks.PLANKS).add(3, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).req(ModRegistry.Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.STONE_AMMO_9MM, SkillUtil::getAmmoAmount).add(0, Blocks.STONE).add(3, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).req(ModRegistry.Skills.STONE_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.IRON_AMMO_9MM, SkillUtil::getAmmoAmount).add(0, Items.IRON_INGOT).add(3, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).req(ModRegistry.Skills.IRON_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.GOLD_AMMO_9MM, SkillUtil::getAmmoAmount).add(0, Items.GOLD_INGOT).add(3, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).req(ModRegistry.Skills.GOLD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.DIAMOND_AMMO_9MM, SkillUtil::getAmmoAmount).add(0, Items.DIAMOND).add(3, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).req(ModRegistry.Skills.DIAMOND_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.EMERALD_AMMO_9MM, SkillUtil::getAmmoAmount).add(0, Items.EMERALD).add(3, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).req(ModRegistry.Skills.EMERALD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.AMETHYST_AMMO_9MM, SkillUtil::getAmmoAmount).add(0, ModRegistry.GRPGItems.AMETHYST).add(3, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).req(ModRegistry.Skills.AMETHYST_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.WOODEN_AMMO_45ACP, SkillUtil::getAmmoAmount).add(0, Blocks.PLANKS).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).req(ModRegistry.Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.STONE_AMMO_45ACP, SkillUtil::getAmmoAmount).add(0, Blocks.STONE).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).req(ModRegistry.Skills.STONE_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.IRON_AMMO_45ACP, SkillUtil::getAmmoAmount).add(0, Items.IRON_INGOT).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).req(ModRegistry.Skills.IRON_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.GOLD_AMMO_45ACP, SkillUtil::getAmmoAmount).add(0, Items.GOLD_INGOT).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).req(ModRegistry.Skills.GOLD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.DIAMOND_AMMO_45ACP, SkillUtil::getAmmoAmount).add(0, Items.DIAMOND).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).req(ModRegistry.Skills.DIAMOND_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.EMERALD_AMMO_45ACP, SkillUtil::getAmmoAmount).add(0, Items.EMERALD).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).req(ModRegistry.Skills.EMERALD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.AMETHYST_AMMO_45ACP, SkillUtil::getAmmoAmount).add(0, ModRegistry.GRPGItems.AMETHYST).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).req(ModRegistry.Skills.AMETHYST_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.WOODEN_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).add(0, Blocks.PLANKS).add(3, Items.STICK).add(6, ModRegistry.GRPGItems.BOLT_FLETCHING).req(ModRegistry.Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.STONE_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).add(0, Blocks.STONE).add(3, Items.STICK).add(6, ModRegistry.GRPGItems.BOLT_FLETCHING).req(ModRegistry.Skills.STONE_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.IRON_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).add(0, Items.IRON_INGOT).add(3, Items.STICK).add(6, ModRegistry.GRPGItems.BOLT_FLETCHING).req(ModRegistry.Skills.IRON_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.GOLD_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).add(0, Items.GOLD_INGOT).add(3, Items.STICK).add(6, ModRegistry.GRPGItems.BOLT_FLETCHING).req(ModRegistry.Skills.GOLD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.DIAMOND_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).add(0, Items.DIAMOND).add(3, Items.STICK).add(6, ModRegistry.GRPGItems.BOLT_FLETCHING).req(ModRegistry.Skills.DIAMOND_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.EMERALD_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).add(0, Items.EMERALD).add(3, Items.STICK).add(6, ModRegistry.GRPGItems.BOLT_FLETCHING).req(ModRegistry.Skills.EMERALD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.AMETHYST_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).add(0, ModRegistry.GRPGItems.AMETHYST).add(3, Items.STICK).add(6, ModRegistry.GRPGItems.BOLT_FLETCHING).req(ModRegistry.Skills.AMETHYST_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.WOODEN_AMMO_12G, SkillUtil::getAmmoAmount).add(0, Blocks.PLANKS).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SHOTGUN_SHELL).req(ModRegistry.Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.STONE_AMMO_12G, SkillUtil::getAmmoAmount).add(0, Blocks.STONE).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SHOTGUN_SHELL).req(ModRegistry.Skills.STONE_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.IRON_AMMO_12G, SkillUtil::getAmmoAmount).add(0, Items.IRON_INGOT).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SHOTGUN_SHELL).req(ModRegistry.Skills.IRON_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.GOLD_AMMO_12G, SkillUtil::getAmmoAmount).add(0, Items.GOLD_INGOT).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SHOTGUN_SHELL).req(ModRegistry.Skills.GOLD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.DIAMOND_AMMO_12G, SkillUtil::getAmmoAmount).add(0, Items.DIAMOND).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SHOTGUN_SHELL).req(ModRegistry.Skills.DIAMOND_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.EMERALD_AMMO_12G, SkillUtil::getAmmoAmount).add(0, Items.EMERALD).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SHOTGUN_SHELL).req(ModRegistry.Skills.EMERALD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.AMETHYST_AMMO_12G, SkillUtil::getAmmoAmount).add(0, ModRegistry.GRPGItems.AMETHYST).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.SHOTGUN_SHELL).req(ModRegistry.Skills.AMETHYST_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.WOODEN_AMMO_556MM, SkillUtil::getAmmoAmount).add(0, Blocks.PLANKS).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).req(ModRegistry.Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.STONE_AMMO_556MM, SkillUtil::getAmmoAmount).add(0, Blocks.STONE).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).req(ModRegistry.Skills.STONE_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.IRON_AMMO_556MM, SkillUtil::getAmmoAmount).add(0, Items.IRON_INGOT).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).req(ModRegistry.Skills.IRON_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.GOLD_AMMO_556MM, SkillUtil::getAmmoAmount).add(0, Items.GOLD_INGOT).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).req(ModRegistry.Skills.GOLD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.DIAMOND_AMMO_556MM, SkillUtil::getAmmoAmount).add(0, Items.DIAMOND).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).req(ModRegistry.Skills.DIAMOND_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.EMERALD_AMMO_556MM, SkillUtil::getAmmoAmount).add(0, Items.EMERALD).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).req(ModRegistry.Skills.EMERALD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.AMETHYST_AMMO_556MM, SkillUtil::getAmmoAmount).add(0, ModRegistry.GRPGItems.AMETHYST).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).req(ModRegistry.Skills.AMETHYST_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.WOODEN_AMMO_762MM, SkillUtil::getAmmoAmount).add(0, Blocks.PLANKS).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).req(ModRegistry.Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.STONE_AMMO_762MM, SkillUtil::getAmmoAmount).add(0, Blocks.STONE).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).req(ModRegistry.Skills.STONE_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.IRON_AMMO_762MM, SkillUtil::getAmmoAmount).add(0, Items.IRON_INGOT).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).req(ModRegistry.Skills.IRON_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.GOLD_AMMO_762MM, SkillUtil::getAmmoAmount).add(0, Items.GOLD_INGOT).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).req(ModRegistry.Skills.GOLD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.DIAMOND_AMMO_762MM, SkillUtil::getAmmoAmount).add(0, Items.DIAMOND).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).req(ModRegistry.Skills.DIAMOND_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.EMERALD_AMMO_762MM, SkillUtil::getAmmoAmount).add(0, Items.EMERALD).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).req(ModRegistry.Skills.EMERALD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.AMETHYST_AMMO_762MM, SkillUtil::getAmmoAmount).add(0, ModRegistry.GRPGItems.AMETHYST).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).req(ModRegistry.Skills.AMETHYST_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.GRENADE).add(new int[]{1, 3, 5, 7}, Items.IRON_INGOT).add(4, Blocks.TNT).add(2, Items.FLINT_AND_STEEL).req(ModRegistry.Skills.GRENADES).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.MASSIVE_GRENADE).add(new int[]{0, 2, 3, 5, 6, 7, 8}, Items.GOLD_NUGGET).add(1, Blocks.TNT).add(4, ModRegistry.GRPGItems.GRENADE).req(ModRegistry.Skills.MASSIVE_GRENADES).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.IMPACT_GRENADE).add(new int[]{0, 2, 6, 8}, Items.REDSTONE).add(new int[]{3, 7, 5}, Items.GLOWSTONE_DUST).add(1, Items.BLAZE_POWDER).add(4, ModRegistry.GRPGItems.GRENADE).req(ModRegistry.Skills.IMPACT_GRENADES).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.WOODEN_HAMMER).add(new int[]{3, 0, 1, 2, 5}, Blocks.LOG).add(new int[]{4, 7}, Items.STICK).req(ModRegistry.Skills.HAMMER_I, ModRegistry.Skills.HEAVY_PICKAXE_II, ModRegistry.Skills.MOTHER_LODE_II).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.STONE_HAMMER).add(new int[]{3, 0, 1, 2, 5}, Blocks.STONE).add(new int[]{4, 7}, Items.STICK).req(ModRegistry.Skills.HAMMER_II, ModRegistry.Skills.HEAVY_PICKAXE_III, ModRegistry.Skills.MOTHER_LODE_III).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.IRON_HAMMER).add(new int[]{3, 0, 1, 2, 5}, Items.IRON_INGOT).add(new int[]{4, 7}, Items.STICK).req(ModRegistry.Skills.HAMMER_III, ModRegistry.Skills.HEAVY_PICKAXE_IV, ModRegistry.Skills.MOTHER_LODE_IV).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.AMETHYST).add(1, Items.DIAMOND).add(new int[]{3, 5}, Items.QUARTZ).add(4, Items.BLAZE_POWDER).add(7, Items.EMERALD).req(ModRegistry.Skills.MINERALOGIST).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.PLASTER_CAST).add(new int[]{0, 2, 6, 8}, Items.DYE, 15).add(new int[]{1, 3, 5, 7}, Blocks.CARPET).add(4, Items.DIAMOND).req(ModRegistry.Skills.MEDIC).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.BANDAGE, 2).add(new int[]{0, 8}, Items.DYE, 4).add(new int[]{1, 3, 5, 7}, Items.PAPER).add(new int[]{2, 6}, Items.SLIME_BALL).add(4, new ItemStack(Blocks.WOOL, 1, 0)).req(ModRegistry.Skills.MEDIC).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.VACCINE).add(8, Items.GOLDEN_APPLE).add(6, Items.SPECKLED_MELON).add(4, ModRegistry.GRPGItems.AMETHYST).add(2, Items.GOLDEN_CARROT).add(0, Items.IRON_INGOT).add(new int[]{1, 3, 5, 7}, Blocks.GLASS_PANE).req(ModRegistry.Skills.DOCTOR).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.ANTIDOTUM_PILLS, 2).add(new int[]{0, 2}, Items.PAPER).add(1, Items.SUGAR).add(3, new ItemStack(Blocks.RED_FLOWER, 1, 2)).add(4, Items.EMERALD).add(5, Items.DYE, 2).add(6, Blocks.YELLOW_FLOWER).add(7, new ItemStack(Blocks.RED_FLOWER, 1, 1)).add(8, Blocks.RED_FLOWER).req(ModRegistry.Skills.DOCTOR).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.ANALGETICS).add(new int[] {0, 2}, Items.PAPER).add(1, Items.SUGAR).add(3, Blocks.YELLOW_FLOWER).add(4, Items.DYE, 2).add(5, Blocks.RED_FLOWER).add(new int[] {6, 7, 8}, Items.DYE, 4).req(ModRegistry.Skills.PHARMACIST_I).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.STEREOIDS).add(0, Items.IRON_INGOT).add(new int[]{1,3,5,7}, Blocks.GLASS_PANE).add(new int[]{2,6}, Items.GOLDEN_CARROT).add(4, Blocks.REDSTONE_BLOCK).add(8, Items.SUGAR).req(ModRegistry.Skills.PHARMACIST_II).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.ADRENALINE).add(0, Items.IRON_INGOT).add(new int[]{1,3,5,7}, Blocks.GLASS_PANE).add(new int[]{2,6}, Items.SPECKLED_MELON).add(4, Items.FERMENTED_SPIDER_EYE).add(8, new ItemStack(Blocks.RED_FLOWER, 1, 1)).req(ModRegistry.Skills.PHARMACIST_III).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.PAINKILLERS).add(new int[]{0, 2}, Items.PAPER).add(1, Items.SUGAR).add(new int[]{3,4,5}, Items.FERMENTED_SPIDER_EYE).add(new int[]{6,8}, Items.NETHER_WART).add(7, Blocks.REDSTONE_BLOCK).req(ModRegistry.Skills.PHARMACIST_IV).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.MORPHINE).add(0, Items.IRON_INGOT).add(new int[]{1,3,5,7}, Blocks.GLASS_PANE).add(2, ModRegistry.GRPGItems.STEREOIDS).add(4, ModRegistry.GRPGItems.PAINKILLERS).add(6, ModRegistry.GRPGItems.ADRENALINE).add(8, Items.ENDER_EYE).req(ModRegistry.Skills.PHARMACIST_V).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.BARREL).add(new int[]{0,1,2,6,7,8}, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE).req(ModRegistry.Skills.GUN_PARTS_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.GUN_PARTS).add(new int[]{0,2,6,8}, Blocks.IRON_BARS).add(new int[]{1, 7}, Blocks.IRON_TRAPDOOR).add(new int[]{3, 5}, Items.IRON_INGOT).add(4, Items.IRON_PICKAXE).req(ModRegistry.Skills.GUN_PARTS_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.IRON_STOCK).add(new int[]{0, 1}, Blocks.IRON_BLOCK).add(new int[]{2, 4}, Items.IRON_INGOT).add(5, Items.FLINT_AND_STEEL).req(ModRegistry.Skills.GUN_PARTS_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.LONG_BARREL).add(new int[]{1, 2}, ModRegistry.GRPGItems.BARREL).req(ModRegistry.Skills.GUN_PARTS_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.MAGAZINE).add(new int[]{1, 2, 4, 5, 6, 7}, Items.IRON_INGOT).req(ModRegistry.Skills.GUN_PARTS_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.SMALL_IRON_STOCK).add(0, Blocks.IRON_BLOCK).add(new int[]{1, 3}, Items.IRON_INGOT).add(4, Items.FLINT_AND_STEEL).req(ModRegistry.Skills.GUN_PARTS_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.WOODEN_STOCK).add(new int[]{0, 1}, Blocks.LOG, Blocks.LOG2).add(2, Items.IRON_INGOT).add(4, Blocks.WOODEN_SLAB).add(5, Items.FLINT_AND_STEEL).req(ModRegistry.Skills.GUN_PARTS_SMITH).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.PISTOL).add(0, ModRegistry.GRPGItems.BARREL).add(1, ModRegistry.GRPGItems.GUN_PARTS).add(2, ModRegistry.GRPGItems.SMALL_IRON_STOCK).req(ModRegistry.Skills.PISTOL_ASSEMBLY).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.SMG).add(0, ModRegistry.GRPGItems.BARREL).add(1, ModRegistry.GRPGItems.GUN_PARTS).add(2, ModRegistry.GRPGItems.IRON_STOCK).add(4, ModRegistry.GRPGItems.MAGAZINE).req(ModRegistry.Skills.SMG_ASSEMBLY).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.CROSSBOW).add(3, Blocks.TRIPWIRE_HOOK).add(4, ModRegistry.GRPGItems.GUN_PARTS).add(5, ModRegistry.GRPGItems.WOODEN_STOCK).add(new int[]{1, 7}, Items.BOW).req(ModRegistry.Skills.CROSSBOW_ASSEMBLY).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.SHOTGUN).add(0, ModRegistry.GRPGItems.LONG_BARREL).add(1, ModRegistry.GRPGItems.GUN_PARTS).add(2, ModRegistry.GRPGItems.WOODEN_STOCK).add(4, ModRegistry.GRPGItems.MAGAZINE).req(ModRegistry.Skills.SHOTGUN_ASSEMBLY).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.ASSAULT_RIFLE).add(0, ModRegistry.GRPGItems.LONG_BARREL).add(1, ModRegistry.GRPGItems.GUN_PARTS).add(2, ModRegistry.GRPGItems.IRON_STOCK).add(4, ModRegistry.GRPGItems.MAGAZINE).req(ModRegistry.Skills.ASSAULT_RIFLE_ASSEMBLY).recipe());
        register(new RecipeBuilder().out(ModRegistry.GRPGItems.SNIPER_RIFLE).add(0, ModRegistry.GRPGItems.LONG_BARREL).add(1, ModRegistry.GRPGItems.GUN_PARTS).add(2, ModRegistry.GRPGItems.WOODEN_STOCK).req(ModRegistry.Skills.SNIPER_RIFLE_ASSEMBLY).recipe());
    }

    public static SmithingRecipe findRecipe(TileEntitySmithingTable table, int selected) {
        if (selected >= 0 && selected < RECIPES.size()) {
            SmithingRecipe recipe = getRecipeById(selected);
            boolean valid = true;
            for (SmithingIngredient ingredient : recipe.getIngredients()) {
                ItemStack stack = table.getStackInSlot(ingredient.getIndex());
                if (!ModUtils.contains(stack, ingredient.items, ItemStack::isItemEqual)) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                return recipe;
            }
        }
        List<SmithingRecipe> validRecipes = new ArrayList<>();
        for (SmithingRecipe recipe : RECIPES) {
            boolean valid = true;
            for (SmithingIngredient ingredient : recipe.ingredients) {
                ItemStack item = table.getStackInSlot(ingredient.id);
                if (!ModUtils.contains(item, ingredient.items, ItemStack::isItemEqual)) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                validRecipes.add(recipe);
            }
        }
        if(!validRecipes.isEmpty()) {
            if(validRecipes.size() == 1) {
                return validRecipes.get(0);
            } else {
                SmithingRecipe matched = null;
                for (SmithingRecipe recipe : validRecipes) {
                    if (matched == null || recipe.ingredients.length > matched.ingredients.length) {
                        matched = recipe;
                    }
                }
                return matched;
            }
        }
        return null;
    }

    public static SmithingRecipe getRecipeByOutput(Item output) {
        for(SmithingRecipe recipe : RECIPES) {
            if(recipe.output.item.getItem() == output) {
                return recipe;
            }
        }
        return RECIPES.get(0);
    }

    public static int getRecipeId(SmithingRecipe recipe) {
        return RECIPES.indexOf(recipe);
    }

    public static SmithingRecipe getRecipeById(int id) {
        if (id >= 0 && id < RECIPES.size()) {
            return RECIPES.get(id);
        }
        return RECIPES.get(0);
    }

    private static void register(SmithingRecipe recipe) {
        RECIPES.add(recipe);
    }

    public static List<SmithingRecipe> getAvailableRecipes(EntityPlayer player) {
        PlayerSkills skills = PlayerDataFactory.get(player).getSkills();
        return RECIPES.stream().filter(r -> canCraftRecipe(r, skills)).collect(Collectors.toList());
    }

    public static boolean canCraftRecipe(SmithingRecipe recipe, PlayerSkills skills) {
        if (recipe.requiredTypes != null && recipe.requiredTypes.length > 0) {
            for (SkillType<?> type : recipe.requiredTypes) {
                if (!skills.hasSkill(type)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static class SmithingRecipe {
        private final SmithingIngredient[] ingredients;
        private final SmithingRecipeOutput output;
        private final SkillType<?>[] requiredTypes;
        private final boolean returnsBucket;

        public SmithingRecipe(RecipeBuilder builder) {
            this.ingredients = builder.ingredientList.toArray(new SmithingIngredient[0]);
            this.output = builder.output;
            this.requiredTypes = builder.requiredTypes;
            this.returnsBucket = builder.returnsBucket;
        }

        public ItemStack getOutputForDisplay() {
            return output.item.copy();
        }

        public ItemStack getOutput(EntityPlayer player) {
            if(!player.world.isRemote && returnsBucket) {
                player.addItemStackToInventory(new ItemStack(Items.BUCKET));
            }
            return output.getResult(player);
        }

        public SkillType<?>[] getRequiredTypes() {
            return requiredTypes;
        }

        public SmithingIngredient[] getIngredients() {
            return ingredients;
        }
    }

    public static class RecipeBuilder {
        private final List<SmithingIngredient> ingredientList = new ArrayList<>();
        private SmithingRecipeOutput output;
        private SkillType<?>[] requiredTypes;
        private boolean returnsBucket;

        public RecipeBuilder add(int id, ItemStack stack) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, stack));
            return this;
        }

        public RecipeBuilder add(int id, Block... blocks) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, Arrays.stream(blocks).map(ItemStack::new).toArray(ItemStack[]::new)));
            return this;
        }

        public RecipeBuilder add(int id, Block block) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, Item.getItemFromBlock(block)));
            return this;
        }

        public RecipeBuilder add(int id, Item item) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, item));
            return this;
        }

        public RecipeBuilder add(int id, Item item, int meta) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, item, meta));
            return this;
        }

        public RecipeBuilder add(int id, Item... items) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, 0, items));
            return this;
        }

        public RecipeBuilder add(int[] ints, Item item, int meta) {
            for (int id : ints) {
                if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
                ingredientList.add(new SmithingIngredient(id, item, meta));
            }
            return this;
        }

        public RecipeBuilder add(int[] ints, Item... items) {
            for (int id : ints) {
                if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
                ingredientList.add(new SmithingIngredient(id, 0, items));
            }
            return this;
        }

        public RecipeBuilder add(int[] ints, int meta, Item... items) {
            for (int id : ints) {
                if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
                ingredientList.add(new SmithingIngredient(id, meta, items));
            }
            return this;
        }

        public RecipeBuilder add(int[] ints, Block block) {
            return this.add(ints, Item.getItemFromBlock(block));
        }

        public RecipeBuilder add(int[] ints, Block... blocks) {
            return this.add(ints, Arrays.stream(blocks).map(Item::getItemFromBlock).toArray(Item[]::new));
        }

        public RecipeBuilder out(Item item) {
            this.output = new SmithingRecipeOutput(item, p -> 1);
            return this;
        }

        public RecipeBuilder out(ItemStack stack, Function<EntityPlayer, Integer> toIntFunction) {
            this.output = new SmithingRecipeOutput(stack, toIntFunction);
            return this;
        }

        public RecipeBuilder out(Item item, int amount) {
            this.output = new SmithingRecipeOutput(item, p -> amount);
            return this;
        }

        public RecipeBuilder out(Item item, Function<EntityPlayer, Integer> amountFunction) {
            this.output = new SmithingRecipeOutput(item, amountFunction);
            return this;
        }

        public RecipeBuilder req(SkillType<?>... types) {
            this.requiredTypes = types;
            return this;
        }

        public RecipeBuilder bucket() {
            this.returnsBucket = true;
            return this;
        }

        public SmithingRecipe recipe() {
            return new SmithingRecipe(this);
        }
    }

    public static class SmithingIngredient {

        private final int id;
        private final ItemStack[] items;

        private SmithingIngredient(int id, Item item, int meta) {
            this(id, new ItemStack(item, 1, meta));
        }

        private SmithingIngredient(int id, Item item) {
            this(id, new ItemStack(item));
        }

        private SmithingIngredient(int id, int meta, Item... items) {
            this(id, Arrays.stream(items).map(item -> new ItemStack(item, 1, meta)).toArray(ItemStack[]::new));
        }

        private SmithingIngredient(int id, ItemStack item) {
            this(id, new ItemStack[]{item});
        }

        private SmithingIngredient(int id, ItemStack[] item) {
            this.id = id;
            this.items = item;
        }

        public int getIndex() {
            return id;
        }

        public ItemStack getFirstItem() {
            return items[0].copy();
        }

        public Item[] getEntries() {
            Item[] arr = new Item[items.length];
            int j = 0;
            for(ItemStack stack : items) {
                arr[j] = stack.getItem();
                ++j;
            }
            return arr;
        }
    }

    public static class SmithingRecipeOutput {

        private final Function<EntityPlayer, Integer> amount;
        private final ItemStack item;

        private SmithingRecipeOutput(Item item, Function<EntityPlayer, Integer> amount) {
            this.item = new ItemStack(item);
            this.amount = amount;
        }

        private SmithingRecipeOutput(ItemStack stack, Function<EntityPlayer, Integer> toIntFunction) {
            this.item = stack;
            this.amount = toIntFunction;
        }

        public ItemStack getResult(EntityPlayer player) {
            ItemStack stack = item.copy();
            stack.setCount(amount.apply(player));
            return stack;
        }
    }
}
