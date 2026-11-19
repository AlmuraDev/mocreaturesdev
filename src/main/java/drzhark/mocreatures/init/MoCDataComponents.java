/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.init;

import com.mojang.serialization.Codec;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Item data components; 26.3 items carry their extra data as components instead of an NBT tag. */
public final class MoCDataComponents {

    public static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MoCConstants.MOD_ID);

    /** Which creature hatches from a Mo' Creatures egg (the 1.20 "EggType" tag). */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> EGG_TYPE =
            COMPONENTS.registerComponentType("egg_type", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    private MoCDataComponents() {
    }
}
