/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import drzhark.mocreatures.entity.tameable.MoCPetData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Supplier;

public class MoCMessageUpdatePetName implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MoCMessageUpdatePetName> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "update_pet_name"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageUpdatePetName> STREAM_CODEC = StreamCodec.ofMember(MoCMessageUpdatePetName::encode, MoCMessageUpdatePetName::new);

    @Override
    public CustomPacketPayload.Type<MoCMessageUpdatePetName> type() {
        return TYPE;
    }


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

    public static void onMessage(MoCMessageUpdatePetName message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            // This is a server-side message handler
            ServerPlayer sender = ((ServerPlayer) ctx.player());
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
                        ListTag tag = petData.getOwnerRootNBT().getListOrEmpty("TamedList");
                        for (int i = 0; i < tag.size(); i++) {
                            CompoundTag nbt = tag.getCompoundOrEmpty(i);
                            if (nbt.getIntOr("PetId", 0) == id) {
                                nbt.putString("Name", message.name);
                                ((IMoCTameable) pet).setPetName(message.name);
                            }
                        }
                    }
                }
            }
        });
            }

    @Override
    public String toString() {
        return String.format("MoCMessageUpdatePetName - entityId:%s, name:%s", this.entityId, this.name);
    }
}
