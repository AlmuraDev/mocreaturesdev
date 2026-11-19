package drzhark.mocreatures.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import net.minecraft.client.renderer.rendertype.RenderType;

public class MoCType extends RenderType {

    private static final RenderType SQUARE = create("square", 
        DefaultVertexFormat.POSITION_COLOR, 
        VertexFormat.Mode.QUADS, 
        256, 
        false, 
        true, 
        RenderType.CompositeState.builder()
            .setLayeringState(VIEW_OFFSET_Z_LAYERING)
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setOutputState(ITEM_ENTITY_TARGET)
            .setWriteMaskState(COLOR_DEPTH_WRITE)
            .createCompositeState(false)
    );

    public MoCType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, 
                   boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static RenderType getSquare() {
        return SQUARE;
    }
}
