/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import drzhark.mocreatures.entity.item.MoCEntityThrowableRock;
import drzhark.mocreatures.MoCreatures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.math.Axis;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderTRock extends EntityRenderer<MoCEntityThrowableRock> {

    public MoCRenderTRock(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(MoCEntityThrowableRock entitytrock, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLightIn) {
        poseStack.pushPose();
        BlockState state = entitytrock.getState();
        poseStack.translate(-0.5F, 0.25F, -0.5F);
        poseStack.rotate(Axis.YN.rotationDegrees(((100 - entitytrock.acceleration) / 10F) * 36F));
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        
        // Render the block state
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
            state, 
            poseStack, 
            buffer, 
            packedLightIn,
            OverlayTexture.NO_OVERLAY
        );
        
        poseStack.popPose();
    }

    @Override
    public Identifier getTextureLocation(MoCEntityThrowableRock par1Entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
