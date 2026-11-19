/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import drzhark.mocreatures.entity.tameable.MoCPetData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.ITeleporter;

import java.util.function.Function;

@SuppressWarnings("removal")
public class CommandMoCTP {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("moctp")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("petId", IntegerArgumentType.integer(0))
                .executes(context -> {
                    int petId = IntegerArgumentType.getInteger(context, "petId");
                    return teleportPet(context.getSource(), petId, context.getSource().getPlayerOrException());
                })
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(context -> {
                        int petId = IntegerArgumentType.getInteger(context, "petId");
                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                        return teleportPet(context.getSource(), petId, player);
                    })
                )
            )
        );
    }
    
    private static int teleportPet(CommandSourceStack source, int petId, ServerPlayer player) {
        // Search for tamed entity in mocreatures.dat
        MoCPetData ownerPetData = MoCreatures.instance.mapData.getPetData(player.getUUID());
        if (ownerPetData != null) {
            for (int i = 0; i < ownerPetData.getTamedList().size(); i++) {
                CompoundTag nbt = ownerPetData.getTamedList().getCompoundOrEmpty(i);
                if (nbt.contains("PetId") && nbt.getIntOr("PetId", 0) == petId) {
                    String petName = nbt.getStringOr("Name", "");
                    ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, 
                            Identifier.parse(nbt.getStringOr("Dimension", "")));
                    ServerLevel world = source.getServer().getLevel(dimensionKey);
                    
                    if (world != null) {
                        if (!teleportLoadedPet(world, player, petId, petName, source)) {
                            double posX = nbt.getListOrEmpty("Pos").getDouble(0);
                            double posY = nbt.getListOrEmpty("Pos").getDouble(1);
                            double posZ = nbt.getListOrEmpty("Pos").getDouble(2);
                            int x = (int) Math.floor(posX);
                            int y = (int) Math.floor(posY);
                            int z = (int) Math.floor(posZ);
                            
                            source.sendSuccess(() -> Component.literal("Found unloaded pet " + 
                                    nbt.getStringOr("id", "") + " with name " + 
                                    petName + " at location " + x + ", " + y + ", " + z + 
                                    " with Pet ID " + nbt.getIntOr("PetId", 0)), true);
                                    
                            // Attempt to TP again
                            boolean result = teleportLoadedPet(world, player, petId, petName, source);
                            if (!result) {
                                source.sendFailure(Component.literal("Unable to transfer entity ID " + 
                                        petId + ". It may only be transferred to " + player.getName().getString()));
                            }
                        }
                        return 1;
                    }
                }
            }
        }
        source.sendFailure(Component.literal("Tamed entity could not be located."));
        return 0;
    }

    private static boolean teleportLoadedPet(ServerLevel world, ServerPlayer player, int petId, String petName, CommandSourceStack source) {
        for (Entity entity : world.getAllEntities()) {
            // Search for entities that are IMoCTameable
            if (entity instanceof IMoCTameable tameable && !tameable.getPetName().isEmpty() && tameable.getOwnerPetId() == petId) {
                // Grab the entity data
                CompoundTag compound = new CompoundTag();
                entity.save(compound);
                
                if (!compound.isEmpty() && !compound.getStringOr("Owner", "").isEmpty()) {
                    String owner = compound.getStringOr("Owner", "");
                    String name = compound.getStringOr("Name", "");
                    
                    if (!owner.isEmpty() && owner.equalsIgnoreCase(player.getName().getString())) {
                        // Check if in same dimension
                        if (entity.level().dimension() == player.level().dimension()) {
                            entity.setPos(player.getX(), player.getY(), player.getZ());
                        } else if (!player.level().isClientSide()) {
                            // Cross-dimension teleportation using changeDimension
                            ServerLevel targetLevel = player.serverLevel();
                            entity.changeDimension(targetLevel, new PetTeleporter(
                                player.getX(), player.getY(), player.getZ()));
                        }
                        
                        source.sendSuccess(() -> Component.literal(name + 
                                " has been teleported to location " + Math.round(player.getX()) + 
                                ", " + Math.round(player.getY()) + ", " + Math.round(player.getZ()) + 
                                " in dimension " + player.level().dimension().identifier()), true);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Simple teleporter implementation for cross-dimension pet teleportation
     */
    private static class PetTeleporter implements ITeleporter {
        private final double x, y, z;
        
        public PetTeleporter(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        @Override
        public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, 
                                  float yaw, Function<Boolean, Entity> repositionEntity) {
            Entity newEntity = repositionEntity.apply(false);
            if (newEntity != null) {
                newEntity.setPos(x, y, z);
                newEntity.setYRot(yaw);
            }
            return newEntity;
        }
    }
}
