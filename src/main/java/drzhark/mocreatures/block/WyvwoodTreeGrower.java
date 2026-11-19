/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.Feature;

/**
 * The wyvwood sapling's trees. 26.3 tree growers are weighted lists; as before, about one sapling in ten grows a large
 * tree, and the rest grow a dark oak or spruce shape.
 */
public final class WyvwoodTreeGrower {

    private static final ResourceKey<Feature> WYVWOOD_DARK_OAK = key("wyvwood_dark_oak");
    private static final ResourceKey<Feature> WYVWOOD_LARGE_DARK_OAK = key("wyvwood_large_dark_oak");
    private static final ResourceKey<Feature> WYVWOOD_SPRUCE = key("wyvwood_spruce");
    private static final ResourceKey<Feature> WYVWOOD_LARGE_SPRUCE = key("wyvwood_large_spruce");

    public static final TreeGrower WYVWOOD = new TreeGrower(
            "mocreatures:wyvwood",
            WeightedList.of(new Weighted<>(WYVWOOD_DARK_OAK, 9), new Weighted<>(WYVWOOD_SPRUCE, 9),
                    new Weighted<>(WYVWOOD_LARGE_DARK_OAK, 1), new Weighted<>(WYVWOOD_LARGE_SPRUCE, 1)),
            WeightedList.of(),
            WeightedList.of(),
            null);

    private WyvwoodTreeGrower() {
    }

    private static ResourceKey<Feature> key(String name) {
        return ResourceKey.create(Registries.FEATURE, Identifier.fromNamespaceAndPath("mocreatures", name));
    }
}
