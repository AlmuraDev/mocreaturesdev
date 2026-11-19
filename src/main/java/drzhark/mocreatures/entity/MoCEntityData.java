/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity;

import drzhark.mocreatures.MoCreatures;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;

/**
 * Updated for Minecraft 1.20.1 (Yarn mappings + Forge).
 */
public class MoCEntityData {

    /**
     * In 1.20.1, use MobSpawnSettings.SpawnerData instead of MobSpawnInfo.Spawners.
     */
    private final MobSpawnSettings.SpawnerData spawnListEntry;

    /**
     * In 1.20.1, biome types are represented by TagKey<Biome> rather than BiomeDictionary.Type.
     */
    private List<TagKey<net.minecraft.world.level.biome.Biome>> biomeTags;
    private List<TagKey<net.minecraft.world.level.biome.Biome>> blockedBiomeTags = new ArrayList<>();

    /**
     * In 1.20.1, EntityClassification is now called MobCategory.
     */
    private MobCategory typeOfCreature;

    private String entityName;
    private boolean canSpawn = true;
    private int entityId;
    private int frequency;
    private int minGroup;
    private int maxGroup;
    private int maxSpawnInChunk;

    /**
     * In 1.20.1, ResourceKey<World> became ResourceKey<Level>.
     */
    private ResourceKey<Level>[] dimensions;

    public MoCEntityData(
            String name,
            int maxChunk,
            ResourceKey<Level>[] dimensions,
            MobCategory type,
            MobSpawnSettings.SpawnerData spawnListEntry,
            int weight,
            List<TagKey<net.minecraft.world.level.biome.Biome>> biomeTags
    ) {
        this.entityName = name;
        this.typeOfCreature = type;
        this.dimensions = dimensions;
        this.biomeTags = biomeTags;
        this.frequency = weight;      // 26.3 keeps the weight beside the SpawnerData
        this.minGroup = spawnListEntry.count().minInclusive();
        this.maxGroup = spawnListEntry.count().maxInclusive();
        this.maxSpawnInChunk = maxChunk;
        this.spawnListEntry = spawnListEntry;
        MoCreatures.entityMap.put(spawnListEntry.type(), this);
    }

    public MoCEntityData(
            String name,
            int maxChunk,
            ResourceKey<Level>[] dimensions,
            MobCategory type,
            MobSpawnSettings.SpawnerData spawnListEntry,
            int weight,
            List<TagKey<net.minecraft.world.level.biome.Biome>> biomeTags,
            List<TagKey<net.minecraft.world.level.biome.Biome>> blockedBiomeTags
    ) {
        this.entityName = name;
        this.typeOfCreature = type;
        this.dimensions = dimensions;
        this.biomeTags = biomeTags;
        this.blockedBiomeTags = blockedBiomeTags;
        this.frequency = weight;
        this.minGroup = spawnListEntry.count().minInclusive();
        this.maxGroup = spawnListEntry.count().maxInclusive();
        this.maxSpawnInChunk = maxChunk;
        this.spawnListEntry = spawnListEntry;
        MoCreatures.entityMap.put(spawnListEntry.type(), this);
    }

    /** Returns the EntityType of this entry. */
    public EntityType<?> getEntityClass() {
        return this.spawnListEntry.type();
    }

    /** Returns the mob category (formerly EntityClassification). */
    public MobCategory getType() {
        return this.typeOfCreature;
    }

    public void setTypeMoC(MobCategory type) {
        this.typeOfCreature = type;
    }

    /** Returns the dimensions (as ResourceKey<Level>[]). */
    public ResourceKey<Level>[] getDimensions() {
        return this.dimensions;
    }

    public void setDimensions(ResourceKey<Level>[] dimensions) {
        this.dimensions = dimensions;
    }

    /** Returns the allowed biome tags list. */
    public List<TagKey<net.minecraft.world.level.biome.Biome>> getBiomeTags() {
        return this.biomeTags;
    }

    public void setBiomeTags(List<TagKey<net.minecraft.world.level.biome.Biome>> biomeTags) {
        this.biomeTags = biomeTags;
    }

    /** Returns the blocked biome tags list. */
    public List<TagKey<net.minecraft.world.level.biome.Biome>> getBlockedBiomeTags() {
        return this.blockedBiomeTags;
    }

    public void setBlockedBiomeTags(List<TagKey<net.minecraft.world.level.biome.Biome>> blockedBiomeTags) {
        this.blockedBiomeTags = blockedBiomeTags;
    }

    public int getEntityID() {
        return this.entityId;
    }

    public void setEntityID(int id) {
        this.entityId = id;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public void setFrequency(int freq) {
        this.frequency = Math.max(freq, 0);
    }

    public int getMinSpawn() {
        return this.minGroup;
    }

    public void setMinSpawn(int min) {
        this.minGroup = Math.max(min, 0);
    }

    public int getMaxSpawn() {
        return this.maxGroup;
    }

    public void setMaxSpawn(int max) {
        this.maxGroup = Math.max(max, 0);
    }

    public int getMaxInChunk() {
        return this.maxSpawnInChunk;
    }

    public void setMaxInChunk(int max) {
        this.maxSpawnInChunk = Math.max(max, 0);
    }

    public String getEntityName() {
        return this.entityName;
    }

    public void setEntityName(String name) {
        this.entityName = name;
    }

    public boolean getCanSpawn() {
        return this.canSpawn;
    }

    public void setCanSpawn(boolean flag) {
        this.canSpawn = flag;
    }

    /** Returns the raw MobSpawnSettings.SpawnerData entry. */
    public MobSpawnSettings.SpawnerData getSpawnListEntry() {
        return this.spawnListEntry;
    }
}
