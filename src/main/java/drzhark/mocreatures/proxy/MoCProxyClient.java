/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.proxy;

import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.client.gui.MoCGUIEntityNamer;
import drzhark.mocreatures.client.renderer.fx.MoCParticles;
import drzhark.mocreatures.client.renderer.fx.data.StarParticleData;
import drzhark.mocreatures.client.renderer.fx.data.VacuumParticleData;
import drzhark.mocreatures.client.renderer.fx.data.VanishParticleData;
import drzhark.mocreatures.client.renderer.fx.impl.MoCEntityFXUndead;
import drzhark.mocreatures.client.renderer.fx.impl.MoCEntityFXVanish;
import drzhark.mocreatures.client.renderer.texture.MoCTextures;
import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.entity.hostile.MoCEntityGolem;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.bus.api.SubscribeEvent;

/**
 * Client-side proxy for Mo' Creatures
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = "mocreatures", value = Dist.CLIENT)
public class MoCProxyClient extends MoCProxy {

    public static Minecraft mc = Minecraft.getInstance();
    public static MoCProxyClient instance;
    public static MoCTextures mocTextures = new MoCTextures();

    public MoCProxyClient() {
        instance = this;
    }

    @Override
    public void registerRenderers() {
        super.registerRenderers();
    }

    @Override
    public Identifier getArmorTexture(String texture) {
        return mocTextures.getArmorTexture(texture);
    }

    @Override
    public Identifier getBlockTexture(String texture) {
        return mocTextures.getBlockTexture(texture);
    }

    @Override
    public Identifier getItemTexture(String texture) {
        return mocTextures.getItemTexture(texture);
    }

    @Override
    public Identifier getModelTexture(String texture) {
        return mocTextures.getModelTexture(texture);
    }

    @Override
    public Identifier getGuiTexture(String texture) {
        return mocTextures.getGuiTexture(texture);
    }

    @Override
    public Identifier getMiscTexture(String texture) {
        return mocTextures.getMiscTexture(texture);
    }

    /**
     * This method is called during initialization
     * In 1.20.1, entity renderers are registered via events
     */
    @Override
    public void registerRenderInformation() {
        // Entity renderers are now registered via events in MoCModelRegistry and MoCRendererRegistry
        // This method is maintained for compatibility but doesn't need to do anything since
        // renderer registration is now event-based
        
        MoCreatures.LOGGER.info("MoCreatures client setup initialized");
    }

    /**
     * This method is kept for backward compatibility
     * In 1.20.1, entity renderers are registered via events
     */
    public void registerEntityRenderers() {
        // This method is kept for backward compatibility
        // Actual entity renderer registration happens in MoCRendererRegistry.registerEntityRenderers
    }

    @Override
    public Player getPlayer() {
        return MoCProxyClient.mc.player;
    }

    @Override
    public void openBook(net.minecraft.world.item.ItemStack book) {
        net.minecraft.client.gui.screens.inventory.BookViewScreen.BookAccess access =
                net.minecraft.client.gui.screens.inventory.BookViewScreen.BookAccess.fromItem(book);
        if (access != null) {
            mc.gui.setScreen(new net.minecraft.client.gui.screens.inventory.BookViewScreen(access));
        }
    }

    /**
     * Sets the name client side. Name is synchronized with data watchers
     */
    @Override
    public void setName(Player player, IMoCEntity mocanimal) {
        mc.gui.setScreen(new MoCGUIEntityNamer(mocanimal, mocanimal.getPetName()));
    }

    @Override
    public void UndeadFX(Entity entity) {
        int densityInt = (MoCreatures.proxy.getParticleFX());
        if (densityInt == 0 || !(entity.level() instanceof ClientLevel)) {
            return;
        }

        int i = (densityInt / 2) * (entity.level().getRandom().nextInt(2) + 1);
        if (i == 0) {
            i = 1;
        }
        if (i > 10) {
            i = 10;
        }

        ClientLevel level = (ClientLevel) entity.level();
        for (int x = 0; x < i; x++) {
            // Create the undead particle directly in ClientLevel
            level.addParticle(
                MoCParticles.UNDEAD_FX.get(),
                entity.getX(),
                entity.getY() + entity.getBbHeight() * entity.level().getRandom().nextFloat(),
                entity.getZ(),
                0, 0, 0
            );
        }
    }

    @Override
    public void StarFX(MoCEntityHorse entity) {
        int densityInt = MoCreatures.proxy.getParticleFX();
        if (densityInt == 0 || !(entity.level() instanceof ClientLevel)) return;

        ClientLevel level = (ClientLevel) entity.level();
        
        // Get colors from horse entity
        float r = entity.colorFX(1, entity.getTypeMoC());
        float g = entity.colorFX(2, entity.getTypeMoC());
        float b = entity.colorFX(3, entity.getTypeMoC());

        int count = densityInt * (level.getRandom().nextInt(2) + 1);
        for (int i = 0; i < count; ++i) {
            double x = entity.getX();
            double y = entity.getY() + level.getRandom().nextFloat() * entity.getBbHeight();
            double z = entity.getZ();

            level.addParticle(
                    new StarParticleData(r, g, b),
                    x, y, z,
                    0, 0, 0
            );
        }
    }

    @Override
    public void LavaFX(Entity entity) {
        int densityInt = (MoCreatures.proxy.getParticleFX());
        if (densityInt == 0) {
            return;
        }
        double var2 = entity.level().getRandom().nextGaussian() * 0.02D;
        double var4 = entity.level().getRandom().nextGaussian() * 0.02D;
        double var6 = entity.level().getRandom().nextGaussian() * 0.02D;
        mc.level.addParticle(ParticleTypes.LAVA, 
            entity.getX() + entity.level().getRandom().nextFloat() * entity.getBbWidth() - entity.getBbWidth(), 
            entity.getY() + 0.5D + entity.level().getRandom().nextFloat() * entity.getBbHeight(), 
            entity.getZ() + entity.level().getRandom().nextFloat() * entity.getBbWidth() - entity.getBbWidth(), 
            var2, var4, var6);
    }

    @Override
    public void VanishFX(MoCEntityHorse entity) {
        int densityInt = MoCreatures.proxy.getParticleFX();
        if (densityInt == 0 || !(entity.level() instanceof ClientLevel)) return;

        ClientLevel level = (ClientLevel) entity.level();

        // Get colors from horse entity
        float r = entity.colorFX(1, entity.getTypeMoC());
        float g = entity.colorFX(2, entity.getTypeMoC());
        float b = entity.colorFX(3, entity.getTypeMoC());

        for (int i = 0; i < densityInt * 8; ++i) {
            double newPosX = entity.getX() + level.getRandom().nextFloat();
            double newPosY = entity.getY() + 0.7D + level.getRandom().nextFloat();
            double newPosZ = entity.getZ() + level.getRandom().nextFloat();

            int sign = level.getRandom().nextInt(2) * 2 - 1;
            double speedX = level.getRandom().nextFloat() * 2.0F * sign;
            double speedY = (level.getRandom().nextFloat() - 0.5D) * 0.5D;
            double speedZ = level.getRandom().nextFloat() * 2.0F * sign;

            level.addParticle(
                    new VanishParticleData(r, g, b, false), // Using horse's custom RGB
                    newPosX, newPosY, newPosZ,
                    speedX, speedY, speedZ
            );
        }
    }

    @Override
    public void MaterializeFX(MoCEntityHorse entity) {
        int densityInt = MoCreatures.proxy.getParticleFX();
        if (densityInt == 0 || !(entity.level() instanceof ClientLevel)) return;

        ClientLevel level = (ClientLevel) entity.level();

        // Get colors from horse entity
        float r = entity.colorFX(1, entity.getTypeMoC());
        float g = entity.colorFX(2, entity.getTypeMoC());
        float b = entity.colorFX(3, entity.getTypeMoC());

        for (int i = 0; i < (densityInt * 50); ++i) {
            double newPosX = entity.getX() + level.getRandom().nextFloat();
            double newPosY = entity.getY() + 0.7D + level.getRandom().nextFloat();
            double newPosZ = entity.getZ() + level.getRandom().nextFloat();

            int sign = level.getRandom().nextInt(2) * 2 - 1;
            double speedX = level.getRandom().nextFloat() * 2.0F * sign;
            double speedY = (level.getRandom().nextFloat() - 0.5D) * 0.5D;
            double speedZ = level.getRandom().nextFloat() * 2.0F * sign;

            level.addParticle(
                    new VanishParticleData(r, g, b, false),
                    newPosX, newPosY, newPosZ,
                    speedX, speedY, speedZ
            );
        }
    }

    @Override
    public void VacuumFX(MoCEntityGolem entity) {
        int densityInt = MoCreatures.proxy.getParticleFX();
        if (densityInt == 0 || !(entity.level() instanceof ClientLevel)) return;

        ClientLevel level = (ClientLevel) entity.level();

        // Get colors from golem entity
        float r = entity.colorFX(1);
        float g = entity.colorFX(2);
        float b = entity.colorFX(3);

        for (int i = 0; i < 2; ++i) {
            double yawRad = Math.toRadians(entity.getYRot() - 90F);
            double newPosX = entity.getX() - (1.5 * Math.cos(yawRad));
            double newPosZ = entity.getZ() - (1.5 * Math.sin(yawRad));
            double newPosY = entity.getY() + (entity.getBbHeight() - 0.8D - entity.getAdjustedYOffset() * 1.8D);

            double speedX = (level.getRandom().nextDouble() - 0.5D) * 4.0D;
            double speedY = -level.getRandom().nextDouble();
            double speedZ = (level.getRandom().nextDouble() - 0.5D) * 4.0D;

            level.addParticle(new VacuumParticleData(r, g, b), newPosX, newPosY, newPosZ, speedX, speedY, speedZ);
        }
    }

    @Override
    public void hammerFX(Player entity) {
        int densityInt = (MoCreatures.proxy.getParticleFX());
        if (densityInt == 0 || !(entity.level() instanceof ClientLevel)) return;

        ClientLevel level = (ClientLevel) entity.level();
        
        for (int var6 = 0; var6 < (densityInt * 10); ++var6) {
            double newPosX = ((float) entity.getX() + level.getRandom().nextFloat());
            double newPosY = 0.3D + ((float) entity.getY() + level.getRandom().nextFloat());
            double newPosZ = ((float) entity.getZ() + level.getRandom().nextFloat());
            int var19 = level.getRandom().nextInt(2) * 2 - 1;
            double speedY = (level.getRandom().nextFloat() - 0.5D) * 0.5D;
            double speedX = level.getRandom().nextFloat() * 2.0F * var19;
            double speedZ = level.getRandom().nextFloat() * 2.0F * var19;

            // Use a spell particle instead of custom particle for now
            level.addParticle(
                ParticleTypes.ENCHANT,
                newPosX, newPosY, newPosZ,
                speedX, speedY, speedZ
            );
        }
    }

    @Override
    public void teleportFX(Player entity) {
        int densityInt = (MoCreatures.proxy.getParticleFX());
        if (densityInt == 0 || !(entity.level() instanceof ClientLevel)) return;

        ClientLevel level = (ClientLevel) entity.level();
        
        for (int var6 = 0; var6 < (densityInt * 50); ++var6) {
            double newPosX = ((float) entity.getX() + level.getRandom().nextFloat());
            double newPosY = 0.7D + ((float) entity.getY() + level.getRandom().nextFloat());
            double newPosZ = ((float) entity.getZ() + level.getRandom().nextFloat());
            int var19 = level.getRandom().nextInt(2) * 2 - 1;
            double speedY = (level.getRandom().nextFloat() - 0.5D) * 0.5D;
            double speedX = level.getRandom().nextFloat() * 2.0F * var19;
            double speedZ = level.getRandom().nextFloat() * 2.0F * var19;

            // Use VanishParticleData
            level.addParticle(
                new VanishParticleData(1.0F, 1.0F, 1.0F, false),
                newPosX, newPosY, newPosZ,
                speedX, speedY, speedZ
            );
        }
    }

    @Override
    public int getProxyMode() {
        return 2;
    }

    @Override
    public void resetAllData() {
        super.resetAllData();
    }

    @Override
    public int getParticleFX() {
        return this.particleFX;
    }

    @Override
    public boolean getDisplayPetName() {
        return this.displayPetName;
    }

    @Override
    public boolean getDisplayPetIcons() {
        return this.displayPetIcons;
    }

    @Override
    public boolean getDisplayPetHealth() {
        return this.displayPetHealth;
    }

    @Override
    public boolean getAnimateTextures() {
        return this.animateTextures;
    }

    @Override
    public void printMessageToPlayer(String msg) {
        if (mc.player != null) {
            Component chatText = Component.literal(msg);
            mc.player.sendSystemMessage(chatText);
        }
    }
}
