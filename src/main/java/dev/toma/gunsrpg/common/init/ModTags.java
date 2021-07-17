package dev.toma.gunsrpg.common.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {

    public static void init() {
        Blocks.init();
        Items.init();
    }

    public static class Blocks {

        public static final Tags.IOptionalNamedTag<Block> ORES_AMETHYST = forge("ores/amethyst");

        private static void init() {
        }

        private static Tags.IOptionalNamedTag<Block> forge(String path) {
            return tag("forge", path);
        }

        private static Tags.IOptionalNamedTag<Block> tag(String namespace, String path) {
            return BlockTags.createOptional(new ResourceLocation(namespace, path));
        }
    }

    public static class Items {
        public static final Tags.IOptionalNamedTag<Item> ORES_AMETHYST = forge("ores/amethyst");

        private static void init() {
        }

        private static Tags.IOptionalNamedTag<Item> forge(String path) {
            return tag("forge", path);
        }

        private static Tags.IOptionalNamedTag<Item> tag(String namespace, String path) {
            return ItemTags.createOptional(new ResourceLocation(namespace, path));
        }
    }
}
