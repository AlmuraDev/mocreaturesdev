/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.util;

import drzhark.mocreatures.MoCConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

/**
 * Mo' Creatures tool materials. 26.3 tool materials are records; their repair items come from an item tag
 * (data/mocreatures/tags/item/*_tool_materials.json), and the mining level from an "incorrect for" block tag.
 */
public final class MoCItemTier {

    public static final ToolMaterial SHARK = new ToolMaterial(BlockTags.INCORRECT_FOR_STONE_TOOL, 161, 7.0F, 2.5F, 15, repairTag("shark_tool_materials"));
    public static final ToolMaterial SILVER = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 304, 9.5F, 3.0F, 19, repairTag("silver_tool_materials"));
    public static final ToolMaterial SCORPC = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 371, 7.5F, 2.5F, 16, repairTag("scorpc_tool_materials"));
    public static final ToolMaterial SCORPF = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 371, 7.5F, 2.5F, 16, repairTag("scorpf_tool_materials"));
    public static final ToolMaterial SCORPN = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 371, 7.5F, 2.5F, 16, repairTag("scorpn_tool_materials"));
    public static final ToolMaterial SCORPD = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 371, 7.5F, 2.5F, 16, repairTag("scorpd_tool_materials"));
    public static final ToolMaterial SCORPU = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 371, 7.5F, 2.5F, 16, repairTag("scorpu_tool_materials"));
    public static final ToolMaterial STING = new ToolMaterial(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 8, 6.0F, 0.0F, 5, repairTag("sting_tool_materials"));

    private MoCItemTier() {
    }

    private static TagKey<Item> repairTag(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, name));
    }
}
