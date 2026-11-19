/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.compat.datafixes;

import net.minecraft.core.registries.Registries;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import net.minecraft.server.jsonrpc.api.Schema;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.MissingMappingsEvent;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("removal")
public class BlockIDFixer extends DataFix {
    private static final Map<Identifier, Identifier> BLOCK_NAME_MAPPINGS = new HashMap<>();

    static {
        BLOCK_NAME_MAPPINGS.put(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "mocstone"), Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "wyvstone"));
        BLOCK_NAME_MAPPINGS.put(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "mocgrass"), Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "wyvgrass"));
        BLOCK_NAME_MAPPINGS.put(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "mocdirt"), Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "wyvdirt"));
        BLOCK_NAME_MAPPINGS.put(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "mocleaves"), Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "wyvwood_leaves"));
        BLOCK_NAME_MAPPINGS.put(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "moclog"), Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "wyvwood_log"));
        BLOCK_NAME_MAPPINGS.put(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "moctallgrass"), Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "tall_wyvgrass"));
        BLOCK_NAME_MAPPINGS.put(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "mocwoodplank"), Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "wyvwood_planks"));
    }

    public BlockIDFixer(Schema outputSchema, boolean changesType) {
        super(outputSchema, changesType);
        NeoForge.EVENT_BUS.register(this);
    }

    @Override
    public TypeRewriteRule makeRule() {
        return TypeRewriteRule.nop();
    }

    @SubscribeEvent
    public void onMissingBlockMappings(MissingMappingsEvent event) {
        event.getMappings(Registries.BLOCK, MoCConstants.MOD_ID).forEach(mapping -> {
            Identifier oldName = mapping.getKey();
            Identifier newName = BLOCK_NAME_MAPPINGS.get(oldName);
            if (newName != null) {
                Block newBlock = BuiltInRegistries.BLOCK.getValue(newName);
                if (newBlock != null) {
                    mapping.remap(newBlock);
                }
            }
        });
    }

    @SubscribeEvent
    public void onMissingItemMappings(MissingMappingsEvent event) {
        event.getMappings(Registries.ITEM, MoCConstants.MOD_ID).forEach(mapping -> {
            Identifier oldName = mapping.getKey();
            Identifier newName = BLOCK_NAME_MAPPINGS.get(oldName);
            if (newName != null) {
                Item newItem = BuiltInRegistries.ITEM.getValue(newName);
                if (newItem != null) {
                    mapping.remap(newItem);
                }
            }
        });
    }
}
