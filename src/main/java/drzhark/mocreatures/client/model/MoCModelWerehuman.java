/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import drzhark.mocreatures.entity.hostile.MoCEntityWerewolf;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCModelWerehuman<T extends MoCEntityWerewolf> extends MoCHumanoidModel<T> {
    
    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath("mocreatures", "werehuman"), "main");

    public MoCModelWerehuman(ModelPart root) {
        super(root);
    }
    
    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(
                HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F),
                64,
                32
        );
    }
}
