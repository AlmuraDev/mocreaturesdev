/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.EntitySpawnReason;

import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.entity.MoCEntityAnimal;
import drzhark.mocreatures.entity.ambient.MoCEntityMaggot;
import drzhark.mocreatures.entity.hostile.MoCEntityOgre;
import drzhark.mocreatures.entity.hostile.MoCEntitySilverSkeleton;
import drzhark.mocreatures.entity.inventory.MoCAnimalChest;
import drzhark.mocreatures.entity.item.MoCEntityThrowableRock;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCEntities;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageNameGUI;
import drzhark.mocreatures.util.MoCTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MoCTools {

    /**
     * spawns tiny slimes
     */
    public static void spawnSlimes(Level level, Entity entity) {
        if (!level.isClientSide()) {
            RandomSource random = level.getRandom();
            int count = 1 + random.nextInt(1); // 0–1 slimes

            for (int i = 0; i < count; ++i) {
                float offsetX = (i % 2 - 0.5F) * 0.25F;
                float offsetZ = ((float) i / 2 - 0.5F) * 0.25F;

                Slime slime = EntityType.SLIME.create(level);
                if (slime == null) continue;

                slime.snapTo(
                        entity.getX() + offsetX,
                        entity.getY() + 0.5D,
                        entity.getZ() + offsetZ,
                        random.nextFloat() * 360.0F,
                        0.0F
                );

                level.addFreshEntity(slime);
            }
        }
    }

    /**
     * Drops saddle
     */
    public static void dropSaddle(MoCEntityAnimal entity, Level level) {
        if (!entity.getIsRideable() || level.isClientSide()) {
            return;
        }

        dropCustomItem(entity, level, new ItemStack(MoCItems.HORSE_SADDLE.get(), 1));
        entity.setRideable(false);
    }

    /**
     * Drops item
     */
    public static void dropCustomItem(Entity entity, Level level, ItemStack itemstack) {
        if (level.isClientSide()) {
            return;
        }

        ItemEntity entityItem = new ItemEntity(
                level,
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                itemstack
        );

        float f = 0.05F;
        RandomSource random = level.getRandom();
        entityItem.setDeltaMovement(
                random.nextGaussian() * f,
                (random.nextGaussian() * f) + 0.2F,
                random.nextGaussian() * f
        );

        level.addFreshEntity(entityItem);
    }

    public static void bigSmack(Entity entity, Entity target, float force) {
        double dx = entity.getX() - target.getX();
        double dz;

        RandomSource random = entity.level().getRandom();

        // Ensure there's always some non-zero push direction
        for (dz = entity.getZ() - target.getZ(); (dx * dx + dz * dz) < 0.0001D; dz = (random.nextDouble() - random.nextDouble()) * 0.01D) {
            dx = (random.nextDouble() - random.nextDouble()) * 0.01D;
        }

        float dist = Mth.sqrt((float) (dx * dx + dz * dz));
        Vec3 motion = target.getDeltaMovement().scale(0.5D)
                .subtract((dx / dist) * force, -force, (dz / dist) * force);

        target.setDeltaMovement(motion);

        if (target.getDeltaMovement().y() > force) {
            target.setDeltaMovement(target.getDeltaMovement().x(), force, target.getDeltaMovement().z());
        }
    }


    public static void buckleMobs(Mob entityAttacker, double dist, Level level) {
        AABB area = entityAttacker.getBoundingBox().inflate(dist, 2D, dist);
        List<Entity> targets = level.getEntities(entityAttacker, area);

        for (Entity entityTarget : targets) {
            if (!(entityTarget instanceof Mob) || (entityAttacker.isPassenger() && entityTarget == entityAttacker.getVehicle())) {
                continue;
            }

            DamageSource source = new DamageSource(
                    level.registryAccess()
                            .registryOrThrow(Registries.DAMAGE_TYPE)
                            .getHolderOrThrow(DamageTypes.MOB_ATTACK),
                    entityAttacker
            );


            entityTarget.hurtOrSimulate(source, 2.0F);
            bigSmack(entityAttacker, entityTarget, 0.6F);
            playCustomSound(entityAttacker, MoCSoundEvents.ENTITY_GENERIC_TUD.get());
        }
    }


    public static void buckleMobsNotPlayers(Mob attacker, double dist, Level level) {
        AABB area = attacker.getBoundingBox().inflate(dist, 2D, dist);
        List<Entity> targets = level.getEntities(attacker, area);

        for (Entity target : targets) {
            if (!(target instanceof Mob) || (attacker.isPassenger() && target == attacker.getVehicle())) {
                continue;
            }

            DamageSource source = new DamageSource(
                    level.registryAccess()
                            .registryOrThrow(Registries.DAMAGE_TYPE)
                            .getHolderOrThrow(DamageTypes.MOB_ATTACK),
                    attacker
            );

            target.hurtOrSimulate(source, 2.0F);
            bigSmack(attacker, target, 0.6F);
            playCustomSound(attacker, MoCSoundEvents.ENTITY_GENERIC_TUD.get());
        }
    }

    public static void spawnNearPlayer(ServerPlayer player, int entityId, int numberToSpawn) {
        ServerLevel world = player.serverLevel(); // Replaces player.getServer().getWorld(...)

        for (int i = 0; i < numberToSpawn; i++) {
            Mob entity = null;
            try {
                Class<? extends Mob> entityClass = MoCreatures.instaSpawnerMap.get(entityId);
                entity = entityClass.getConstructor(Level.class).newInstance(world);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (entity != null) {
                entity.snapTo(
                        player.getX() - 1, player.getY(), player.getZ() - 1,
                        player.getYRot(), player.getXRot()
                );
                world.addFreshEntity(entity);
            }
        }
    }

    public static void playCustomSound(Entity entity, SoundEvent customSound) {
        playCustomSound(entity, customSound, 1.0F);
    }

    public static void playCustomSound(Entity entity, SoundEvent customSound, float volume) {
        float pitch = 1.0F + ((entity.level().getRandom().nextFloat() - entity.level().getRandom().nextFloat()) * 0.2F);
        entity.level().playSound(
                null,                      // null = plays for all nearby players
                entity.blockPosition(),    // sound origin
                customSound,               // sound event
                SoundSource.NEUTRAL,       // category (adjust if needed)
                volume,
                pitch
        );
    }

    public static JukeboxBlockEntity nearJukeBoxRecord(Entity entity, double dist) {
        AABB aabb = entity.getBoundingBox().inflate(dist, dist / 2D, dist);
        for (BlockPos pos : BlockPos.betweenClosed(
                Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ),
                Mth.floor(aabb.maxX + 1.0D), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ + 1.0D))) {
            if (entity.level().getBlockState(pos).getBlock() instanceof JukeboxBlock
                    && entity.level().getBlockEntity(pos) instanceof JukeboxBlockEntity jukebox) {
                return jukebox;
            }
        }
        return null;
    }

    public static void checkForTwistedEntities(Level level) {
        ServerLevel serverLevel = level.getServer().getLevel(level.dimension());
        if (serverLevel == null) return;

        for (Entity entity : serverLevel.getAllEntities()) {
            if (entity instanceof LivingEntity living) {
                if (living.deathTime > 0 && !living.isPassenger() && !living.isDeadOrDying()) {
                    living.deathTime = 0;
                }
            }
        }
    }

    public static double getSqDistanceTo(Entity entity, double x, double y, double z) {
        double dx = entity.getX() - x;
        double dy = entity.getY() - y;
        double dz = entity.getZ() - z;
        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    public static int[] returnNearestMaterialCoord(Entity entity, double rangeXZ, double rangeY) {
        double shortestDistance = -1D;
        double distance;
        int x = -9999, y = -1, z = -1;

        AABB box = entity.getBoundingBox().inflate(rangeXZ, rangeY, rangeXZ);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        int minX = Mth.floor(box.minX);
        int maxX = Mth.floor(box.maxX + 1.0D);
        int minY = Mth.floor(box.minY);
        int maxY = Mth.floor(box.maxY + 1.0D);
        int minZ = Mth.floor(box.minZ);
        int maxZ = Mth.floor(box.maxZ + 1.0D);

        for (int xi = minX; xi < maxX; xi++) {
            for (int yi = minY; yi < maxY; yi++) {
                for (int zi = minZ; zi < maxZ; zi++) {
                    pos.set(xi, yi, zi);
                    BlockState state = entity.level().getBlockState(pos);

                    if (state.getFluidState().isSource() && state.getFluidState().getType().isSame(Fluids.WATER)) {
                        distance = getSqDistanceTo(entity, xi, yi, zi);
                        if (shortestDistance == -1D || distance < shortestDistance) {
                            x = xi;
                            y = yi;
                            z = zi;
                            shortestDistance = distance;
                        }
                    }
                }
            }
        }

        if (entity.getX() > x) x -= 2;
        else x += 2;

        if (entity.getZ() > z) z -= 2;
        else z += 2;

        return new int[]{x, y, z};
    }

    public static int[] returnNearestBlockCoord(Entity entity, Block targetBlock, double dist) {
        double shortestDistance = -1D;
        double distance;
        int x = -9999, y = -1, z = -1;

        AABB box = entity.getBoundingBox().inflate(dist);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        int minX = Mth.floor(box.minX);
        int maxX = Mth.floor(box.maxX + 1.0D);
        int minY = Mth.floor(box.minY);
        int maxY = Mth.floor(box.maxY + 1.0D);
        int minZ = Mth.floor(box.minZ);
        int maxZ = Mth.floor(box.maxZ + 1.0D);

        for (int xi = minX; xi < maxX; xi++) {
            for (int yi = minY; yi < maxY; yi++) {
                for (int zi = minZ; zi < maxZ; zi++) {
                    pos.set(xi, yi, zi);
                    BlockState state = entity.level().getBlockState(pos);
                    if (!state.isAir() && state.getBlock() == targetBlock) {
                        distance = getSqDistanceTo(entity, xi, yi, zi);
                        if (shortestDistance == -1D || distance < shortestDistance) {
                            x = xi;
                            y = yi;
                            z = zi;
                            shortestDistance = distance;
                        }
                    }
                }
            }
        }

        if (entity.getX() > x) x -= 2;
        else x += 2;

        if (entity.getZ() > z) z -= 2;
        else z += 2;

        return new int[]{x, y, z};
    }

    public static BlockPos getTreeTop(Level level, Entity entity, int range) {
        BlockPos entityPos = BlockPos.containing(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos pos = entityPos.offset(x, y, z);
                    BlockState state = level.getBlockState(pos);

                    if (state.is(BlockTags.LOGS)) {
                        // Climb up until we find air above the leaves
                        for (int yOffset = 1; yOffset < 256; yOffset++) {
                            BlockPos checkPos = pos.above(yOffset);
                            BlockState checkState = level.getBlockState(checkPos);

                            if (checkState.isAir()) {
                                return checkPos;
                            } else if (!checkState.is(BlockTags.LEAVES)) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public static void moveCreatureToXYZ(PathfinderMob entity, int x, int y, int z, double speed) {
        Path path = entity.getNavigation().createPath(BlockPos.containing(x, y, z), 0);
        if (path != null) {
            entity.getNavigation().moveTo(path, speed);
        }
    }

    public static void moveToWater(PathfinderMob entity) {
        int[] coords = MoCTools.returnNearestMaterialCoord(entity, 20.0D, 2.0D);
        if (coords[0] > -1000) {
            MoCTools.moveCreatureToXYZ(entity, coords[0], coords[1], coords[2], 1.0D);
        }
    }

    /**
     * Gives angles in the range 0-360 i.e. 361 will be returned like 1
     */
    public static float realAngle(float origAngle) {
        return origAngle % 360F;
    }

    public static double waterSurfaceAtGivenPosition(double posX, double posY, double posZ, Level level) {
        BlockPos basePos = BlockPos.containing(posX, posY, posZ);
        BlockState state = level.getBlockState(basePos);

        if (!state.isAir() && state.getFluidState().getType().isSame(Fluids.WATER)) {
            for (int offset = 1; offset < 64; offset++) {
                BlockPos checkPos = basePos.above(offset);
                BlockState checkState = level.getBlockState(checkPos);
                if (checkState.isAir() || !checkState.getFluidState().getType().isSame(Fluids.WATER)) {
                    return checkPos.getY();
                }
            }
        }

        return 0.0;
    }

    public static double waterSurfaceAtGivenEntity(Entity entity) {
        return waterSurfaceAtGivenPosition(entity.getX(), entity.getY(), entity.getZ(), entity.level());
    }

    public static float distanceToSurface(double posX, double posY, double posZ, Level level) {
        BlockPos basePos = BlockPos.containing(posX, posY, posZ);
        BlockState state = level.getBlockState(basePos);

        if (!state.isAir() && state.getFluidState().getType().isSame(Fluids.WATER)) {
            for (int offset = 1; offset < 64; offset++) {
                BlockPos checkPos = basePos.above(offset);
                BlockState checkState = level.getBlockState(checkPos);
                if (checkState.isAir() || !checkState.getFluidState().getType().isSame(Fluids.WATER)) {
                    return offset;
                }
            }
        }

        return 0F;
    }

    public static int distanceToFloor(Entity entity) {
        BlockPos pos = BlockPos.containing(entity.getX(), entity.getY(), entity.getZ());
        Level level = entity.level();

        for (int offset = 0; offset < 64; offset++) {
            BlockPos checkPos = pos.below(offset);
            if (!level.getBlockState(checkPos).isAir()) {
                return offset;
            }
        }

        return 0;
    }

    public static String biomeName(Level level, BlockPos pos) {
        return level.getBiome(pos).unwrapKey()
                .map(key -> key.identifier().toString())
                .orElse("unknown");
    }

    public static ResourceKey<Biome> biomeKind(Level level, BlockPos pos) {
        return level.getBiome(pos).unwrapKey().orElse(Biomes.THE_VOID);
    }

    public static void destroyDrops(Entity entity, double range) {
        if (!MoCreatures.proxy.destroyDrops) return;

        AABB box = entity.getBoundingBox().inflate(range);
        List<Entity> entities = entity.level().getEntities(entity, box);

        for (Entity nearby : entities) {
            if (nearby instanceof ItemEntity item && item.getAge() < 50) {
                item.discard(); // replaces `remove()`
            }
        }
    }

    public static boolean mobGriefing(Level world) {
        return world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
    }

    public static void destroyBlast(Entity entity, double x, double y, double z, float strength, boolean fire) {
        Level level = entity.level();
        Player player = (entity instanceof Player p) ? p : null;

        level.playSound(player, x, y, z,
                MoCSoundEvents.ENTITY_GENERIC_DESTROY.get(),
                SoundSource.HOSTILE,
                4.0F,
                (1.0F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F) * 0.7F
        );

        boolean mobGrief = mobGriefing(level);

        Set<BlockPos> affectedBlocks = new HashSet<>();
        float blastRadius = strength;
        int steps = 16;

        for (int i = 0; i < steps; i++) {
            for (int j = 0; j < steps; j++) {
                for (int k = 0; k < steps; k++) {
                    if (i != 0 && i != steps - 1 && j != 0 && j != steps - 1 && k != 0 && k != steps - 1) continue;

                    double dx = ((double) i / (steps - 1)) * 2.0D - 1.0D;
                    double dy = ((double) j / (steps - 1)) * 2.0D - 1.0D;
                    double dz = ((double) k / (steps - 1)) * 2.0D - 1.0D;

                    double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    dx /= dist;
                    dy /= dist;
                    dz /= dist;

                    float power = strength * (0.7F + level.getRandom().nextFloat() * 0.6F);
                    double px = x;
                    double py = y;
                    double pz = z;

                    float step = 0.3F;
                    while (power > 0.0F) {
                        BlockPos pos = BlockPos.containing(px, py, pz);
                        BlockState state = level.getBlockState(pos);

                        if (!state.isAir()) {
                            float resistance = state.getBlock().getExplosionResistance();
                            power -= (resistance + 0.3F) * step;
                        }

                        if (power > 0.0F && py > entity.getY() && state.getDestroySpeed(level, pos) < 3.0F) {
                            affectedBlocks.add(pos);
                        }

                        px += dx * step;
                        py += dy * step;
                        pz += dz * step;
                        power -= step * 0.75F;
                    }
                }
            }
        }

        if (!level.isClientSide()) {
            AABB blastBox = new AABB(
                    x - blastRadius, y - blastRadius, z - blastRadius,
                    x + blastRadius, y + blastRadius, z + blastRadius
            );

            Vec3 blastCenter = new Vec3(x, y, z);
            List<Entity> entities = level.getEntities(entity, blastBox);

            for (Entity e : entities) {
                if (e instanceof MoCEntityOgre) continue;

                double distance = Math.sqrt(e.distanceToSqr(blastCenter)) / blastRadius;
                if (distance > 1.0D) continue;

                double dx = e.getX() - x;
                double dy = e.getY() - y;
                double dz = e.getZ() - z;
                double magnitude = Math.sqrt(dx * dx + dy * dy + dz * dz);

                if (magnitude != 0.0D) {
                    dx /= magnitude;
                    dy /= magnitude;
                    dz /= magnitude;

                    double exposure = Explosion.getSeenPercent(blastCenter, e);
                    double impact = (1.0D - distance) * exposure;
                    float damage = (float) (((impact * impact + impact) / 2.0D) * 7.0D * blastRadius + 1.0D);

                    e.hurtOrSimulate(level.damageSources().explosion(entity, null), damage);
                    Vec3 motion = e.getDeltaMovement().add(dx * impact, dy * impact, dz * impact);
                    e.setDeltaMovement(motion);
                }
            }
        }

        // CLIENT: Spawn particles
        if (level.isClientSide()) {
            for (BlockPos pos : affectedBlocks) {
                for (int i = 0; i < 5; i++) {
                    Vec3 spawn = new Vec3(pos.getX(), pos.getY(), pos.getZ()).add(
                            level.getRandom().nextDouble(), level.getRandom().nextDouble(), level.getRandom().nextDouble()
                    );
                    Vec3 motion = spawn.subtract(x, y, z).normalize().scale(level.getRandom().nextDouble() * 0.5);
                    level.addParticle(ParticleTypes.SMOKE, spawn.x, spawn.y, spawn.z, motion.x, motion.y, motion.z);
                }
                entity.setDeltaMovement(entity.getDeltaMovement().subtract(0.001D, 0.001D, 0));
            }
        }

        // SERVER: Destroy blocks
        if (!level.isClientSide() && mobGrief) {
            ServerLevel serverLevel = (ServerLevel) level;

            for (BlockPos pos : affectedBlocks) {
                BlockState state = level.getBlockState(pos);
                if (!state.isAir()) {
                    BlockEvent.BreakEvent breakEvent = new BlockEvent.BreakEvent(serverLevel, pos, state,
                            FakePlayerFactory.get(serverLevel, MoCreatures.MOCFAKEPLAYER));
                    if (!NeoForge.EVENT_BUS.post(breakEvent)) {
                        level.removeBlock(pos, false);
                        Explosion explosion = new Explosion(level, entity, pos.getX(), pos.getY(), pos.getZ(), 3f, false, Explosion.BlockInteraction.KEEP);
                        state.onBlockExploded(level, pos, explosion);
                    }
                }
            }
        }

        // SERVER: Set blocks on fire
        if (!level.isClientSide() && mobGrief && fire) {
            for (BlockPos pos : affectedBlocks) {
                if (level.getBlockState(pos).isAir() && level.getRandom().nextInt(8) == 0) {
                    ServerLevel serverLevel = (ServerLevel) level;
                    BlockEvent.BreakEvent breakEvent = new BlockEvent.BreakEvent(serverLevel, pos, Blocks.AIR.defaultBlockState(),
                            FakePlayerFactory.get(serverLevel, MoCreatures.MOCFAKEPLAYER));
                    if (!NeoForge.EVENT_BUS.post(breakEvent)) {
                        level.setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);
                    }
                }
            }
        }
    }


    public static void updatePlayerArmorEffects(Player player) {
        if (!MoCreatures.proxy.armorSetEffects) return;

        Item boots = player.getItemBySlot(EquipmentSlot.FEET).getItem();
        Item legs = player.getItemBySlot(EquipmentSlot.LEGS).getItem();
        Item plate = player.getItemBySlot(EquipmentSlot.CHEST).getItem();
        Item helmet = player.getItemBySlot(EquipmentSlot.HEAD).getItem();

        // Cave Scorpion Armor Set Effect - Night Vision
        if (boots == MoCItems.BOOTS_SCORP_C.get() && legs == MoCItems.LEGS_SCORP_C.get() &&
                plate == MoCItems.PLATE_SCORP_C.get() && helmet == MoCItems.HELMET_SCORP_C.get()) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0));
            return;
        }

        // Fire Scorpion Armor Set Effect - Fire Resistance
        if (boots == MoCItems.BOOTS_SCORP_N.get() && legs == MoCItems.LEGS_SCORP_N.get() &&
                plate == MoCItems.PLATE_SCORP_N.get() && helmet == MoCItems.HELMET_SCORP_N.get()) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0));
            return;
        }

        // Frost Scorpion Armor Set Effect - Resistance
        if (boots == MoCItems.BOOTS_SCORP_F.get() && legs == MoCItems.LEGS_SCORP_F.get() &&
                plate == MoCItems.PLATE_SCORP_F.get() && helmet == MoCItems.HELMET_SCORP_F.get()) {
            player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 300, 0));
            return;
        }

        // Dirt Scorpion Armor Set Effect - Health Boost
        if (boots == MoCItems.BOOTS_SCORP_D.get() && legs == MoCItems.LEGS_SCORP_D.get() &&
                plate == MoCItems.PLATE_SCORP_D.get() && helmet == MoCItems.HELMET_SCORP_D.get()) {
            player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 300, 1));
            return;
        }

        // Undead Scorpion Armor Set Effect - Strength
        if (boots == MoCItems.BOOTS_SCORP_U.get() && legs == MoCItems.LEGS_SCORP_U.get() &&
                plate == MoCItems.PLATE_SCORP_U.get() && helmet == MoCItems.HELMET_SCORP_U.get()) {
            player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 300, 0));
        }
    }

    @Nullable
    public static BlockState destroyRandomBlockWithIBlockState(Entity entity, double distance) {
        int l = (int) (distance * distance * distance);
        Level level = entity.level();

        for (int i = 0; i < l; i++) {
            int x = (int) (entity.getX() + level.getRandom().nextInt((int) distance) - (distance / 2));
            int y = (int) (entity.getY() + level.getRandom().nextInt((int) distance) - (distance / 2));
            int z = (int) (entity.getZ() + level.getRandom().nextInt((int) distance) - (distance / 2));
            BlockPos pos = new BlockPos(Mth.floor(x), Mth.floor(y), Mth.floor(z));
            BlockState stateAbove = level.getBlockState(pos.above());
            BlockState stateTarget = level.getBlockState(pos);

            if (pos.getY() == (int) entity.getY() - 1D && pos.getX() == Mth.floor(entity.getX()) && pos.getZ() == Mth.floor(entity.getZ())) {
                continue;
            }

            if (!stateTarget.isAir() && stateTarget.getBlock() != Blocks.WATER && stateTarget.getBlock() != Blocks.BEDROCK && stateAbove.isAir()) {
                if (mobGriefing(level)) {
                    BlockEvent.BreakEvent event = null;
                    if (!level.isClientSide()) {
                        event = new BlockEvent.BreakEvent(level, pos, stateTarget, FakePlayerFactory.get((ServerLevel) level, MoCreatures.MOCFAKEPLAYER));
                    }
                    if (event != null && !event.isCanceled()) {
                        level.removeBlock(pos, false);
                    } else {
                        stateTarget = null;
                    }
                }
                if (stateTarget != null) {
                    return stateTarget;
                }
            }
        }

        return null;
    }

    public static BlockPos getRandomSurfaceBlockPos(Entity entity, int distance) {
        BlockPos pos = entity.blockPosition();
        Level world = entity.level();

        int x = pos.getX() + world.random.nextInt(distance * 2 + 1) - distance;
        int z = pos.getZ() + world.random.nextInt(distance * 2 + 1) - distance;
        int y = world.getChunk(x >> 4, z >> 4).getHeight(Heightmap.Types.MOTION_BLOCKING, x & 15, z & 15) - 1;

        return new BlockPos(x, y, z);
    }

    /**
     * Method called to tame an entity, it will check that the player has slots
     * for taming, increase the taming count of the player, add the
     * player.getName() as the owner of the entity, and name the entity.
     */
    public static InteractionResult tameWithName(Player ep, IMoCTameable storedCreature) {
        if (ep == null || storedCreature == null) return InteractionResult.PASS;

        storedCreature.setOwnerId(ep.getUUID());

        if (MoCreatures.proxy.enableOwnership) {
            int max = MoCreatures.proxy.maxTamed;
            if (!MoCreatures.instance.mapData.isExistingPet(ep.getUUID(), storedCreature)) {
                int count = MoCTools.numberTamedByPlayer(ep);
                if (isThisPlayerAnOP(ep)) {
                    max = MoCreatures.proxy.maxOPTamed;
                }
                if (count >= max) {
                    ep.sendSystemMessage(Component.literal("§4" + ep.getName().getString() + " can not tame more creatures, limit of " + max + " reached"));
                    return InteractionResult.PASS;
                }
            }
        }

        storedCreature.setTamed(true);

        if (MoCreatures.instance.mapData != null && storedCreature.getOwnerPetId() == -1) {
            MoCreatures.instance.mapData.updateOwnerPet(storedCreature);
            
            // Force a save of the pet data immediately after taming
            if (ep.level() instanceof ServerLevel) {
                MoCreatures.LOGGER.info("Forcing save after taming pet for player {}", ep.getName().getString());
                ((ServerLevel) ep.level()).getDataStorage().save();
            }
        }

        if (!ep.level().isClientSide() && MoCreatures.proxy.alwaysNamePets && ep instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) ep;
            Entity entity = (Entity) storedCreature;

            MoCTools.runLater(() -> {
                MoCMessageHandler.sendToPlayer(serverPlayer, new MoCMessageNameGUI(entity.getId()));
            }, 3);
        }

        return InteractionResult.SUCCESS;
    }


    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static void runLater(Runnable task, int ticksDelay) {
        long delayMs = ticksDelay * 50L;
        scheduler.schedule(task, delayMs, TimeUnit.MILLISECONDS);
    }


    public static int numberTamedByPlayer(Player ep) {
        if (MoCreatures.instance.mapData != null && MoCreatures.instance.mapData.getPetData(ep.getUUID()) != null) {
            return MoCreatures.instance.mapData.getPetData(ep.getUUID()).getTamedList().size();
        }
        return 0;
    }

    /**
     * Destroys blocks in front of entity
     *
     * @param distance used to calculate the distance where the target block is
     *                 located
     * @param strength int 1 - 3. Checked against block hardness, also used to
     *                 calculate how many blocks are recovered
     * @param height   how many rows of blocks are destroyed in front of the
     *                 entity
     * @return the count of blocks destroyed
     */
    public static int destroyBlocksInFront(Entity entity, double distance, int strength, int height) {
        if (strength == 0) {
            return 0;
        }
        int count = 0;
        double newPosX = entity.getX() - (distance * Math.cos((MoCTools.realAngle(entity.getYRot() - 90F)) / (180F / Math.PI)));
        double newPosZ = entity.getZ() - (distance * Math.sin((MoCTools.realAngle(entity.getYRot() - 90F)) / (180F / Math.PI)));
        double newPosY = entity.getY();
        int x = Mth.floor(newPosX);
        int y = Mth.floor(newPosY);
        int z = Mth.floor(newPosZ);

        for (int i = 0; i < height; i++) {
            BlockPos pos = new BlockPos(x, y + i, z);
            BlockState blockstate = entity.level().getBlockState(pos);
            if (!blockstate.isAir() && blockstate.getDestroySpeed(entity.level(), pos) <= strength) {
                BlockEvent.BreakEvent event = null;
                if (!entity.level().isClientSide()) {
                    event = new BlockEvent.BreakEvent(entity.level(), pos, blockstate,
                            FakePlayerFactory.get((ServerLevel) entity.level(), MoCreatures.MOCFAKEPLAYER));
                }
                if (event != null && !event.isCanceled()) {
                    entity.level().removeBlock(pos, false);
                    if (entity.level().getRandom().nextInt(3) == 0) {
                        playCustomSound(entity, MoCSoundEvents.ENTITY_GOLEM_WALK.get());
                        count++; // only counts recovered blocks
                    }
                }
            }
        }
        return count;
    }

    public static void dropInventory(Entity entity, MoCAnimalChest animalchest) {
        if (animalchest == null || entity.level().isClientSide()) {
            return;
        }

        int i = Mth.floor(entity.getX());
        int j = Mth.floor(entity.getBoundingBox().minY);
        int k = Mth.floor(entity.getZ());

        RandomSource random = entity.level().getRandom();

        for (int l = 0; l < animalchest.getContainerSize(); l++) {
            ItemStack itemstack = animalchest.getItem(l);
            if (itemstack.isEmpty()) {
                continue;
            }

            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            float f2 = random.nextFloat() * 0.8F + 0.1F;
            float f3 = 0.05F;

            ItemEntity entityitem = new ItemEntity(entity.level(), i + f, j + f1, k + f2, itemstack);
            entityitem.setDeltaMovement(
                    random.nextGaussian() * f3,
                    random.nextGaussian() * f3 + 0.2F,
                    random.nextGaussian() * f3
            );

            entity.level().addFreshEntity(entityitem);
            animalchest.setItem(l, ItemStack.EMPTY);
        }
    }

    /**
     * Drops an amulet with the stored information of the entity passed
     */
    public static void dropHorseAmulet(MoCEntityTameableAnimal entity) {
        if (entity.level().isClientSide()) {
            return;
        }

        ItemStack stack = getProperAmulet(entity);
        if (stack == null) {
            return;
        }

        CompoundTag tag = stack.getOrCreateTag(); // replaces .getTag() and null check
        UUID ownerId = entity.getOwnerId();
        Player ownerPlayer = null;

        if (ownerId != null) {
            ownerPlayer = entity.level().getPlayerByUUID(ownerId);
        }

        try {
            tag.putString("SpawnClass", "WildHorse");
            tag.putFloat("Health", entity.getHealth());
            tag.putInt("Edad", entity.getMoCAge());
            tag.putString("Name", entity.getPetName());
            tag.putBoolean("Rideable", entity.getIsRideable());
            tag.putInt("Armor", entity.getArmorType());
            tag.putInt("CreatureType", entity.getTypeMoC());
            tag.putBoolean("Adult", entity.getIsAdult());
            tag.putString("OwnerName", ownerPlayer != null ? ownerPlayer.getName().getString() : "");
            if (ownerId != null) {
                tag.putUUID("OwnerUUID", ownerId);
            }
            tag.putInt("PetId", entity.getOwnerPetId());

            int amuletType = 1;
            if (stack.getItem() == MoCItems.PET_AMULET_FULL.get()) {
                amuletType = 2;
            } else if (stack.getItem() == MoCItems.AMULET_GHOST_FULL.get()) {
                amuletType = 3;
            }

            tag.putBoolean("Ghost", amuletType == 3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ownerPlayer != null && ownerPlayer.getInventory().getFreeSlot() != -1) {
            ownerPlayer.getInventory().add(stack);
        } else {
            ItemEntity entityItem = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), stack);
            entityItem.setPickUpDelay(20);
            entity.level().addFreshEntity(entityItem);
        }
    }

    /**
     * Drops a new amulet/fishnet with the stored information of the entity
     */
    public static void dropAmulet(IMoCEntity entity, int amuletType, Player player) {
        if (player.level().isClientSide()) return;

        ItemStack stack = switch (amuletType) {
            case 2 -> new ItemStack(MoCItems.PET_AMULET_FULL.get());
            case 3 -> new ItemStack(MoCItems.AMULET_GHOST_FULL.get());
            default -> new ItemStack(MoCItems.FISH_NET_FULL.get());
        };

        CompoundTag tag = stack.getOrCreateTag();

        try {
            final EntityType<?> entry = ((Entity) entity).getType();
            final String petClass = BuiltInRegistries.ENTITY_TYPE.getKey(entry).getPath().replace(MoCConstants.MOD_PREFIX, "");
            tag.putString("SpawnClass", petClass);
            tag.putUUID("OwnerUUID", player.getUUID());
            tag.putString("OwnerName", player.getName().getString());
            tag.putFloat("Health", ((LivingEntity) entity).getHealth());
            tag.putInt("Edad", entity.getMoCAge());
            tag.putString("Name", entity.getPetName());
            tag.putInt("CreatureType", entity.getTypeMoC());
            tag.putBoolean("Adult", entity.getIsAdult());
            tag.putInt("PetId", entity.getOwnerPetId());
            tag.putBoolean("Ghost", amuletType == 3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!player.getInventory().add(stack)) {
            Level level = ((LivingEntity) entity).level();
            ItemEntity itemEntity= new ItemEntity(level, ((Entity) entity).getX(), ((Entity) entity).getY(), ((Entity) entity).getZ(), stack);
            itemEntity.setPickUpDelay(20);
            level.addFreshEntity(itemEntity);
        }
    } 


    /**
     * Returns the right full amulet based on the MoCEntityAnimal passed
     */
    public static ItemStack getProperAmulet(MoCEntityAnimal entity) {
        if (entity instanceof MoCEntityHorse) {
            int type = entity.getTypeMoC();

            if (type == 26 || type == 27 || type == 28) {
                return new ItemStack(MoCItems.AMULET_BONE_FULL.get());
            }
            if (type > 47 && type < 60) {
                return new ItemStack(MoCItems.AMULET_FAIRY_FULL.get());
            }
            if (type == 39 || type == 40) {
                return new ItemStack(MoCItems.AMULET_PEGASUS_FULL.get());
            }
            if (type == 21 || type == 22) {
                return new ItemStack(MoCItems.AMULET_GHOST_FULL.get());
            }
        }

        return ItemStack.EMPTY;
    }

    public static boolean isThisPlayerAnOP(Player player) {
        if (player.level().isClientSide()) return false;

        return player.getServer().getPlayerList().isOp(player.getGameProfile());
    }

    public static void spawnMaggots(Level level, Entity entity) {
        if (!level.isClientSide()) {
            RandomSource rand = level.getRandom();
            int count = 1 + rand.nextInt(4);
            for (int i = 0; i < count; ++i) {
                float offsetX = (i % 2 - 0.5F) * 0.25F;
                float offsetZ = ((float) i / 2 - 0.5F) * 0.25F;

                MoCEntityMaggot maggot = MoCEntities.MAGGOT.get().create(level, EntitySpawnReason.BREEDING);
                if (maggot != null) {
                    maggot.snapTo(entity.getX() + offsetX, entity.getY() + 0.5D, entity.getZ() + offsetZ,
                            rand.nextFloat() * 360.0F, 0.0F);
                    level.addFreshEntity(maggot);
                }
            }
        }
    }

    public static void setPathToEntity(Mob creature, Entity target, float distance) {
        if (creature.getNavigation() instanceof PathNavigation) {
            Path path = creature.getNavigation().createPath(target, 0);
            if (path != null && distance < 12F) {
                creature.getNavigation().moveTo(path, 1.0D);
            }
        }
    }

    public static void runLikeHell(Mob runner, Entity boogey) {
        RandomSource rand = runner.getRandom();

        double dx = runner.getX() - boogey.getX();
        double dz = runner.getZ() - boogey.getZ();
        double angle = Math.atan2(dx, dz);
        angle += (rand.nextFloat() - rand.nextFloat()) * 0.75D;

        double targetX = runner.getX() + (Math.sin(angle) * 8D);
        double targetZ = runner.getZ() + (Math.cos(angle) * 8D);

        int baseX = Mth.floor(targetX);
        int baseY = Mth.floor(runner.getBoundingBox().minY);
        int baseZ = Mth.floor(targetZ);
        Level level = runner.level();

        for (int i = 0; i < 16; i++) {
            int x = baseX + rand.nextInt(4) - rand.nextInt(4);
            int y = baseY + rand.nextInt(3) - rand.nextInt(3);
            int z = baseZ + rand.nextInt(4) - rand.nextInt(4);
            BlockPos pos = new BlockPos(x, y, z);

            BlockState state = level.getBlockState(pos);
            BlockState below = level.getBlockState(pos.below());

            if (y > 4 && (state.isAir() || state.is(Blocks.SNOW)) && !below.isAir()) {
                runner.getNavigation().moveTo(x, y, z, 1.5D);
                break;
            }
        }
    }

    /**
     * Finds a near vulnerable player and poisons it if the player is in the
     * water and not riding anything
     *
     * @param needsToBeInWater: the target needs to be in water for poison to be
     *                          successful?
     * @return true if was able to poison the player
     */
    public static boolean findNearPlayerAndPoison(Entity poisoner, boolean needsToBeInWater) {
        Level level = poisoner.level();
        Player target = level.getNearestPlayer(poisoner, 2D);

        if (target != null &&
                (!needsToBeInWater || target.isInWater()) &&
                poisoner.distanceTo(target) < 2.0F &&
                !target.getAbilities().invulnerable &&
                !(target.getVehicle() instanceof Boat)) {

            target.addEffect(new MobEffectInstance(MobEffects.POISON, 120, 0));
            return true;
        }

        return false;
    }

    public static boolean isTamed(Entity entity) {
        if (entity instanceof TamableAnimal tamable && tamable.isTame()) {
            return true;
        }

        CompoundTag tag = entity.serializeNBT();
        if (tag.contains("Owner") && !tag.getStringOr("Owner", "").isEmpty()) {
            return true;
        }

        return tag.contains("Tamed") && tag.getBooleanOr("Tamed", false);
    }

    /**
     * Throws stone at entity
     */
    public static void throwStone(Entity throwerEntity, Entity targetEntity, BlockState state, double speedMod, double height) {
        throwStone(throwerEntity, (int) targetEntity.getX(), (int) targetEntity.getY(), (int) targetEntity.getZ(), state, speedMod, height);
    }

    public static void throwStone(Entity throwerEntity, double x, double y, double z, BlockState state, double speedMod, double height) {
        MoCEntityThrowableRock etrock = MoCEntityThrowableRock.build(
            throwerEntity.level(), throwerEntity, 
            throwerEntity.getX(), throwerEntity.getY() + 0.5D, throwerEntity.getZ()
        );
        if (etrock == null) return;
    
        etrock.setState(state);
        etrock.setBehavior(0);
        etrock.setNoGravity(false);
    
        double dx = (x - throwerEntity.getX()) / speedMod;
        double dy = (y - throwerEntity.getY()) / speedMod + height;
        double dz = (z - throwerEntity.getZ()) / speedMod;
        etrock.setDeltaMovement(dx, dy, dz);
        etrock.hasImpulse = true;
    
        throwerEntity.level().addFreshEntity(etrock);
    }
    

    /**
     * Calculates the moving speed of the entity
     */
    public static float getMyMovementSpeed(Entity entity) {
        return Mth.sqrt((float) (entity.getDeltaMovement().x * entity.getDeltaMovement().x + entity.getDeltaMovement().z * entity.getDeltaMovement().z));
    }

    public static ItemEntity getClosestFood(Entity entity, double range) {
        double closestDistSq = -1D;
        ItemEntity closestItem = null;

        AABB searchBox = entity.getBoundingBox().inflate(range);
        List<Entity> list = entity.level().getEntities(entity, searchBox);

        for (Entity other : list) {
            if (!(other instanceof ItemEntity itemEntity)) continue;

            if (!isItemEdible(itemEntity.getItem().getItem())) continue;

            double distSq = itemEntity.distanceToSqr(entity.getX(), entity.getY(), entity.getZ());

            if ((range < 0.0D || distSq < (range * range)) && (closestDistSq == -1D || distSq < closestDistSq)) {
                closestDistSq = distSq;
                closestItem = itemEntity;
            }
        }

        return closestItem;
    }

    /**
     * List of edible foods
     */
    public static boolean isItemEdible(Item item) {
        return item.isEdible()
                || item.builtInRegistryHolder().is(Tags.Items.SEEDS)
                || item == Items.WHEAT
                || item == Items.SUGAR
                || item == Items.CAKE
                || item == Items.EGG;
    }

    public static boolean isItemEdibleForCarnivores(Item item) {
        return item == Items.BEEF
                || item == Items.CHICKEN
                || item == Items.COOKED_BEEF
                || item == Items.COOKED_CHICKEN
                || item == Items.RABBIT
                || item == Items.COOKED_MUTTON
                || item == Items.COOKED_PORKCHOP
                || item == Items.MUTTON
                || item == Items.COOKED_RABBIT
                || item == Items.PORKCHOP
                || item.builtInRegistryHolder().is(MoCTags.Items.COOKED_FISHES)
                || item.builtInRegistryHolder().is(MoCTags.Items.RAW_FISHES);
    }

    public static CompoundTag getEntityData(Entity entity) {
        if (!entity.getPersistentData().contains(MoCConstants.MOD_ID)) {
            entity.getPersistentData().put(MoCConstants.MOD_ID, new CompoundTag());
        }
        return entity.getPersistentData().getCompoundOrEmpty(MoCConstants.MOD_ID);
    }

    // ---- 26.3 save data helpers: entities save through ValueOutput / ValueInput instead of a CompoundTag ----

    /** Writes any NBT tag (compound or list) under {@code key}, as the 1.20 {@code CompoundTag.put} did. */
    public static void writeTag(ValueOutput output, String key, Tag tag) {
        output.store(key, ExtraCodecs.NBT, tag);
    }

    /** Reads a compound written by {@link #writeTag}; empty if it isn't there. */
    public static CompoundTag readCompound(ValueInput input, String key) {
        return input.read(key, CompoundTag.CODEC).orElseGet(CompoundTag::new);
    }

    /** Reads a list written by {@link #writeTag}; empty if it isn't there. */
    public static ListTag readList(ValueInput input, String key) {
        return input.read(key, ExtraCodecs.NBT).filter(t -> t instanceof ListTag).map(t -> (ListTag) t).orElseGet(ListTag::new);
    }

    /** Writes an item stack into {@code target} (alongside keys such as "Slot"), as 1.20's {@code ItemStack.save} did. */
    public static void saveStack(Entity holder, ItemStack stack, CompoundTag target) {
        if (stack.isEmpty()) {
            return;
        }
        ItemStack.CODEC.encodeStart(holder.registryAccess().createSerializationContext(NbtOps.INSTANCE), stack)
                .result()
                .filter(tag -> tag instanceof CompoundTag)
                .ifPresent(tag -> target.merge((CompoundTag) tag));
    }

    /** Reads an item stack written by {@link #saveStack}; empty if it can't be read. */
    public static ItemStack loadStack(Entity holder, CompoundTag source) {
        return ItemStack.CODEC.parse(holder.registryAccess().createSerializationContext(NbtOps.INSTANCE), source)
                .result()
                .orElse(ItemStack.EMPTY);
    }

    /** Looting level of whoever caused {@code source}; 26.3 no longer passes it to dropCustomDeathLoot. */
    public static int lootingLevel(ServerLevel level, DamageSource source) {
        if (source.getEntity() instanceof LivingEntity attacker) {
            return level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.LOOTING)
                    .map(looting -> EnchantmentHelper.getEnchantmentLevel(looting, attacker))
                    .orElse(0);
        }
        return 0;
    }

    /** Whether anything was saved under {@code key}. */
    public static boolean has(ValueInput input, String key) {
        return input.read(key, ExtraCodecs.NBT).isPresent();
    }

    public static void findMobRider(Entity mountEntity) {
        List<Entity> list = mountEntity.level().getEntities(mountEntity, mountEntity.getBoundingBox().inflate(4D, 2D, 4D));
        for (Entity entity : list) {
            if (!(entity instanceof Monster)) continue;
            if (entity.getVehicle() == null && (entity instanceof Skeleton || entity instanceof Zombie || entity instanceof MoCEntitySilverSkeleton)) {
                if (!mountEntity.level().isClientSide()) {
                    entity.startRiding(mountEntity);
                }
                break;
            }
        }
    }

    public static void copyDataFromOld(Entity source, Entity target) {
        CompoundTag tag = target.saveWithoutId(new CompoundTag());
        tag.remove("Dimension");
        source.load(tag);
    }

    public static void dismountSneakingPlayer(Mob entity) {
        if (!entity.isPassenger()) return;

        Entity ridden = entity.getVehicle();
        if (ridden instanceof LivingEntity && ridden.isShiftKeyDown()) {
            entity.stopRiding();

            double dist = -1.5D;
            double yaw = Math.toRadians(((LivingEntity) ridden).getYRot());
            double newX = ridden.getX() + (dist * Math.sin(yaw));
            double newZ = ridden.getZ() - (dist * Math.cos(yaw));
            double newY = ridden.getY() + 2D;

            entity.teleportTo(newX, newY, newZ);
            playCustomSound(entity, SoundEvents.CHICKEN_EGG);
        }
    }

    public static double roundToNearest90Degrees(double degrees) {
        double radians = Math.toRadians(degrees);
        double roundedRadians = Math.round(radians / (Math.PI / 2)) * (Math.PI / 2);
        return Math.toDegrees(roundedRadians);
    }

    public static List<String> getAllBiomeTags() {
        RegistryAccess access = getSafeRegistryAccess();
        if (access == null) return Collections.emptyList();

        Registry<Biome> biomeRegistry = access.registryOrThrow(Registries.BIOME);
        Set<Identifier> tagSet = new HashSet<>();

        biomeRegistry.holders().forEach(biomeHolder -> {
            biomeHolder.tags().forEach(tagKey -> tagSet.add(tagKey.location()));
        });

        return tagSet.stream()
                .sorted()
                .map(Identifier::toString)
                .collect(Collectors.toList());
    }

    private static RegistryAccess getSafeRegistryAccess() {
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            // Server-side
            return ServerLifecycleHooks.getCurrentServer().registryAccess();
        } else if (MoCreatures.proxy.getPlayer() != null) {
            // Client-side (the client proxy returns the local player)
            return MoCreatures.proxy.getPlayer().level().registryAccess();
        }
        return null; // Fallback
    }

    /**
     * Checks if the entity type is allowed to spawn in the Wyvern Lair dimension
     */
    public static boolean canSpawnInWyvernLair(Entity entity) {
        if (entity == null) {
            return false;
        }
        
        // Always allow players
        if (entity instanceof Player) {
            return true;
        }
        
        // Allow item entities, experience orbs, and other non-mob entities
        if (!(entity instanceof Mob)) {
            return true;
        }
        
        // Use a lazy approach to get the entity types to avoid early initialization issues
        try {
            EntityType<?> entityType = entity.getType();
            
            // Check by entity type name as a safer approach
            Identifier entityId = EntityType.getKey(entityType);
            String entityName = entityId.getPath();
            
            return entityName.equals("wyvern") || 
                   entityName.equals("bunny") || 
                   entityName.equals("snake") ||
                   entityName.equals("filchlizard") ||
                   entityName.equals("dragonfly") ||
                   entityName.equals("firefly") ||
                   entityName.equals("grasshopper");
        } catch (Exception e) {
            // If anything goes wrong, log and allow the entity to be safe
            MoCreatures.LOGGER.debug("Error checking entity type for Wyvern Lair: " + e.getMessage());
            return true;
        }
    }
    
    /**
     * Checks if an entity is in the Wyvern Lair dimension
     */
    public static boolean isInWyvernLair(Entity entity) {
        if (entity == null || entity.level() == null) {
            return false;
        }
        
        ResourceKey<Level> dimension = entity.level().dimension();
        Identifier dimensionLocation = dimension.identifier();
        
        // Check if this is the Wyvern Lair dimension
        return dimensionLocation.toString().equals("mocreatures:wyvernlairworld");
    }
}
