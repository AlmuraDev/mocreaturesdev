package drzhark.mocreatures;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;

import drzhark.mocreatures.client.renderer.fx.MoCParticles;
import drzhark.mocreatures.compat.CompatHandler;
import drzhark.mocreatures.config.biome.BiomeSpawnConfig;
import drzhark.mocreatures.entity.MoCEntityData;
import drzhark.mocreatures.entity.tameable.MoCPetMapData;
import drzhark.mocreatures.event.MoCEventHooks;
import drzhark.mocreatures.init.MoCBlocks;
import drzhark.mocreatures.init.MoCCreativeTabs;
import drzhark.mocreatures.init.MoCEntities;
import drzhark.mocreatures.init.MoCFeatures;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.init.MoCSpawnEggs;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.proxy.MoCProxy;
import drzhark.mocreatures.proxy.MoCProxyClient;
import drzhark.mocreatures.registry.MoCPOI;
import drzhark.mocreatures.world.MoCSpawnBiomeModifier;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import drzhark.mocreatures.event.MoCWyvernDimensionHandler;

import java.util.UUID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MoCConstants.MOD_ID)
public class MoCreatures {

    public static final Logger LOGGER = LogManager.getLogger(MoCConstants.MOD_ID);
    private static boolean debug = false;

    public static final String MOC_LOGO = ChatFormatting.WHITE + "[" + ChatFormatting.AQUA + MoCConstants.MOD_NAME + ChatFormatting.WHITE + "]";
    public static MoCreatures instance;
    public static MoCProxy proxy;
    public static GameProfile MOCFAKEPLAYER = new GameProfile(UUID.fromString("6E379B45-1111-2222-3333-2FE1A88BCD66"), "[MoCreatures]");
    // Wyvern dimension key
    //public static ResourceKey<Level> wyvernDimension = WyvernLairWorldDimension.WYVERNLAIR_WORLD_KEY;
    public static Object2ObjectLinkedOpenHashMap<String, MoCEntityData> mocEntityMap = new Object2ObjectLinkedOpenHashMap<>();
    public static Object2ObjectOpenHashMap<EntityType<?>, MoCEntityData> entityMap = new Object2ObjectOpenHashMap<>();
    public static Int2ObjectOpenHashMap<Class<? extends Mob>> instaSpawnerMap = new Int2ObjectOpenHashMap<>();
    public MoCPetMapData mapData;

    @SuppressWarnings("removal")
    public MoCreatures() {
        instance = this;

        // Register for config events
        this.proxy = DistExecutor.unsafeRunForDist(() -> MoCProxyClient::new, () -> MoCProxy::new);
        MoCMessageHandler.init();
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        NeoForge.EVENT_BUS.register(new MoCEventHooks());

        // Register the Wyvern dimension handler
        NeoForge.EVENT_BUS.register(MoCWyvernDimensionHandler.class);
        
        CompatHandler.preInit();

        proxy.configInit();
        proxy.registerRenderers();
        proxy.registerRenderInformation();

        CompatHandler.init();
        registerDeferredRegistries(eventBus);

        // Don't init BiomeConfig here - wait for config event
        
        final DeferredRegister<Codec<? extends BiomeModifier>> biomeModifiers = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MoCConstants.MOD_ID);
        biomeModifiers.register(eventBus);
        biomeModifiers.register("moc_spawns", MoCSpawnBiomeModifier::makeCodec);
    }
    
    private void setup(final FMLCommonSetupEvent event) {
        // This is called after registry events - safe to build spawn lists
        event.enqueueWork(() -> {
            LOGGER.info("Building Mo'Creatures world gen spawn lists");
            
            // Initialize entity maps now that registries are available
            LOGGER.info("Initializing Mo'Creatures entity maps...");
            proxy.initializeMocEntityMap();
            //proxy.readMocConfigValues();
            LOGGER.info("Entity maps initialized with {} entities", entityMap.size());
            
            // Initialize BiomeConfig now that configs are loaded
            LOGGER.info("Initializing BiomeConfig during setup...");
            BiomeSpawnConfig.init();
            LOGGER.info("BiomeConfig initialized during setup");
            
            // Enable debug mode for spawn testing
            debug = proxy.debug;
            LOGGER.info("Mo'Creatures debug mode: {}", debug);
            
            // Initialize the POI registry
            MoCPOI.init();
        });
    }

    public static boolean isServer(Level world) {
        return !world.isClientSide();
    }
    
    /**
     * Check if debug mode is enabled
     */
    public static boolean isDebug() {
        return debug;
    }
    
    /**
     * Set debug mode
     */
    public static void setDebug(boolean debug) {
        MoCreatures.debug = debug;
        if (debug) {
            LOGGER.info("Mo'Creatures debug mode enabled");
        }
    }

    public static void registerDeferredRegistries(IEventBus modBus) {
        MoCSoundEvents.SOUND_DEFERRED.register(modBus);
        MoCParticles.PARTICLES.register(modBus);
        MoCBlocks.BLOCKS.register(modBus);
        MoCBlocks.ITEMS.register(modBus);
        MoCEntities.ENTITY_TYPES.register(modBus);
        MoCSpawnEggs.SPAWN_EGGS.register(modBus);
        MoCItems.ITEMS.register(modBus);
        MoCFeatures.FEATURES.register(modBus);
        MoCCreativeTabs.register(modBus);
        MoCPOI.POI_TYPES.register(modBus);
    }
}
