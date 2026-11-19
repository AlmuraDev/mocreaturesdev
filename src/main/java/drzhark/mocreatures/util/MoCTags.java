package drzhark.mocreatures.util;

import drzhark.mocreatures.MoCConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MoCTags {
    public static class Items {
        public static final TagKey<Item> COOKED_FISHES = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "cooked_fishes"));
        public static final TagKey<Item> RAW_FISHES = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "raw_fishes"));
    }
}
