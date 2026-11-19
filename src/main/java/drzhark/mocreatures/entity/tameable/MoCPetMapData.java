package drzhark.mocreatures.entity.tameable;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.saveddata.SavedDataType;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCreatures;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MoCPetMapData extends SavedData {

    /** How 26.3 stores the pet map: one compound per owner UUID, as before. */
    public static final SavedDataType<MoCPetMapData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "pets"),
            () -> new MoCPetMapData(MoCConstants.MOD_ID),
            CompoundTag.CODEC.xmap(MoCPetMapData::load, data -> data.save(new CompoundTag())));

    private final Object2ObjectOpenHashMap<UUID, MoCPetData> petMap = new Object2ObjectOpenHashMap<>();
    private final String name;
    // Counter to track changes and force saves periodically even if not marked dirty
    private final AtomicInteger changeCounter = new AtomicInteger(0);
    private static final int FORCE_SAVE_THRESHOLD = 10;

    public MoCPetMapData(String name) {
        this.name = name;
        setDirty();
    }

    // Static load method for DimensionDataStorage
    public static MoCPetMapData load(CompoundTag tag) {
        MoCPetMapData data = new MoCPetMapData(MoCConstants.MOD_ID);
        for (String key : tag.keySet()) {
            try {
                UUID ownerUUID = UUID.fromString(key);
                CompoundTag nbt = tag.getCompoundOrEmpty(key);
                if (!data.petMap.containsKey(ownerUUID)) {
                    data.petMap.put(ownerUUID, new MoCPetData(nbt, ownerUUID));
                    MoCreatures.LOGGER.debug("Loaded pet data for owner: {}", ownerUUID);
                }
            } catch (Exception e) {
                MoCreatures.LOGGER.error("Error loading pet data for key {}: {}", key, e.getMessage());
            }
        }
        MoCreatures.LOGGER.info("Loaded MoCPetMapData with {} pet owners", data.petMap.size());
        return data;
    }

    public MoCPetData getPetData(UUID ownerUniqueId) {
        return this.petMap.get(ownerUniqueId);
    }

    public Object2ObjectOpenHashMap<UUID, MoCPetData> getPetMap() {
        return this.petMap;
    }

    public void removeOwnerPet(IMoCTameable pet, int petId) {
        UUID owner = pet.getOwnerId();
        MoCPetData data = this.petMap.get(owner);
        if (data != null) {
            boolean success = data.removePet(petId);
            if (success) {
                setDirty();
                pet.setOwnerPetId(-1);
                MoCreatures.LOGGER.debug("Successfully removed pet ID {} for owner {}", petId, owner);
                incrementChangeCounter();
                
                // If this was the last pet, remove the owner entry
                if (data.getTamedList().isEmpty()) {
                    petMap.remove(owner);
                    MoCreatures.LOGGER.debug("Removed last pet for owner {}, removing owner entry", owner);
                }
            } else {
                MoCreatures.LOGGER.error("Could not remove petId {} for owner {}", petId, owner);
            }
        } else {
            MoCreatures.LOGGER.error("No pet data found for owner {} when trying to remove pet ID {}", owner, petId);
        }
    }

    public void updateOwnerPet(IMoCTameable pet) {
        if (pet.getOwnerPetId() == -1 || this.petMap.get(pet.getOwnerId()) == null) {
            UUID owner = MoCreatures.isServer(((Entity) pet).level()) ? pet.getOwnerId() : MoCreatures.proxy.getPlayer().getUUID();
            MoCPetData petData;
            int id;
            if (this.petMap.containsKey(owner)) {
                petData = this.petMap.get(owner);
                id = petData.addPet(pet);
                MoCreatures.LOGGER.debug("Added new pet with ID {} for existing owner {}", id, owner);
            } else {
                petData = new MoCPetData(pet);
                id = petData.addPet(pet);
                this.petMap.put(owner, petData);
                MoCreatures.LOGGER.debug("Created new pet data for owner {} with first pet ID {}", owner, id);
            }
            pet.setOwnerPetId(id);
        } else {
            UUID owner = pet.getOwnerId();
            MoCPetData petData = this.getPetData(owner);
            CompoundTag rootNBT = petData.getOwnerRootNBT();
            ListTag tag = rootNBT.getListOrEmpty("TamedList");
            int id = pet.getOwnerPetId();

            for (int i = 0; i < tag.size(); i++) {
                CompoundTag nbt = tag.getCompoundOrEmpty(i);
                if (nbt.getIntOr("PetId", 0) == id) {
                    nbt.put("Pos", newDoubleNBTList(((Entity) pet).getX(), ((Entity) pet).getY(), ((Entity) pet).getZ()));
                    nbt.putInt("ChunkX", ((Entity) pet).chunkPosition().x());
                    nbt.putInt("ChunkY", ((Entity) pet).chunkPosition().getWorldPosition().getY());
                    nbt.putInt("ChunkZ", ((Entity) pet).chunkPosition().z());
                    nbt.putString("Dimension", ((Entity) pet).level().dimension().identifier().toString());
                    nbt.putInt("PetId", pet.getOwnerPetId());
                    MoCreatures.LOGGER.debug("Updated position data for pet ID {} owned by {}", id, owner);
                    break;
                }
            }
        }
        setDirty(); // Always mark dirty after updating
        incrementChangeCounter();
    }

    private void incrementChangeCounter() {
        // Force a save after every FORCE_SAVE_THRESHOLD changes, regardless of dirty state
        if (changeCounter.incrementAndGet() >= FORCE_SAVE_THRESHOLD) {
            MoCreatures.LOGGER.debug("Reached {} changes, forcing save", changeCounter.get());
            setDirty();
            
            // Try to get the server level to force a save
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null && server.overworld() != null) {
                server.overworld().getDataStorage().scheduleSave();
                MoCreatures.LOGGER.debug("Forced save of pet data after {} changes", FORCE_SAVE_THRESHOLD);
            }
            
            // Reset counter
            changeCounter.set(0);
        }
    }

    protected ListTag newDoubleNBTList(double... values) {
        ListTag list = new ListTag();
        for (double val : values) {
            list.add(DoubleTag.valueOf(val));
        }
        return list;
    }

    public boolean isExistingPet(UUID owner, IMoCTameable pet) {
        MoCPetData petData = MoCreatures.instance.mapData.getPetData(owner);
        if (petData != null) {
            ListTag tag = petData.getTamedList();
            for (int i = 0; i < tag.size(); i++) {
                CompoundTag nbt = tag.getCompoundOrEmpty(i);
                if (nbt.getIntOr("PetId", 0) == pet.getOwnerPetId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public CompoundTag save(CompoundTag tag) {
        int count = 0;
        for (Map.Entry<UUID, MoCPetData> entry : this.petMap.entrySet()) {
            try {
                if (entry.getKey() != null) {
                    tag.put(entry.getKey().toString(), entry.getValue().getOwnerRootNBT());
                    count++;
                }
            } catch (Exception e) {
                MoCreatures.LOGGER.error("Error saving pet data for owner {}: {}", entry.getKey(), e.getMessage());
            }
        }
        MoCreatures.LOGGER.info("Saved MoCPetMapData with {} pet owners", count);
        return tag;
    }
    
    // Force data to be considered dirty even when no changes seem to have occurred
    public void forceDirty() {
        setDirty();
    }
}
