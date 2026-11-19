/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.compat.datafixes;

import net.minecraft.core.registries.Registries;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.MissingMappingsEvent;

public class EntityIDFixer {

    public EntityIDFixer() {
        NeoForge.EVENT_BUS.register(this);
    }

    /**
     * Called manually when loading legacy entities to remap old IDs.
     */
    public CompoundTag fixTagCompound(CompoundTag compound) {
        String entityId = compound.getStringOr("id", "");
        if (entityId.equals(MoCConstants.MOD_PREFIX + "scorpion")) {
            int entityType = compound.getIntOr("TypeInt", 0);
            switch (entityType) {
                case 2:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "cavescorpion");
                    break;
                case 3:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "firescorpion");
                    break;
                case 4:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "frostscorpion");
                    break;
                default:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "dirtscorpion");
            }
            compound.putInt("TypeInt", 1);
        }

        if (entityId.equals(MoCConstants.MOD_PREFIX + "manticore")) {
            int entityType = compound.getIntOr("TypeInt", 0);
            switch (entityType) {
                case 2:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "darkmanticore");
                    break;
                case 3:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "frostmanticore");
                    break;
                case 4:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "toxicmanticore");
                    break;
                default:
                    compound.putString("id", MoCConstants.MOD_PREFIX + "firemanticore");
            }
            compound.putInt("TypeInt", 1);
        }

        return compound;
    }

    @SuppressWarnings("removal")
    @SubscribeEvent
    public void onMissingEntityMappings(MissingMappingsEvent event) {
        Identifier scorpion = Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "scorpion");
        Identifier manticore = Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "manticore");

        event.getMappings(Registries.ENTITY_TYPE, MoCConstants.MOD_ID).forEach(mapping -> {
            if (mapping.getKey().equals(scorpion) || mapping.getKey().equals(manticore)) {
                mapping.ignore(); // Ignore missing legacy IDs
            }
        });
    }
}
