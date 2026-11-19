/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.fx.impl;

import drzhark.mocreatures.client.renderer.fx.data.VacuumParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCEntityFXVacuum extends TextureSheetParticle {
    private final float initialScale;
    private final double startX, startY, startZ;

    public MoCEntityFXVacuum(ClientLevel world, double x, double y, double z,
                             double xSpeed, double ySpeed, double zSpeed,
                             float red, float green, float blue,
                             SpriteSet spriteSet) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);

        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;

        this.startX = x;
        this.startY = y;
        this.startZ = z;

        this.initialScale = this.quadSize = this.random.nextFloat() * 0.2F + 0.5F;
        this.lifetime = (int)(Math.random() * 10.0D) + 30;

        this.setSprite(spriteSet.get(0, lifetime)); // static texture
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        float t = (float)this.age / (float)this.lifetime;
        float scaleFactor = -t + t * t * 2.0F;
        scaleFactor = 1.0F - scaleFactor;
        this.x = this.startX + this.xd * scaleFactor;
        this.y = this.startY + this.yd * scaleFactor + (1.0F - t);
        this.z = this.startZ + this.zd * scaleFactor;

        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    @Override
    public int getLightColor(float partialTick) {
        int base = super.getLightColor(partialTick);
        float t = (float)this.age / (float)this.lifetime;
        t = t * t * t;
        int low = base & 255;
        int high = base >> 16 & 255;
        high += (int)(t * 15.0F * 16.0F);
        if (high > 240) high = 240;
        return low | (high << 16);
    }

    @Override
    public float getQuadSize(float partialTicks) {
        float lifeRatio = (this.age + partialTicks) / this.lifetime;
        lifeRatio = 1.0F - lifeRatio;
        lifeRatio *= lifeRatio;
        lifeRatio = 1.0F - lifeRatio;
        return this.initialScale * lifeRatio;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<VacuumParticleData> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(VacuumParticleData data, ClientLevel world, double x, double y, double z,
                                     double dx, double dy, double dz) {
            return new MoCEntityFXVacuum(world, x, y, z, dx, dy, dz, data.red, data.green, data.blue, sprite);
        }
    }
}

