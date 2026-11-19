/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.command;
import net.minecraft.server.permissions.Permissions;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import drzhark.mocreatures.MoCreatures;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class CommandMoCreatures {

    private static final List<String> commands = new ArrayList<>();

    static {
        commands.add("/moc attackhorses <boolean>");
        commands.add("/moc attackwolves <boolean>");
        commands.add("/moc debug <boolean>");
        commands.add("/moc destroydrops <boolean>");
        commands.add("/moc enablehunters <boolean>");
        commands.add("/moc easybreeding <boolean>");
        commands.add("/moc elephantbulldozer <boolean>");
        commands.add("/moc enableownership <boolean>");
        commands.add("/moc enableresetownerscroll <boolean>");
        commands.add("/moc golemdestroyblocks <boolean>");
        commands.add("/moc ogreattackrange <int>");
        commands.add("/moc ogrestrength <float>");
        commands.add("/moc ostricheggdropchance <int>");
        commands.add("/moc rareitemdropchance <int>");
        commands.add("/moc maxtamedperop <int>");
        commands.add("/moc maxtamedperplayer <int>");
        commands.add("/moc motherwyverneggdropchance <int>");
        commands.add("/moc wyverneggdropchance <int>");
        commands.add("/moc help");
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // Root command
        var mocCommand = Commands.literal("moc")
            .requires(MoCCommandPermissions.require("moc"));
            
        // Boolean settings
        for (String setting : List.of("attackhorses", "attackwolves", 
                "debug", "destroydrops", "enablehunters", "easybreeding", "elephantbulldozer", 
                "enableownership", "enableresetownerscroll", "golemdestroyblocks")) {
            mocCommand.then(Commands.literal(setting)
                .then(Commands.argument("value", BoolArgumentType.bool())
                    .executes(context -> {
                        boolean value = BoolArgumentType.getBool(context, "value");
                        return setBooleanSetting(context.getSource(), setting, value);
                    })
                )
            );
        }
        
        // Float settings
        mocCommand.then(Commands.literal("ogrestrength")
            .then(Commands.argument("value", FloatArgumentType.floatArg(0))
                .executes(context -> {
                    float value = FloatArgumentType.getFloat(context, "value");
                    return setFloatSetting(context.getSource(), "ogrestrength", value);
                })
            )
        );
        
        // Integer settings
        for (String setting : List.of("ogreattackrange", 
                "ostricheggdropchance", "rareitemdropchance", "maxtamedperop", "maxtamedperplayer",
                "motherwyverneggdropchance", "wyverneggdropchance")) {
            mocCommand.then(Commands.literal(setting)
                .then(Commands.argument("value", IntegerArgumentType.integer(0))
                    .executes(context -> {
                        int value = IntegerArgumentType.getInteger(context, "value");
                        return setIntSetting(context.getSource(), setting, value);
                    })
                )
            );
        }
        
        // Help command
        mocCommand.then(Commands.literal("help")
            .executes(context -> {
                return showHelp(context.getSource());
            })
        );
        
        // Register the command with all subcommands
        dispatcher.register(mocCommand);
    }
    
    private static int showHelp(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("MoCreatures Commands:"), false);
        for (String command : commands) {
            source.sendSuccess(() -> Component.literal(command), false);
        }
        return 1;
    }
    
    private static int setBooleanSetting(CommandSourceStack source, String setting, boolean value) {
        try {
            boolean result = false;
            
            switch (setting) {
                case "debug":
                    MoCreatures.proxy.debug = value;
                    result = true;
                    break;
                case "attackhorses":
                    MoCreatures.proxy.attackHorses = value;
                    result = true;
                    break;
                case "attackwolves":
                    MoCreatures.proxy.attackWolves = value;
                    result = true;
                    break;
                case "destroydrops":
                    MoCreatures.proxy.destroyDrops = value;
                    result = true;
                    break;
                case "enablehunters":
                    MoCreatures.proxy.enableHunters = value;
                    result = true;
                    break;
                case "easybreeding":
                    MoCreatures.proxy.easyHorseBreeding = value;
                    result = true;
                    break;
                case "elephantbulldozer":
                    MoCreatures.proxy.elephantBulldozer = value;
                    result = true;
                    break;
                case "enableownership":
                    MoCreatures.proxy.enableOwnership = value;
                    result = true;
                    break;
                case "enableresetownerscroll":
                    MoCreatures.proxy.enableResetOwnership = value;
                    result = true;
                    break;
                case "golemdestroyblocks":
                    MoCreatures.proxy.golemDestroyBlocks = value;
                    result = true;
                    break;
                default:
                    source.sendFailure(Component.literal("Unknown boolean setting: " + setting));
                    return 0;
            }
            
            if (result) {
                // Save the configuration after changing it
                MoCreatures.proxy.mocSettingsConfig.save();
                source.sendSuccess(() -> Component.literal("Successfully set " + setting + " to " + value), true);
                return 1;
            } else {
                source.sendFailure(Component.literal("Failed to set " + setting));
                return 0;
            }
        } catch (Exception e) {
            source.sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }
    
    private static int setFloatSetting(CommandSourceStack source, String setting, float value) {
        try {
            boolean result = false;
            
            switch (setting) {
                case "ogrestrength":
                    MoCreatures.proxy.ogreStrength = value;
                    result = true;
                    break;
                default:
                    source.sendFailure(Component.literal("Unknown float setting: " + setting));
                    return 0;
            }
            
            if (result) {
                // Save the configuration after changing it
                MoCreatures.proxy.mocSettingsConfig.save();
                source.sendSuccess(() -> Component.literal("Successfully set " + setting + " to " + value), true);
                return 1;
            } else {
                source.sendFailure(Component.literal("Failed to set " + setting));
                return 0;
            }
        } catch (Exception e) {
            source.sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }
    
    private static int setIntSetting(CommandSourceStack source, String setting, int value) {
        try {
            boolean result = false;
            
            switch (setting) {
                case "ogreattackrange":
                    MoCreatures.proxy.ogreAttackRange = (short) value;
                    result = true;
                    break;
                case "ostricheggdropchance":
                    MoCreatures.proxy.ostrichEggDropChance = value;
                    result = true;
                    break;
                case "rareitemdropchance":
                    MoCreatures.proxy.rareItemDropChance = value;
                    result = true;
                    break;
                case "maxtamedperop":
                    MoCreatures.proxy.maxOPTamed = value;
                    result = true;
                    break;
                case "maxtamedperplayer":
                    MoCreatures.proxy.maxTamed = value;
                    result = true;
                    break;
                case "motherwyverneggdropchance":
                    MoCreatures.proxy.motherWyvernEggDropChance = value;
                    result = true;
                    break;
                case "wyverneggdropchance":
                    MoCreatures.proxy.wyvernEggDropChance = value;
                    result = true;
                    break;
                default:
                    source.sendFailure(Component.literal("Unknown integer setting: " + setting));
                    return 0;
            }
            
            if (result) {
                // Save the configuration after changing it
                MoCreatures.proxy.mocSettingsConfig.save();
                source.sendSuccess(() -> Component.literal("Successfully set " + setting + " to " + value), true);
                return 1;
            } else {
                source.sendFailure(Component.literal("Failed to set " + setting));
                return 0;
            }
        } catch (Exception e) {
            source.sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }
}
