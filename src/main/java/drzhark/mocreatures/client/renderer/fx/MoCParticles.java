package drzhark.mocreatures.client.renderer.fx;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.registries.Registries;
import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.client.renderer.fx.data.StarParticleData;
import drzhark.mocreatures.client.renderer.fx.data.VacuumParticleData;
import drzhark.mocreatures.client.renderer.fx.data.VanishParticleData;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

@EventBusSubscriber(modid = MoCConstants.MOD_ID)
public class MoCParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, MoCConstants.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> UNDEAD_FX =
            PARTICLES.register("undead_fx", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, ParticleType<VanishParticleData>> VANISH_FX =
            PARTICLES.register("vanish_fx", () -> new ParticleType<VanishParticleData>(false) {
                @Override
                public MapCodec<VanishParticleData> codec() {
                    return VanishParticleData.CODEC;
                }

                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, VanishParticleData> streamCodec() {
                    return VanishParticleData.STREAM_CODEC;
                }
            });

    public static final DeferredHolder<ParticleType<?>, ParticleType<StarParticleData>> STAR_FX =
            PARTICLES.register("star_fx", () -> new ParticleType<StarParticleData>(false) {
                @Override
                public MapCodec<StarParticleData> codec() {
                    return StarParticleData.CODEC;
                }

                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, StarParticleData> streamCodec() {
                    return StarParticleData.STREAM_CODEC;
                }
            });

    public static final DeferredHolder<ParticleType<?>, ParticleType<VacuumParticleData>> VACUUM_FX =
            PARTICLES.register("vacuum_fx", () -> new ParticleType<VacuumParticleData>(false) {
                @Override
                public MapCodec<VacuumParticleData> codec() {
                    return VacuumParticleData.CODEC;
                }

                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, VacuumParticleData> streamCodec() {
                    return VacuumParticleData.STREAM_CODEC;
                }
            });
}