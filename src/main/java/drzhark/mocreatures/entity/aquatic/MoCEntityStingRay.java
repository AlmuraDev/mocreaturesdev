/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.aquatic;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class MoCEntityStingRay extends MoCEntityRay {

    private int poisoncounter;
    private int tailCounter;

    public MoCEntityStingRay(EntityType<? extends MoCEntityStingRay> type, Level world) {
        super(type, world);
        //setSize(0.7F, 0.3F);
        // TODO: Make hitboxes adjust depending on size
        //setAge(50 + (this.random.nextInt(40)));
        setMoCAge(90);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityRay.createAttributes()
            .add(Attributes.MAX_HEALTH, 8.0D);
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("ray_sting.png");
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.STINGRAY;
    }

    @Override
    public boolean isPoisoning() {
        return this.tailCounter != 0;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            if (!getIsTamed() && ++this.poisoncounter > 250 && (this.level().getDifficulty().getId() > 0) && this.random.nextInt(30) == 0) {
                if (MoCTools.findNearPlayerAndPoison(this, true)) {
                    MoCMessageHandler.INSTANCE.send(
                        PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
                            this.getX(), this.getY(), this.getZ(), 64, this.level().dimension()
                        )), 
                        new MoCMessageAnimation(this.getId(), 1)
                    );
                    this.poisoncounter = 0;
                }
            }
        } else { // client stuff
            if (this.tailCounter > 0 && ++this.tailCounter > 50) {
                this.tailCounter = 0;
            }
        }
    }

    @Override
    public void performAnimation(int animationType) {
        if (animationType == 1) { // attacking with tail
            this.tailCounter = 1;
        }
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (super.hurtServer(serverLevel, damagesource, i)) {
            if ((this.level().getDifficulty().getId() == 0)) {
                return true;
            }
            Entity entity = damagesource.getEntity();
            if (entity instanceof LivingEntity) {
                if (entity != this) {
                    setTarget((LivingEntity) entity);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.86F;
    }
}
