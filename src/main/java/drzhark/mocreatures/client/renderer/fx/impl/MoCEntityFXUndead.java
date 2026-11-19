/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.fx.impl;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCEntityFXUndead extends TextureSheetParticle {
    private final TextureAtlasSprite spriteAir;
    private final TextureAtlasSprite spriteGround;

    public MoCEntityFXUndead(ClientLevel world, double x, double y, double z, SpriteSet spriteSet) {
        super(world, x, y, z, 0, 0, 0);

        this.gravity = 0.06F;
        this.lifetime = (int)(32.0D / (Math.random() * 0.8D + 0.2D));
        this.quadSize *= 0.8F;

        this.spriteGround = spriteSet.get(this.random); // fx_undead1
        this.spriteAir = spriteSet.get(this.random);    // fx_undead2

        this.setSprite(spriteAir);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSprite(this.onGround ? spriteGround : spriteAir);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z,
                                     double xSpeed, double ySpeed, double zSpeed) {
            return new MoCEntityFXUndead(world, x, y, z, spriteSet);
        }
    }
}
