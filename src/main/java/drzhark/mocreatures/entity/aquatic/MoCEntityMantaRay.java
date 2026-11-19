/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.aquatic;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public class MoCEntityMantaRay extends MoCEntityRay {

    public MoCEntityMantaRay(EntityType<? extends MoCEntityMantaRay> type, Level world) {
        super(type, world);
        //setSize(1.4F, 0.4F);
        // TODO: Make hitboxes adjust depending on size
        //setAge(80 + (this.random.nextInt(100)));
        setMoCAge(180);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityRay.createAttributes()
            .add(Attributes.MAX_HEALTH, 20.0D);
    }

    @Override
    public int getMaxAge() {
        return 180;
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("ray_manta.png");
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.MANTA_RAY;
    }

    @Override
    public boolean isMantaRay() {
        return true;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.5875F;
    }
}
