/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;

/**
 * The pet name and health bar drawn above tamed creatures. The 1.20 renderers each drew these by hand with vertex
 * calls; in 26.3 they are submitted as text and text-background quads.
 */
public final class MoCPetLabel {

    private static final float SCALE = 0.01666667F * 1.6F;

    private MoCPetLabel() {
    }

    /**
     * @param lift         how far above the entity's origin the label starts, in blocks
     * @param yOffset      the label's offset in text units (negative is up)
     * @param faceYawOnly  turn only around the vertical axis towards the camera (most creatures) instead of facing it fully
     */
    public static void submit(PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera, Font font, int light,
                              LivingEntity entity, String name, boolean showName, boolean showHealth,
                              float lift, int yOffset, boolean faceYawOnly) {
        if (!showName && !showHealth) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.0F, lift, 0.0F);
        if (faceYawOnly) {
            poseStack.rotate(Axis.YP.rotationDegrees(-camera.yRot));
        } else {
            poseStack.rotate(camera.orientation);
        }
        poseStack.scale(-SCALE, -SCALE, SCALE);

        int yOff = yOffset;
        if (showHealth) {
            if (!showName) {
                yOff += 8;
            }
            float barWidth = 40F * (entity.getHealth() / entity.getMaxHealth());
            // red for the missing health, then green on top for what is left
            collector.submitTextBackground(poseStack, -20F + barWidth, -10 + yOff, 20F, -6 + yOff, 0xFFB30000, Font.DisplayMode.NORMAL, light);
            collector.submitTextBackground(poseStack, -20F, -10 + yOff, barWidth - 20F, -6 + yOff, 0xFF00B300, Font.DisplayMode.NORMAL, light);
        }
        if (showName) {
            FormattedCharSequence text = Component.literal(name).getVisualOrderText();
            float textX = -font.width(text) / 2.0F;
            collector.submitText(poseStack, textX, yOff, text, false, Font.DisplayMode.SEE_THROUGH, light, 0x20FFFFFF, 0x40000000, 0);
            collector.submitText(poseStack, textX, yOff, text, false, Font.DisplayMode.NORMAL, light, -1, 0, 0);
        }
        poseStack.popPose();
    }
}
