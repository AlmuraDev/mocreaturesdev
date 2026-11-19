/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.init;

import drzhark.mocreatures.MoCConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class MoCTagProvider extends BlockTagsProvider {

    public MoCTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MoCConstants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Add MoC ore blocks to their specific forge ore tags
        this.tag(Tags.Blocks.ORES_DIAMOND)
            .add(MoCBlocks.wyvernDiamondOre.get());
        this.tag(Tags.Blocks.ORES_EMERALD)
            .add(MoCBlocks.wyvernEmeraldOre.get());
        this.tag(Tags.Blocks.ORES_GOLD)
            .add(MoCBlocks.wyvernGoldOre.get());
        this.tag(Tags.Blocks.ORES_IRON)
            .add(MoCBlocks.wyvernIronOre.get());
        this.tag(Tags.Blocks.ORES_LAPIS)
            .add(MoCBlocks.wyvernLapisOre.get());

        // Add ancient ore to the general ores tag since it's a custom ore type
        this.tag(Tags.Blocks.ORES)
            .add(MoCBlocks.ancientOre.get());

        // Add MoC leaves to the minecraft:leaves tag
        this.tag(BlockTags.LEAVES)
            .add(MoCBlocks.wyvwoodLeaves.get());

        // Mining tool requirements - what can mine these blocks
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(MoCBlocks.ancientOre.get())
            .add(MoCBlocks.wyvernDiamondOre.get())
            .add(MoCBlocks.wyvernEmeraldOre.get())
            .add(MoCBlocks.wyvernGoldOre.get())
            .add(MoCBlocks.wyvernIronOre.get())
            .add(MoCBlocks.wyvernLapisOre.get());

        // Mining level requirements based on actual hardness values
        // Ancient ore (3.0F hardness) - requires iron pickaxe
        this.tag(BlockTags.NEEDS_IRON_TOOL)
            .add(MoCBlocks.ancientOre.get());

        // Wyvern diamond ore (3.0F hardness) - requires iron pickaxe
        this.tag(BlockTags.NEEDS_IRON_TOOL)
            .add(MoCBlocks.wyvernDiamondOre.get());

        // Wyvern emerald ore (3.0F hardness) - requires iron pickaxe  
        this.tag(BlockTags.NEEDS_IRON_TOOL)
            .add(MoCBlocks.wyvernEmeraldOre.get());

        // Wyvern gold ore (3.0F hardness) - requires iron pickaxe
        this.tag(BlockTags.NEEDS_IRON_TOOL)
            .add(MoCBlocks.wyvernGoldOre.get());

        // Wyvern iron ore (3.0F hardness) - requires iron pickaxe
        this.tag(BlockTags.NEEDS_IRON_TOOL)
            .add(MoCBlocks.wyvernIronOre.get());

        // Wyvern lapis ore (1.5F hardness) - requires stone pickaxe
        this.tag(BlockTags.NEEDS_STONE_TOOL)
            .add(MoCBlocks.wyvernLapisOre.get());
    }
}