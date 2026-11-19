package drzhark.mocreatures.network.command.multision;

import drzhark.mocreatures.network.command.MoCCommandPermissions;import net.minecraft.server.permissions.Permissions;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import drzhark.mocreatures.init.MoCEntities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class CommandSpawnMoCHorse {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("mocsummon")
                .requires(MoCCommandPermissions.require("mocsummon"))
                .then(Commands.literal("horse")
                        .then(Commands.argument("type", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    return SharedSuggestionProvider.suggest(MoCHorseType.getNames(), builder);
                                })
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayer();
                                    String typeName = StringArgumentType.getString(ctx, "type");
                                    return summonHorse(player, typeName, false);
                                })
                                .then(Commands.literal("tame")
                                        .executes(ctx -> {
                                            ServerPlayer player = ctx.getSource().getPlayer();
                                            String typeName = StringArgumentType.getString(ctx, "type");
                                            return summonHorse(player, typeName, true);
                                        })
                                )
                        )
                )
        );
    }

    private static int summonHorse(ServerPlayer player, String typeName, boolean tame) {
        Level level = player.level();
        BlockPos pos = player.blockPosition();
        Optional<MoCHorseType> horseType = MoCHorseType.fromName(typeName);

        if (horseType.isEmpty()) {
            player.sendSystemMessage(Component.literal("Invalid horse type: " + typeName));
            return 0;
        }

        MoCEntityHorse horse = new MoCEntityHorse(MoCEntities.WILDHORSE.get(), level);
        horse.setPos(pos.getX(), pos.getY(), pos.getZ());
        horse.setTypeMoC(horseType.get().getId());

        if (tame) {
            MoCTools.tameWithName(player, horse);
        }

        level.addFreshEntity(horse);
        player.sendSystemMessage(Component.literal("Spawned MoC Horse of type " + typeName + (tame ? " (Tamed)" : "")));
        return 1;
    }
}

