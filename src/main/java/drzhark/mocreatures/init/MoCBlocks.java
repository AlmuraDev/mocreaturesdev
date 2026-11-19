package drzhark.mocreatures.init;

import net.minecraft.core.registries.Registries;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.block.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;

public class MoCBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MoCConstants.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MoCConstants.MOD_ID);

    public static final DeferredHolder<Block, Block> ancientSilverBlock = register("ancient_silver_block", id ->
            new MoCBlockMetal(props(id).strength(3.0F, 10.0F).mapColor(MapColor.METAL)));

    public static final DeferredHolder<Block, Block> cobbledWyvstone = register("cobbled_wyvstone", id ->
            new MoCBlockRock(props(id).strength(2.0F, 10.0F).mapColor(MapColor.STONE)));

    public static final DeferredHolder<Block, Block> cobbledDeepWyvstone = register("cobbled_deep_wyvstone", id ->
            new MoCBlockRock(props(id).strength(3.5F, 10.0F).mapColor(MapColor.STONE)));

    public static final DeferredHolder<Block, Block> wyvstone = register("wyvstone", id ->
            new MoCBlockRock(props(id).strength(1.5F, 10.0F).mapColor(MapColor.STONE)));

    public static final DeferredHolder<Block, Block> deepWyvstone = register("deep_wyvstone", id ->
            new MoCBlockRock(props(id).strength(3.0F, 10.0F).mapColor(MapColor.STONE)));

    public static final DeferredHolder<Block, Block> mossyCobbledWyvstone = register("mossy_cobbled_wyvstone", id ->
            new MoCBlockRock(props(id).strength(1.5F, 10.0F).mapColor(MapColor.STONE)));

    public static final DeferredHolder<Block, Block> mossyCobbledDeepWyvstone = register("mossy_cobbled_deep_wyvstone", id ->
            new MoCBlockRock(props(id).strength(1.5F, 10.0F).mapColor(MapColor.STONE)));

    public static final DeferredHolder<Block, Block> gleamingGlass = register("gleaming_glass", id ->
            new MoCBlockGlass(props(id).strength(0.4F).mapColor(MapColor.COLOR_LIGHT_GRAY).noOcclusion()));

    public static final DeferredHolder<Block, Block> silverSand = register("silver_sand", id ->
            new MoCBlockSand(props(id).strength(0.6F).mapColor(MapColor.COLOR_LIGHT_BLUE)));

    public static final DeferredHolder<Block, Block> silverSandstone = register("silver_sandstone", id ->
            new MoCBlockRock(props(id).strength(1.2F).mapColor(MapColor.COLOR_LIGHT_BLUE)));

    public static final DeferredHolder<Block, Block> carvedSilverSandstone = register("carved_silver_sandstone", id ->
            new MoCBlockRock(props(id).strength(1.2F).mapColor(MapColor.COLOR_LIGHT_BLUE)));

    public static final DeferredHolder<Block, Block> smoothSilverSandstone = register("smooth_silver_sandstone", id ->
            new MoCBlockRock(props(id).strength(1.2F).mapColor(MapColor.COLOR_LIGHT_BLUE)));

    public static final DeferredHolder<Block, Block> ancientOre = register("ancient_ore", id ->
            new MoCBlockOre(props(id).strength(3.0F, 5.0F).mapColor(MapColor.STONE).requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, Block> firestone = register("firestone", id ->
            new MoCBlockFirestone(props(id).strength(3.0F).lightLevel(state -> 7).mapColor(MapColor.COLOR_ORANGE)));

    public static final DeferredHolder<Block, Block> wyvernDiamondOre = register("wyvern_diamond_ore", id ->
            new MoCBlockOre(props(id).strength(3.0F, 5.0F).mapColor(MapColor.STONE).requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, Block> wyvernEmeraldOre = register("wyvern_emerald_ore", id ->
            new MoCBlockOre(props(id).strength(3.0F, 5.0F).mapColor(MapColor.STONE).requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, Block> wyvernGoldOre = register("wyvern_gold_ore", id ->
            new MoCBlockOre(props(id).strength(3.0F, 5.0F).mapColor(MapColor.STONE).requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, Block> wyvernIronOre = register("wyvern_iron_ore", id ->
            new MoCBlockOre(props(id).strength(3.0F, 5.0F).mapColor(MapColor.STONE).requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, Block> wyvernLapisOre = register("wyvern_lapis_ore", id ->
            new MoCBlockOre(props(id).strength(1.5F, 5.0F).mapColor(MapColor.STONE).requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, Block> wyvgrass = register("wyvgrass", id ->
            new MoCBlockGrass(props(id).strength(0.7F).mapColor(MapColor.COLOR_CYAN)));

    public static final DeferredHolder<Block, Block> wyvdirt = register("wyvdirt", id ->
            new MoCBlockDirt(props(id).strength(0.6F).mapColor(MapColor.DIRT)));

    public static final DeferredHolder<Block, Block> wyvwoodLeaves = register("wyvwood_leaves", id ->
            new MoCBlockLeaf(props(id).strength(0.2F).mapColor(MapColor.COLOR_LIGHT_BLUE).noOcclusion()));

    public static final DeferredHolder<Block, Block> wyvwoodSapling = register("wyvwood_sapling", id ->
            new MoCBlockSapling(props(id).noCollision().randomTicks().strength(0.0F).mapColor(MapColor.PLANT)));

    public static final DeferredHolder<Block, Block> wyvwoodLog = register("wyvwood_log", id ->
            new MoCBlockLog(props(id).strength(2.0F).mapColor(MapColor.COLOR_CYAN)));

    public static final DeferredHolder<Block, Block> tallWyvgrass = register("tall_wyvgrass", id ->
            new MoCBlockTallGrass(props(id).noCollision().strength(0.0F).mapColor(MapColor.COLOR_LIGHT_BLUE)));

    public static final DeferredHolder<Block, Block> wyvwoodPlanks = register("wyvwood_planks", id ->
            new MoCBlockPlanks(props(id).strength(2.0F, 5.0F).mapColor(MapColor.COLOR_BLUE)));

    public static final DeferredHolder<Block, Block> wyvernNestBlock = register("wyvern_nest_block", id ->
            new MoCBlockNest(props(id).strength(0.5F).mapColor(MapColor.COLOR_YELLOW)));


    /** Block properties carrying the block's registry id, which 26.3 needs before the block is constructed. */
    public static BlockBehaviour.Properties props(Identifier id) {
        return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id));
    }

    private static DeferredHolder<Block, Block> register(String name, Function<Identifier, Block> factory) {
        DeferredHolder<Block, Block> block = BLOCKS.register(name, factory);
        ITEMS.register(name, id -> {
            Item.Properties properties = new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)).useBlockDescriptionPrefix();
            if (name.equals("wyvwood_sapling")) {
                properties = properties.cookingFuel(MoCItems.fuel(name));
            }
            return new BlockItem(block.get(), properties);
        });
        return block;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
