/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.block;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.core.registries.Registries;

import javax.annotation.Nullable;

public class WyvwoodTreeGrower extends AbstractTreeGrower {
    
    private static final ResourceKey<ConfiguredFeature<?, ?>> WYVWOOD_DARK_OAK = 
            ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.parse("mocreatures:wyvwood_dark_oak"));
    private static final ResourceKey<ConfiguredFeature<?, ?>> WYVWOOD_LARGE_DARK_OAK = 
            ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.parse("mocreatures:wyvwood_large_dark_oak"));
    private static final ResourceKey<ConfiguredFeature<?, ?>> WYVWOOD_SPRUCE = 
            ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.parse("mocreatures:wyvwood_spruce"));
    private static final ResourceKey<ConfiguredFeature<?, ?>> WYVWOOD_LARGE_SPRUCE = 
            ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.parse("mocreatures:wyvwood_large_spruce"));

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
        if (random.nextInt(10) == 0) { // 10% chance for a larger variant
            return random.nextBoolean() ? WYVWOOD_LARGE_DARK_OAK : WYVWOOD_LARGE_SPRUCE; 
        } else {
            return random.nextBoolean() ? WYVWOOD_DARK_OAK : WYVWOOD_SPRUCE;
        }
    }
} 