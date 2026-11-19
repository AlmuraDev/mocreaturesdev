/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import drzhark.mocreatures.entity.tameable.MoCPetData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Supplier;

public class MoCMessageUpdatePetName {

    public String name;
    public int entityId;

    public MoCMessageUpdatePetName(int entityId, String name) {
        this.entityId = entityId;
        this.name = name;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(name.length());
        buffer.writeCharSequence(this.name, StandardCharsets.UTF_8);
        buffer.writeInt(this.entityId);
    }

    public MoCMessageUpdatePetName(FriendlyByteBuf buffer) {
        this.name = buffer.readCharSequence(buffer.readInt(), StandardCharsets.UTF_8).toString();
        this.entityId = buffer.readInt();
    }

    public static void onMessage(MoCMessageUpdatePetName message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // This is a server-side message handler
            ServerPlayer sender = ctx.get().getSender();
            if (sender != null && sender.level() != null) {
                Entity pet = null;
                UUID ownerUniqueId = null;

                Entity entity = sender.level().getEntity(message.entityId);
                if (entity != null && entity.getId() == message.entityId && entity instanceof IMoCTameable) {
                    ((IMoCEntity) entity).setPetName(message.name);
                    ownerUniqueId = ((IMoCEntity) entity).getOwnerId();
                    pet = entity;
                }

                // update petdata
                if (ownerUniqueId != null) {
                    MoCPetData petData = MoCreatures.instance.mapData.getPetData(ownerUniqueId);
                    if (petData != null && pet != null && ((IMoCTameable) pet).getOwnerPetId() != -1) {
                        int id = ((IMoCTameable) pet).getOwnerPetId();
                        ListTag tag = petData.getOwnerRootNBT().getList("TamedList", 10);
                        for (int i = 0; i < tag.size(); i++) {
                            CompoundTag nbt = tag.getCompound(i);
                            if (nbt.getIntOr("PetId", 0) == id) {
                                nbt.putString("Name", message.name);
                                ((IMoCTameable) pet).setPetName(message.name);
                            }
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public String toString() {
        return String.format("MoCMessageUpdatePetName - entityId:%s, name:%s", this.entityId, this.name);
    }
}
