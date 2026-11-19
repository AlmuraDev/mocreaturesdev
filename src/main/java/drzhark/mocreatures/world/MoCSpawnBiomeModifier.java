package drzhark.mocreatures.world;
import net.minecraft.core.RegistryAccess;
import com.mojang.serialization.MapCodec;

import com.mojang.serialization.Codec;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCreatures;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

public class MoCSpawnBiomeModifier implements BiomeModifier {
    /** One codec instance, registered as mocreatures:moc_spawns in MoCreatures. */
    private static final MapCodec<MoCSpawnBiomeModifier> CODEC = MapCodec.unit(MoCSpawnBiomeModifier::new);

    public MoCSpawnBiomeModifier() {
    }

    @Override
    public void modify(RegistryAccess registryAccess, Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            String biomeName = biome.unwrap().map(key -> key.identifier().toString(), obj -> "unknown");
            MoCreatures.LOGGER.debug("MoCSpawnBiomeModifier: Processing biome {} in phase {}", biomeName, phase);
            MoCWorldRegistry.addBiomeSpawns(biome, builder);
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return CODEC;
    }

    public static MapCodec<MoCSpawnBiomeModifier> makeCodec() {
        MoCreatures.LOGGER.info("Making MoCSpawnBiomeModifier codec");
        return CODEC;
    }
}