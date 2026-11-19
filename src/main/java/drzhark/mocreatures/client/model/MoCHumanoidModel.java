/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

/**
 * Humanoid Mo' Creatures models (werehuman, wraith). 26.3's HumanoidModel animates from a player-style render state,
 * so these keep the vanilla humanoid parts and the 1.20 humanoid walk, ride, swing and crouch animation themselves.
 * The mesh is still vanilla's ({@code HumanoidModel.createMesh}); the hat is a child of the head.
 */
public abstract class MoCHumanoidModel<T extends LivingEntity> extends MoCEntityModel<T> {

    public final ModelPart head;
    public final ModelPart hat;
    public final ModelPart body;
    public final ModelPart rightArm;
    public final ModelPart leftArm;
    public final ModelPart rightLeg;
    public final ModelPart leftLeg;
    public boolean crouching;

    protected MoCHumanoidModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.hat = this.head.getChild("hat");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.crouching = entity.isCrouching();
        this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot = headPitch * Mth.DEG_TO_RAD;
        this.body.yRot = 0.0F;
        this.rightArm.z = 0.0F;
        this.rightArm.x = -5.0F;
        this.leftArm.z = 0.0F;
        this.leftArm.x = 5.0F;

        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightLeg.yRot = 0.0F;
        this.leftLeg.yRot = 0.0F;
        this.rightLeg.zRot = 0.0F;
        this.leftLeg.zRot = 0.0F;

        if (this.riding) {
            this.rightArm.xRot += -(float) Math.PI / 5F;
            this.leftArm.xRot += -(float) Math.PI / 5F;
            this.rightLeg.xRot = -1.4137167F;
            this.rightLeg.yRot = (float) Math.PI / 10F;
            this.rightLeg.zRot = 0.07853982F;
            this.leftLeg.xRot = -1.4137167F;
            this.leftLeg.yRot = -(float) Math.PI / 10F;
            this.leftLeg.zRot = -0.07853982F;
        }
        this.rightArm.yRot = 0.0F;
        this.leftArm.yRot = 0.0F;

        this.setupAttackAnimation();

        if (this.crouching) {
            this.body.xRot = 0.5F;
            this.rightArm.xRot += 0.4F;
            this.leftArm.xRot += 0.4F;
            this.rightLeg.z = 4.0F;
            this.leftLeg.z = 4.0F;
            this.rightLeg.y = 12.2F;
            this.leftLeg.y = 12.2F;
            this.head.y = 4.2F;
            this.body.y = 3.2F;
            this.leftArm.y = 5.2F;
            this.rightArm.y = 5.2F;
        } else {
            this.body.xRot = 0.0F;
            this.rightLeg.z = 0.1F;
            this.leftLeg.z = 0.1F;
            this.rightLeg.y = 12.0F;
            this.leftLeg.y = 12.0F;
            this.head.y = 0.0F;
            this.body.y = 0.0F;
            this.leftArm.y = 2.0F;
            this.rightArm.y = 2.0F;
        }

        // idle arm sway
        this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
        this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
    }

    /** The 1.20 humanoid melee swing (right arm). */
    protected void setupAttackAnimation() {
        if (this.attackTime <= 0.0F) {
            return;
        }
        float f = this.attackTime;
        this.body.yRot = Mth.sin(Mth.sqrt(f) * ((float) Math.PI * 2F)) * 0.2F;
        this.rightArm.z = Mth.sin(this.body.yRot) * 5.0F;
        this.rightArm.x = -Mth.cos(this.body.yRot) * 5.0F;
        this.leftArm.z = -Mth.sin(this.body.yRot) * 5.0F;
        this.leftArm.x = Mth.cos(this.body.yRot) * 5.0F;
        this.rightArm.yRot += this.body.yRot;
        this.leftArm.yRot += this.body.yRot;
        this.leftArm.xRot += this.body.yRot;
        f = 1.0F - this.attackTime;
        f *= f;
        f *= f;
        f = 1.0F - f;
        float f1 = Mth.sin(f * (float) Math.PI);
        float f2 = Mth.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
        this.rightArm.xRot -= f1 * 1.2F + f2;
        this.rightArm.yRot += this.body.yRot * 2.0F;
        this.rightArm.zRot += Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
    }
}
