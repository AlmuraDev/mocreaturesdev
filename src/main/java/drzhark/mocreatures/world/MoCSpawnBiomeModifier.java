package drzhark.mocreatures.world;

import com.mojang.serialization.Codec;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCreatures;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MoCSpawnBiomeModifier implements BiomeModifier {
    private static final DeferredHolder<Codec, Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "moc_spawns"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MoCConstants.MOD_ID);

    public MoCSpawnBiomeModifier() {
    }

    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            String biomeName = biome.unwrap().map(key -> key.identifier().toString(), obj -> "unknown");
            MoCreatures.LOGGER.debug("MoCSpawnBiomeModifier: Processing biome {} in phase {}", biomeName, phase);
            MoCWorldRegistry.addBiomeSpawns(biome, builder);
        }
    }

    public Codec<? extends BiomeModifier> codec() {
        return (Codec)SERIALIZER.get();
    }

    public static Codec<MoCSpawnBiomeModifier> makeCodec() {
        MoCreatures.LOGGER.info("Making MoCSpawnBiomeModifier codec");
        return Codec.unit(MoCSpawnBiomeModifier::new);
    }
}