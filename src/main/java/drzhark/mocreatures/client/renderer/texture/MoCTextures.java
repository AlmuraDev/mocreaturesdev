/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.texture;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.proxy.MoCProxy;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.Identifier;

public class MoCTextures {

    private static final Object2ObjectOpenHashMap<String, Identifier> RESOURCE_CACHE = new Object2ObjectOpenHashMap<>();

    public Identifier getArmorTexture(String texture) {
        return getTexture(MoCProxy.ARMOR_TEXTURE, texture);
    }

    public Identifier getBlockTexture(String texture) {
        return getTexture(MoCProxy.BLOCK_TEXTURE, texture);
    }

    public Identifier getItemTexture(String texture) {
        return getTexture(MoCProxy.ITEM_TEXTURE, texture);
    }

    public Identifier getModelTexture(String texture) {
        return getTexture(MoCProxy.MODEL_TEXTURE, texture);
    }

    public Identifier getGuiTexture(String texture) {
        return getTexture(MoCProxy.GUI_TEXTURE, texture);
    }

    public Identifier getMiscTexture(String texture) {
        return getTexture(MoCProxy.MISC_TEXTURE, texture);
    }

    @SuppressWarnings("removal")
    public Identifier getTexture(String category, String texture) {
        Identifier resourceLocation = RESOURCE_CACHE.get(texture);
        if (resourceLocation == null) {
            resourceLocation = Identifier.parse(MoCConstants.MOD_PREFIX + category + texture);
            RESOURCE_CACHE.put(texture, resourceLocation);
        }
        return resourceLocation;
    }
}
