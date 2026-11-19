package drzhark.mocreatures.network.command;

import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.network.command.multision.CommandSpawnMoCHorse;
import drzhark.mocreatures.network.command.multision.MoCDebugSpawnCommand;
import drzhark.mocreatures.network.command.multision.ReloadSpawnConfigCommand;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.bus.api.SubscribeEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;

@EventBusSubscriber(modid = "mocreatures")
public class MoCCommandHandler {
    
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        LOGGER.info("MoCreatures registering commands");
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        try {
            // Register commands from the network/command package
            LOGGER.info("Registering MoCreatures commands");
            
            // Main MoCreatures commands
            CommandMoCreatures.register(dispatcher);
            CommandMoCPets.register(dispatcher);
            CommandMoCSpawn.register(dispatcher);
            CommandMoCTP.register(dispatcher);

            // Commands brought over from 1.16.5 and new to 1.20.1 (Mainly for debugging)
            CommandSpawnMoCHorse.register(dispatcher);
            ReloadSpawnConfigCommand.register(dispatcher);
            
            // Debug commands
            MoCDebugSpawnCommand.register(dispatcher);
        } catch (Exception e) {
            LOGGER.error("Error registering MoCreatures commands", e);
        }
    }
}