/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client;

import drzhark.mocreatures.client.gui.MoCGUIEntityNamer;
import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.entity.hostile.MoCEntityGolem;
import drzhark.mocreatures.entity.hostile.MoCEntityOgre;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import drzhark.mocreatures.network.message.MoCMessageAppear;
import drzhark.mocreatures.network.message.MoCMessageAttachedEntity;
import drzhark.mocreatures.network.message.MoCMessageExplode;
import drzhark.mocreatures.network.message.MoCMessageHealth;
import drzhark.mocreatures.network.message.MoCMessageHeart;
import drzhark.mocreatures.network.message.MoCMessageNameGUI;
import drzhark.mocreatures.network.message.MoCMessageShuffle;
import drzhark.mocreatures.network.message.MoCMessageTwoBytes;
import drzhark.mocreatures.network.message.MoCMessageVanish;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Client-only: what the client does with each client-bound Mo' Creatures packet. The message classes only hand over to
 * these methods, so a dedicated server (which loads the message classes to send them) never sees a client class.
 */
public final class MoCClientPacketHandlers {

    private MoCClientPacketHandlers() {
    }

    public static void handleAnimation(MoCMessageAnimation message) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                Entity entity = minecraft.level.getEntity(message.entityId);
                if (entity instanceof IMoCEntity) {
                    ((IMoCEntity) entity).performAnimation(message.animationType);
                }
            }
        }

    public static void handleAppear(MoCMessageAppear message) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                Entity entity = minecraft.level.getEntity(message.entityId);
                if (entity instanceof MoCEntityHorse) {
                    ((MoCEntityHorse) entity).MaterializeFX();
                }
            }
        }

    public static void handleAttachedEntity(MoCMessageAttachedEntity message) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                Entity sourceEntity = minecraft.level.getEntity(message.sourceEntityId);
                Entity targetEntity = minecraft.level.getEntity(message.targetEntityId);

                if (sourceEntity != null && targetEntity != null) {
                    sourceEntity.startRiding(targetEntity);
                }
            }
        }

    public static void handleExplode(MoCMessageExplode message) {
            Entity ent = Minecraft.getInstance().player.level().getEntity(message.entityId);
            if (ent instanceof MoCEntityOgre) {
                ((MoCEntityOgre) ent).performDestroyBlastAttack();
            }
        }

    public static void handleHealth(MoCMessageHealth message) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                Entity entity = minecraft.level.getEntity(message.entityId);
                if (entity instanceof Mob mob) {
                    mob.setHealth(message.health);
                }
            }
        }

    public static void handleHeart(MoCMessageHeart message) {
            Entity ent = Minecraft.getInstance().player.level().getEntity(message.entityId);
            if (ent instanceof IMoCTameable) {
                ((IMoCTameable) ent).spawnHeart();
            }
        }

    public static void handleShuffle(MoCMessageShuffle message) {
            Entity ent = Minecraft.getInstance().player.level().getEntity(message.entityId);
            if (ent instanceof MoCEntityHorse) {
                if (message.flag) {
                    //((MoCEntityHorse) ent).shuffle();
                } else {
                    ((MoCEntityHorse) ent).shuffleCounter = 0;
                }
            }
        }

    public static void handleTwoBytes(MoCMessageTwoBytes message) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                Entity ent = minecraft.level.getEntity(message.entityId);
                if (ent instanceof MoCEntityGolem) {
                    ((MoCEntityGolem) ent).saveGolemCube(message.slot, message.value);
                }
            }
        }

    public static void handleVanish(MoCMessageVanish message) {
            Entity ent = Minecraft.getInstance().player.level().getEntity(message.entityId);
            if (ent instanceof MoCEntityHorse) {
                ((MoCEntityHorse) ent).setVanishC((byte) 1);
            }
        }

    public static void handleNameGUI(MoCMessageNameGUI message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null || mc.level == null) return;

        Entity ent = mc.level.getEntity(message.entityId);
        if (ent instanceof IMoCEntity) {
            IMoCEntity mocEntity = (IMoCEntity) ent;
            mc.gui.setScreen(new MoCGUIEntityNamer(mocEntity, mocEntity.getPetName()));
        }
    }
}
