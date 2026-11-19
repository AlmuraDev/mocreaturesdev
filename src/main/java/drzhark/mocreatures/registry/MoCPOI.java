package drzhark.mocreatures.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Set;

import drzhark.mocreatures.MoCConstants;

public class MoCPOI {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, MoCConstants.MOD_ID);

    // Wyvern Portal POI used for teleportation targeting
    public static final ResourceKey<PoiType> WYVERN_PORTAL_KEY = ResourceKey.create(
            Registries.POINT_OF_INTEREST_TYPE,
            Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "wyvern_portal"));
    
    public static final DeferredHolder<PoiType, PoiType> WYVERN_PORTAL = POI_TYPES.register("wyvern_portal", 
            () -> new PoiType(
                    Set.of(Blocks.QUARTZ_BLOCK.defaultBlockState()), // Quartz block at center of portal
                    1,  // Only 1 entity can use this POI at once
                    1   // Can detect this POI from 1 block away
            ));

} 