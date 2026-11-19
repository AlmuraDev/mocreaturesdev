package drzhark.mocreatures.client.renderer.fx;

import net.minecraft.core.registries.Registries;
import net.neoforged.fml.common.EventBusSubscriber;
import com.mojang.serialization.Codec;
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
            PARTICLES.register("vanish_fx", () -> new ParticleType<VanishParticleData>(false, VanishParticleData.DESERIALIZER) {
                @Override
                public Codec<VanishParticleData> codec() {
                    return Codec.unit(new VanishParticleData(1.0F, 1.0F, 1.0F, false));
                }
            });

    public static final DeferredHolder<ParticleType<?>, ParticleType<StarParticleData>> STAR_FX =
            PARTICLES.register("star_fx", () -> new ParticleType<StarParticleData>(false, StarParticleData.DESERIALIZER) {
                @Override
                public Codec<StarParticleData> codec() {
                    return Codec.unit(new StarParticleData(1.0F, 1.0F, 1.0F)); // default white
                }
            });

    public static final DeferredHolder<ParticleType<?>, ParticleType<VacuumParticleData>> VACUUM_FX =
            PARTICLES.register("vacuum_fx", () -> new ParticleType<VacuumParticleData>(false, VacuumParticleData.DESERIALIZER) {
                @Override
                public Codec<VacuumParticleData> codec() {
                    return Codec.unit(new VacuumParticleData(1.0F, 1.0F, 1.0F));
                }
            });
}