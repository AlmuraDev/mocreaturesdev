/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.fx.impl;

import drzhark.mocreatures.client.renderer.fx.data.StarParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCEntityFXStar extends TextureSheetParticle {

    public MoCEntityFXStar(ClientLevel world, double posX, double posY, double posZ,
                           float red, float green, float blue, SpriteSet spriteSet) {
        super(world, posX, posY, posZ, 0.0D, 0.0D, 0.0D);

        this.xd *= 0.8D;
        this.yd = this.random.nextFloat() * 0.4F + 0.05F;
        this.zd *= 0.8D;

        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;
        this.alpha = 1.0F;

        this.setSize(0.01F, 0.01F);
        this.gravity = 0.06F;
        this.lifetime = (int)(64.0D / (Math.random() * 0.8D + 0.2D));
        this.quadSize *= 0.6F;

        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.quadSize *= 0.995F;
        this.yd -= 0.03D;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.9D;
        this.yd *= 0.2D;
        this.zd *= 0.9D;

        if (this.onGround) {
            this.xd *= 0.7D;
            this.zd *= 0.7D;
        }

        if (this.lifetime-- <= 0) {
            this.remove();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class Factory implements ParticleProvider<StarParticleData> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(StarParticleData data, ClientLevel world, double x, double y, double z,
                                     double xSpeed, double ySpeed, double zSpeed) {
            return new MoCEntityFXStar(world, x, y, z, data.red, data.green, data.blue, spriteSet);
        }
    }

}
