/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.entity.hunter.MoCEntityManticorePet;
import drzhark.mocreatures.entity.neutral.MoCEntityWyvern;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCEntities;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAppear;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.server.command.EnumArgument;

public class CommandMoCSpawn {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("mocspawn").requires((source) -> {
            return source.hasPermission(2);
        }).then(Commands.argument("entityType", EnumArgument.enumArgument(Type.class)).executes((context) -> {
            return execute(context.getSource(), context.getArgument("entityType", Type.class), 0);
        }).then(Commands.argument("type", IntegerArgumentType.integer(1)).executes((context) -> {
            return execute(context.getSource(), context.getArgument("entityType", Type.class), IntegerArgumentType.getInteger(context, "type"));
        }))));
    }

    private static int execute(CommandSourceStack source, Type entityType, int type) {
        try {
            ServerPlayer player = source.getPlayer();
            Level level = player.level();
            MoCEntityTameableAnimal specialEntity = null;
            
            switch (entityType) {
                case horse:
                    specialEntity = new MoCEntityHorse(MoCEntities.WILDHORSE.get(), level);
                    specialEntity.setAdult(true);
                    break;
                case manticore:
                    specialEntity = new MoCEntityManticorePet(MoCEntities.MANTICORE_PET.get(), level);
                    specialEntity.setAdult(true);
                    break;
                case wyvern:
                    specialEntity = new MoCEntityWyvern(MoCEntities.WYVERN.get(), level);
                    specialEntity.setAdult(false);
                    break;
                case wyvernghost:
                    specialEntity = new MoCEntityWyvern(MoCEntities.WYVERN.get(), level);
                    specialEntity.setAdult(false);
                    ((MoCEntityWyvern) specialEntity).setIsGhost(true);
                    break;
                default:
                    source.sendFailure(Component.literal("ERROR: The entity spawn type " + entityType + " is not a valid type."));
                    return 1;
            }
            
            double dist = 3D;
            double newPosY = player.getY();
            double newPosX = player.getX() - (dist * Math.cos((MoCTools.realAngle(player.getYRot() - 90F)) / 57.29578F));
            double newPosZ = player.getZ() - (dist * Math.sin((MoCTools.realAngle(player.getYRot() - 90F)) / 57.29578F));
            specialEntity.setPos(newPosX, newPosY, newPosZ);
            specialEntity.setTamed(true);
            specialEntity.setOwnerId(null);
            specialEntity.setPetName("Rename_Me");
            specialEntity.setTypeMoC(type);

            if ((entityType == Type.horse && (type < 0 || type > 67))
                    || (entityType == Type.wyvern && (type < 0 || type > 12))
                    || (entityType == Type.manticore && (type < 0 || type > 4))) {
                source.sendFailure(Component.literal("ERROR: The spawn type " + type + " is not a valid type."));
                return 1;
            }
            level.addFreshEntity(specialEntity);
            if (!level.isClientSide()) {
                MoCMessageHandler.sendToNear(level, player.getX(), player.getY(), player.getZ(), 64, new MoCMessageAppear(specialEntity.getId()));
            }
            MoCTools.playCustomSound(specialEntity, MoCSoundEvents.ENTITY_GENERIC_MAGIC_APPEAR.get());
        } catch (Exception e){}
        return 1;
    }

    private enum Type{
        horse,
        manticore,
        wyvern,
        wyvernghost
    }
}
