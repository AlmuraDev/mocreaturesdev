package drzhark.mocreatures.event;

import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.client.renderer.fx.impl.MoCEntityFXStar;
import drzhark.mocreatures.client.renderer.fx.impl.MoCEntityFXUndead;
import drzhark.mocreatures.client.renderer.fx.impl.MoCEntityFXVacuum;
import drzhark.mocreatures.client.renderer.fx.impl.MoCEntityFXVanish;
import drzhark.mocreatures.client.renderer.fx.MoCParticles;
import drzhark.mocreatures.init.MoCBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SpriteSet;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = MoCConstants.MOD_ID, value = Dist.CLIENT)
public class MoCClientEvents {

    public static SpriteSet UNDEAD_SPRITE_SET, VANISH_SPRITE_SET, STAR_SPRITE_SET, VACUUM_SPRITE_SET;

    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        // Register particle providers using the new 1.20.1 API
        try {
            event.registerSpriteSet(
                MoCParticles.UNDEAD_FX.get(), 
                (spriteSet) -> {
                    UNDEAD_SPRITE_SET = spriteSet;
                    return new MoCEntityFXUndead.Factory(spriteSet);
                }
            );

            event.registerSpriteSet(
                MoCParticles.VANISH_FX.get(), 
                (spriteSet) -> {
                    VANISH_SPRITE_SET = spriteSet;
                    return new MoCEntityFXVanish.Provider(spriteSet);
                }
            );

            event.registerSpriteSet(
                MoCParticles.STAR_FX.get(), 
                (spriteSet) -> {
                    STAR_SPRITE_SET = spriteSet;
                    return new MoCEntityFXStar.Factory(spriteSet);
                }
            );

            event.registerSpriteSet(
                MoCParticles.VACUUM_FX.get(), 
                (spriteSet) -> {
                    VACUUM_SPRITE_SET = spriteSet;
                    return new MoCEntityFXVacuum.Factory(spriteSet);
                }
            );
            
            MoCreatures.LOGGER.info("Mo'Creatures particle providers registered successfully");
        } catch (Exception e) {
            MoCreatures.LOGGER.error("Error registering Mo'Creatures particle providers: " + e.getMessage(), e);
        }
    }
}