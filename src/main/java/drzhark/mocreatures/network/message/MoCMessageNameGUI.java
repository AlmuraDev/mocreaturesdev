/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import drzhark.mocreatures.client.gui.MoCGUIEntityNamer;
import drzhark.mocreatures.entity.IMoCEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MoCMessageNameGUI {
    public int entityId;

    public MoCMessageNameGUI() {}

    public MoCMessageNameGUI(int entityId) {
        this.entityId = entityId;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    public MoCMessageNameGUI(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    public static void onMessage(MoCMessageNameGUI message, Supplier<NetworkEvent.Context> ctx) {
        // We're already on the client when we receive this packet
        if (DistExecutor.unsafeRunForDist(
                () -> () -> {
                    ctx.get().enqueueWork(() -> handleClient(message));
                    return true;
                },
                () -> () -> false)) {
            // Only executed on client side
        }
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(MoCMessageNameGUI message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null || mc.level == null) return;

        Entity ent = mc.level.getEntity(message.entityId);
        if (ent instanceof IMoCEntity) {
            IMoCEntity mocEntity = (IMoCEntity) ent;
            mc.setScreen(new MoCGUIEntityNamer(mocEntity, mocEntity.getPetName()));
        }
    }

    @Override
    public String toString() {
        return "MoCMessageNameGUI - entityId: " + this.entityId;
    }
}
