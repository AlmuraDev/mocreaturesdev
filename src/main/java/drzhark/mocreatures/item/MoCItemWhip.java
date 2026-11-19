package drzhark.mocreatures.item;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityAnimal;
import drzhark.mocreatures.entity.hunter.*;
import drzhark.mocreatures.entity.neutral.*;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;

import java.util.List;

public class MoCItemWhip extends MoCItem {

    public MoCItemWhip(Item.Properties properties) {
        super(properties.stacksTo(1).durability(24));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;

        ItemStack stack = player.getItemInHand(context.getHand());
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Block block = level.getBlockState(pos).getBlock();
        Block blockAbove = level.getBlockState(pos.above()).getBlock();

        if (context.getClickedFace() != Direction.DOWN && blockAbove == Blocks.AIR && block != Blocks.AIR && !(block instanceof StandingSignBlock)) {
            whipFX(level, pos);
            level.playSound(player, pos, MoCSoundEvents.ENTITY_GENERIC_WHIP.get(), SoundSource.PLAYERS, 0.5F, 0.4F / ((level.getRandom().nextFloat() * 0.4F) + 0.8F));
            stack.hurtAndBreak(1, player, context.getHand());

            List<Entity> entities = level.getEntities(player, player.getBoundingBox().inflate(12D));
            for (Entity entity : entities) {
                if (entity instanceof MoCEntityAnimal animal) {
                    if (MoCreatures.proxy.enableOwnership && animal.getOwnerId() != null &&
                            !player.getUUID().equals(animal.getOwnerId()) && !MoCTools.isThisPlayerAnOP(player)) {
                        continue;
                    }
                }

                if (entity instanceof MoCEntityBigCat bigCat) {
                    if (bigCat.getIsTamed()) {
                        bigCat.setSitting(!bigCat.getIsSitting());
                        bigCat.setIsJumping(false);
                        bigCat.getNavigation().stop();
                        bigCat.setTarget(null);
                    } else if (level.getDifficulty().getId() > 0 && bigCat.getIsAdult()) {
                        bigCat.setTarget(player);
                    }
                }

                if (entity instanceof MoCEntityHorse horse) {
                    if (horse.getIsTamed()) {
                        if (horse.getVehicle() == null) {
                            horse.setSitting(!horse.getIsSitting());
                            horse.setIsJumping(false);
                            horse.getNavigation().stop();
                            horse.setTarget(null);
                        } else if (horse.isNightmare()) {
                            horse.setNightmareInt(100);
                        } else if (horse.sprintCounter == 0) {
                            horse.sprintCounter = 1;
                        }
                    }
                }

                if (entity instanceof MoCEntityKitty kitty) {
                    if (kitty.getKittyState() > 2 && kitty.whipable()) {
                        kitty.setSitting(!kitty.getIsSitting());
                        kitty.setIsJumping(false);
                        kitty.getNavigation().stop();
                        kitty.setTarget(null);
                    }
                }

                if (entity instanceof MoCEntityWyvern wyvern) {
                    if (wyvern.getIsTamed() && wyvern.getVehicle() == null && !wyvern.isOnAir()) {
                        wyvern.setSitting(!wyvern.getIsSitting());
                        wyvern.setIsJumping(false);
                        wyvern.getNavigation().stop();
                        wyvern.setTarget(null);
                    }
                }

                if (entity instanceof MoCEntityPetScorpion scorpion) {
                    if (scorpion.getIsTamed() && scorpion.getVehicle() == null) {
                        scorpion.setSitting(!scorpion.getIsSitting());
                        scorpion.setIsJumping(false);
                        scorpion.getNavigation().stop();
                        scorpion.setTarget(null);
                    }
                }

                if (entity instanceof MoCEntityOstrich ostrich) {
                    if (ostrich.isVehicle() && ostrich.sprintCounter == 0) {
                        ostrich.sprintCounter = 1;
                    }
                    if (ostrich.getIsTamed() && ostrich.getVehicle() == null) {
                        ostrich.setHiding(!ostrich.getHiding());
                        ostrich.setIsJumping(false);
                        ostrich.getNavigation().stop();
                        ostrich.setTarget(null);
                    }
                }

                if (entity instanceof MoCEntityElephant elephant) {
                    if (elephant.isVehicle() && elephant.sprintCounter == 0) {
                        elephant.sprintCounter = 1;
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    private void whipFX(Level level, BlockPos pos) {
        double d = pos.getX() + 0.5F;
        double d1 = pos.getY() + 1.0F;
        double d2 = pos.getZ() + 0.5F;
        double spread = 0.27D;
        double rise = 0.22D;

        level.addParticle(ParticleTypes.FLAME, d - spread, d1 + rise, d2, 0.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.FLAME, d + spread, d1 + rise, d2, 0.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.FLAME, d, d1 + rise, d2 - spread, 0.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.FLAME, d, d1 + rise, d2 + spread, 0.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.FLAME, d, d1, d2, 0.0D, 0.0D, 0.0D);

        level.addParticle(ParticleTypes.SMOKE, d - spread, d1 + rise, d2, 0.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.SMOKE, d + spread, d1 + rise, d2, 0.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.SMOKE, d, d1 + rise, d2 - spread, 0.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.SMOKE, d, d1 + rise, d2 + spread, 0.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.SMOKE, d, d1, d2, 0.0D, 0.0D, 0.0D);
    }
}
