package drzhark.mocreatures.client.renderer.fx.impl;

import drzhark.mocreatures.client.renderer.fx.data.VanishParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCEntityFXVanish extends TextureSheetParticle {

    private final double portalPosX;
    private final double portalPosY;
    private final double portalPosZ;
    private final boolean implode;

    public MoCEntityFXVanish(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed,
                             float red, float green, float blue, boolean implode, SpriteSet spriteSet) {
        super(world, x, y, z, 0, 0, 0);

        this.xd = xSpeed;
        this.yd = ySpeed * 5D;
        this.zd = zSpeed;

        this.portalPosX = this.x = x;
        this.portalPosY = this.y = y;
        this.portalPosZ = this.z = z;

        this.implode = implode;
        this.lifetime = (int)(Math.random() * 10.0D) + 70;

        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;
        this.alpha = 1.0F;

        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        int speeder = implode ? (this.lifetime / 2) : 0;
        float sizeExp = implode ? 5.0F : 2.0F;

        float var1 = (float) (this.age + speeder) / (float) this.lifetime;
        float var2 = var1;
        var1 = -var1 + var1 * var1 * sizeExp;
        var1 = 1.0F - var1;

        this.x = this.portalPosX + this.xd * var1;
        this.y = this.portalPosY + this.yd * var1 + (1.0F - var2);
        this.z = this.portalPosZ + this.zd * var1;

        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<VanishParticleData> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(VanishParticleData data, ClientLevel world,
                                     double x, double y, double z,
                                     double xSpeed, double ySpeed, double zSpeed) {
            return new MoCEntityFXVanish(world, x, y, z,
                    xSpeed, ySpeed, zSpeed,
                    data.red, data.green, data.blue, data.implode,
                    spriteSet);
        }
    }
}
