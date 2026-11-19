/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import drzhark.mocreatures.entity.tameable.MoCPetData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandMoCPets {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("mocpets")
            .requires(source -> source.hasPermission(2))
            .executes(context -> execute(context.getSource()))
            .then(Commands.argument("page", IntegerArgumentType.integer(1))
                .executes(context -> {
                    int page = IntegerArgumentType.getInteger(context, "page");
                    return execute(context.getSource(), page);
                })
            )
        );
    }

    private static int execute(CommandSourceStack source) {
        return execute(source, 1);
    }

    private static int execute(CommandSourceStack source, int page) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            int unloadedCount = 0;
            int loadedCount = 0;
            ArrayList<Integer> foundIds = new ArrayList<>();
            ArrayList<String> tamedList = new ArrayList<>();
            UUID playerId = player.getUUID();
            
            // Search for tamed entities in all dimensions
            for (ServerLevel world : source.getServer().getAllLevels()) {
                for (Entity entity : world.getAllEntities()) {
                    if (entity instanceof IMoCTameable mocreature) {
                        if (mocreature.getOwnerId() != null && mocreature.getOwnerId().equals(playerId)) {
                            loadedCount++;
                            foundIds.add(mocreature.getOwnerPetId());
                            tamedList.add("Found pet with Type: " + ((Mob) mocreature).getName().getString() + 
                                    ", Name: " + mocreature.getPetName() + 
                                    ", Owner: " + player.getName().getString() + 
                                    ", PetId: " + mocreature.getOwnerPetId() + 
                                    ", Dimension: " + entity.level().dimension().identifier() + 
                                    ", Pos: " + Math.round(entity.getX()) + 
                                    ", " + Math.round(entity.getY()) + 
                                    ", " + Math.round(entity.getZ()));
                        }
                    }
                }
            }
            
            // Search for unloaded tamed entities
            MoCPetData ownerPetData = MoCreatures.instance.mapData.getPetData(player.getUUID());
            if (ownerPetData != null) {
                for (int i = 0; i < ownerPetData.getTamedList().size(); i++) {
                    CompoundTag nbt = ownerPetData.getTamedList().getCompoundOrEmpty(i);
                    if (nbt.contains("PetId") && !foundIds.contains(nbt.getIntOr("PetId", 0))) {
                        unloadedCount++;
                        double posX = nbt.getListOrEmpty("Pos").getDouble(0);
                        double posY = nbt.getListOrEmpty("Pos").getDouble(1);
                        double posZ = nbt.getListOrEmpty("Pos").getDouble(2);
                        
                        if (nbt.getBooleanOr("InAmulet", false)) {
                            tamedList.add("Found unloaded pet in AMULET with Type: " + 
                                    nbt.getStringOr("id", "").replace("MoCreatures.", "") + 
                                    ", Name: " + nbt.getStringOr("Name", "") + 
                                    ", Owner: " + nbt.getStringOr("Owner", "") + 
                                    ", PetId: " + nbt.getIntOr("PetId", 0));
                        } else {
                            tamedList.add("Found unloaded pet with Type: " + 
                                    nbt.getStringOr("id", "").replace("MoCreatures.", "") + 
                                    ", Name: " + nbt.getStringOr("Name", "") + 
                                    ", Owner: " + nbt.getStringOr("Owner", "") + 
                                    ", PetId: " + nbt.getIntOr("PetId", 0) + 
                                    ", Dimension: " + nbt.getStringOr("Dimension", "") + 
                                    ", Pos: " + Math.round(posX) + 
                                    ", " + Math.round(posY) + 
                                    ", " + Math.round(posZ));
                        }
                    }
                }
            }
            
            final int finalLoadedCount = loadedCount;
            final int finalUnloadedCount = unloadedCount;
            final int finalTotalCount = ownerPetData != null ? ownerPetData.getTamedList().size() : 0;
            
            if (tamedList.size() > 0) {
                sendPageHelp(source, (byte) 10, tamedList, page);
                source.sendSuccess(() -> Component.literal("Loaded tamed count: " + finalLoadedCount + 
                        ", Unloaded count: " + finalUnloadedCount + 
                        ", Total count: " + finalTotalCount), 
                        false);
            } else {
                source.sendSuccess(() -> Component.literal("Loaded tamed count: " + finalLoadedCount + 
                        ", Unloaded Count: " + finalUnloadedCount + 
                        ", Total count: " + (finalLoadedCount + finalUnloadedCount)),
                        false);
            }
            
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("Error: " + e.getMessage()));
            e.printStackTrace();
            return 0;
        }
    }
    
    private static void sendPageHelp(CommandSourceStack sender, byte pageLimit, ArrayList<String> list, int page) {
        int totalPages = (list.size() - 1) / pageLimit;
        final int finalPage = Math.max(0, Math.min(page - 1, totalPages));
        
        int start = finalPage * pageLimit;
        int end = Math.min((finalPage + 1) * pageLimit, list.size());
        
        sender.sendSuccess(() -> Component.literal("--- Showing MoCreatures Help Info " + 
                (finalPage + 1) + " of " + (totalPages + 1) + " (/mocpets <page>) ---"), 
                false);
        
        for (int i = start; i < end; i++) {
            String tamedInfo = list.get(i);
            sender.sendSuccess(() -> Component.literal(tamedInfo), false);
        }
    }
}
