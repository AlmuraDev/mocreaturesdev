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

/** Particle options for {@link MoCParticles#STAR_FX}; 26.3 particle options are read and sent with codecs. */
public class StarParticleData implements ParticleOptions {

    public static final MapCodec<StarParticleData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.FLOAT.fieldOf("red").forGetter(d -> d.red),
                    Codec.FLOAT.fieldOf("green").forGetter(d -> d.green),
                    Codec.FLOAT.fieldOf("blue").forGetter(d -> d.blue)
            ).apply(instance, StarParticleData::new));
    public static final StreamCodec<ByteBuf, StarParticleData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, d -> d.red, ByteBufCodecs.FLOAT, d -> d.green, ByteBufCodecs.FLOAT, d -> d.blue, StarParticleData::new);

    public final float red;
    public final float green;
    public final float blue;

    public StarParticleData(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public ParticleType<StarParticleData> getType() {
        return MoCParticles.STAR_FX.get();
    }
}
