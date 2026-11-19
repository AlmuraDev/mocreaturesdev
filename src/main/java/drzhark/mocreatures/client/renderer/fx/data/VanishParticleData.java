/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.fx.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import drzhark.mocreatures.client.renderer.fx.MoCParticles;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/** Particle options for {@link MoCParticles#VANISH_FX}; 26.3 particle options are read and sent with codecs. */
public class VanishParticleData implements ParticleOptions {

    public static final MapCodec<VanishParticleData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.FLOAT.fieldOf("red").forGetter(d -> d.red),
                    Codec.FLOAT.fieldOf("green").forGetter(d -> d.green),
                    Codec.FLOAT.fieldOf("blue").forGetter(d -> d.blue),
                    Codec.BOOL.fieldOf("implode").forGetter(d -> d.implode)
            ).apply(instance, VanishParticleData::new));
    public static final StreamCodec<ByteBuf, VanishParticleData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, d -> d.red, ByteBufCodecs.FLOAT, d -> d.green, ByteBufCodecs.FLOAT, d -> d.blue, ByteBufCodecs.BOOL, d -> d.implode, VanishParticleData::new);

    public final float red;
    public final float green;
    public final float blue;
    public final boolean implode;

    public VanishParticleData(float red, float green, float blue, boolean implode) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.implode = implode;
    }

    @Override
    public ParticleType<VanishParticleData> getType() {
        return MoCParticles.VANISH_FX.get();
    }
}
