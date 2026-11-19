package drzhark.mocreatures.entity.tameable;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.UUID;

public class MoCPetData {

    private final UUID ownerUniqueId;
    private final BitSet idMap = new BitSet(Long.SIZE << 4);
    private final ArrayList<Integer> usedPetIds = new ArrayList<>();
    private CompoundTag ownerData = new CompoundTag();
    private ListTag tamedList = new ListTag();

    public MoCPetData(IMoCTameable pet) {
        this.ownerData.put("TamedList", this.tamedList);
        Level level = ((Entity) pet).level();
        this.ownerUniqueId = MoCreatures.isServer(level) ? pet.getOwnerId() : MoCreatures.proxy.getPlayer().getUUID();
        MoCreatures.LOGGER.debug("Created new MoCPetData for owner {}", this.ownerUniqueId);
    }

    public MoCPetData(CompoundTag nbt, UUID owner) {
        this.ownerData = nbt;
        this.tamedList = nbt.getListOrEmpty("TamedList");
        this.ownerUniqueId = owner;
        this.loadPetDataMap(nbt.getCompoundOrEmpty("PetIdData"));
        MoCreatures.LOGGER.debug("Loaded MoCPetData for owner {} with {} pets", 
                this.ownerUniqueId, this.tamedList.size());
    }

    public int addPet(IMoCTameable pet) {
        Entity entity = (Entity) pet;
        ChunkPos chunkCoord = entity.chunkPosition();
        BlockPos coords = new BlockPos(chunkCoord.x(), chunkCoord.getWorldPosition().getY(), chunkCoord.z());
        CompoundTag petNBT = MoCTools.getEntityData(entity);

        if (this.tamedList != null) {
            int id = getNextFreePetId();
            petNBT.putInt("PetId", id);
            CompoundTag petData = petNBT.copy();
            petData.putInt("ChunkX", coords.getX());
            petData.putInt("ChunkY", coords.getY());
            petData.putInt("ChunkZ", coords.getZ());
            petData.putString("Dimension", entity.level().dimension().identifier().toString());
            this.tamedList.add(petData);
            this.ownerData.put("PetIdData", savePetDataMap());
            MoCreatures.LOGGER.debug("Added pet ID {} for owner {}", id, this.ownerUniqueId);
            return id;
        } else {
            MoCreatures.LOGGER.error("Cannot add pet - tamedList is null for owner {}", this.ownerUniqueId);
            return -1;
        }
    }

    public boolean removePet(int id) {
        for (int i = this.tamedList.size() - 1; i >= 0; i--) {
            CompoundTag nbt = this.tamedList.getCompoundOrEmpty(i);
            if (nbt.contains("PetId") && nbt.getIntOr("PetId", 0) == id) {
                if (i >= 0 && i < this.tamedList.size()) {
                    this.tamedList.remove(i);
                } else {
                    MoCreatures.LOGGER.error("Invalid tamedList index: {} for PetId: {}", i, id);
                    return false;
                }
                this.usedPetIds.remove(Integer.valueOf(id));
                this.idMap.clear(id);
                if (this.usedPetIds.isEmpty()) {
                    this.idMap.clear();
                }
                this.ownerData.put("PetIdData", savePetDataMap());
                MoCreatures.LOGGER.debug("Removed pet ID {} for owner {}", id, this.ownerUniqueId);
                return true;
            }
        }
        MoCreatures.LOGGER.error("Failed to remove petId {} - not found in tamedList for owner {}", 
                id, this.ownerUniqueId);
        return false;
    }

    public CompoundTag getPetData(int id) {
        if (this.tamedList != null) {
            for (int i = 0; i < this.tamedList.size(); i++) {
                CompoundTag nbt = this.tamedList.getCompoundOrEmpty(i);
                if (nbt.contains("PetId") && nbt.getIntOr("PetId", 0) == id) {
                    return nbt;
                }
            }
        }
        return null;
    }

    public CompoundTag getOwnerRootNBT() {
        return this.ownerData;
    }

    public ListTag getTamedList() {
        return this.tamedList;
    }

    public String getOwner() {
        return this.ownerData != null ? this.ownerData.getStringOr("Owner", "") : null;
    }

    public boolean getInAmulet(int petId) {
        CompoundTag petData = getPetData(petId);
        return petData != null && petData.getBooleanOr("InAmulet", false);
    }

    public void setInAmulet(int petId, boolean flag) {
        CompoundTag petData = getPetData(petId);
        if (petData != null) {
            petData.putBoolean("InAmulet", flag);
        }
    }

    public int getNextFreePetId() {
        int next = 0;
        while (true) {
            next = this.idMap.nextClearBit(next);
            if (this.usedPetIds.contains(next)) {
                this.idMap.set(next);
            } else {
                this.usedPetIds.add(next);
                return next;
            }
        }
    }

    public CompoundTag savePetDataMap() {
        int[] data = new int[(this.idMap.length() + Integer.SIZE - 1) / Integer.SIZE];
        CompoundTag dataMap = new CompoundTag();
        for (int i = 0; i < data.length; i++) {
            int val = 0;
            for (int j = 0; j < Integer.SIZE; j++) {
                val |= this.idMap.get(i * Integer.SIZE + j) ? (1 << j) : 0;
            }
            data[i] = val;
        }
        dataMap.putIntArray("PetIdArray", data);
        return dataMap;
    }

    public void loadPetDataMap(CompoundTag compoundTag) {
        if (compoundTag == null) {
            this.idMap.clear();
            MoCreatures.LOGGER.debug("Reset pet ID map for owner {} (null compound tag)", this.ownerUniqueId);
        } else {
            int[] intArray = compoundTag.getIntArray("PetIdArray").orElse(new int[0]);
            for (int i = 0; i < intArray.length; i++) {
                for (int j = 0; j < Integer.SIZE; j++) {
                    this.idMap.set(i * Integer.SIZE + j, (intArray[i] & (1 << j)) != 0);
                }
            }
            int next = 0;
            int idsAdded = 0;
            while (true) {
                next = this.idMap.nextSetBit(next);
                if (next == -1) break;
                
                if (!this.usedPetIds.contains(next)) {
                    this.usedPetIds.add(next);
                    idsAdded++;
                }
                next++;
            }
            MoCreatures.LOGGER.debug("Loaded pet ID map for owner {} with {} IDs", 
                    this.ownerUniqueId, idsAdded);
        }
    }
    
    public UUID getOwnerUniqueId() {
        return this.ownerUniqueId;
    }
}
