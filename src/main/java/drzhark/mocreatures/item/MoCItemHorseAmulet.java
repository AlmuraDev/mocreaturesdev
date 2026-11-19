package drzhark.mocreatures.item;
import net.minecraft.core.UUIDUtil;
import java.util.function.Consumer;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.hunter.MoCEntityBigCat;
import drzhark.mocreatures.entity.neutral.MoCEntityWyvern;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.entity.tameable.MoCPetData;
import drzhark.mocreatures.init.MoCEntities;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAppear;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MoCItemHorseAmulet extends MoCItem {

    private int ageCounter;
    private String name;
    private float health;
    private int age;
    private int creatureType;
    private String spawnClass;
    private boolean isGhost;
    private boolean rideable;
    private byte armor;
    private boolean adult;
    private UUID ownerUniqueId;
    private String ownerName;
    private int PetId;

    public MoCItemHorseAmulet(Item.Properties properties) {
        super(properties.stacksTo(1));
        this.ageCounter = 0;
    }

    @SuppressWarnings("removal")
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (++this.ageCounter < 2) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            initAndReadNBT(stack);
        }

        double dist = 3D;
        double newPosY = player.getY();
        double newPosX = player.getX() - (dist * Math.cos((MoCTools.realAngle(player.getYRot() - 90F)) / 57.29578F));
        double newPosZ = player.getZ() - (dist * Math.sin((MoCTools.realAngle(player.getYRot() - 90F)) / 57.29578F));

        if (!level.isClientSide()) {
            try {
                MoCEntityTameableAnimal storedCreature;
                this.spawnClass = this.spawnClass.replace(MoCConstants.MOD_PREFIX, "").toLowerCase();

                if (this.spawnClass.equalsIgnoreCase("Wyvern")) {
                    storedCreature = MoCEntities.WYVERN.get().create(level, EntitySpawnReason.SPAWN_ITEM_USE);
                    ((MoCEntityWyvern) storedCreature).setIsGhost(true);
                    this.isGhost = true;
                } else if (this.spawnClass.equalsIgnoreCase("WildHorse")) {
                    storedCreature = MoCEntities.WILDHORSE.get().create(level, EntitySpawnReason.SPAWN_ITEM_USE);
                } else {
                    Identifier res = Identifier.parse(MoCConstants.MOD_PREFIX + this.spawnClass.toLowerCase());
                    storedCreature = (MoCEntityTameableAnimal) BuiltInRegistries.ENTITY_TYPE.getValue(res).create(level, EntitySpawnReason.SPAWN_ITEM_USE);
                    if (storedCreature instanceof MoCEntityBigCat) {
                        this.isGhost = true;
                        ((MoCEntityBigCat) storedCreature).setIsGhost(true);
                    }
                }

                storedCreature.setPos(newPosX, newPosY, newPosZ);
                storedCreature.setTypeMoC(this.creatureType);
                storedCreature.setTamed(true);
                storedCreature.setRideable(this.rideable);
                storedCreature.setPetName(this.name);
                storedCreature.setHealth(this.health);
                // Set adult status BEFORE age to prevent automatic adult detection conflicts
                storedCreature.setAdult(this.adult);
                storedCreature.setMoCAge(this.age);
                storedCreature.setArmorType(this.armor);
                storedCreature.setOwnerPetId(this.PetId);
                storedCreature.setOwnerId(player.getUUID());
                this.ownerName = player.getName().getString();

//                if (this.ownerUniqueId == null) {
//                    this.ownerUniqueId = player.getUUID();
//                    MoCTools.updatePetDataIfAllowed(player, storedCreature);
//                } else {
//                    MoCTools.transferPetOwnershipIfNeeded(this.ownerUniqueId, player, storedCreature);
//                }

                if (level.addFreshEntity(storedCreature)) {
                    MoCMessageHandler.sendToNear(level, player.getX(), player.getY(), player.getZ(), 64, new MoCMessageAppear(storedCreature.getId()));

                    MoCTools.playCustomSound(storedCreature, MoCSoundEvents.ENTITY_GENERIC_MAGIC_APPEAR.get());


                    // Case 3 is for tigers - They don't have a separate creature type for ghost states
                    // Replace amulet
                    ItemStack replacement = switch (this.creatureType) {
                        case 21, 22 -> new ItemStack(MoCItems.AMULET_GHOST.get());
                        case 26, 27, 28 -> new ItemStack(MoCItems.AMULET_BONE.get());
                        case 39, 40 -> new ItemStack(MoCItems.AMULET_PEGASUS.get());
                        default -> (this.creatureType > 47 && this.creatureType < 60) ? new ItemStack(MoCItems.AMULET_FAIRY.get()) : ItemStack.EMPTY;
                    };

                    if (storedCreature.getIsGhost()) {
                        replacement = new ItemStack(MoCItems.AMULET_GHOST.get());
                    }
                    
                    if (!replacement.isEmpty()) {
                        player.setItemInHand(hand, replacement);
                    }

                    MoCPetData petData = MoCreatures.instance.mapData.getPetData(storedCreature.getOwnerId());
                    if (petData != null) {
                        petData.setInAmulet(storedCreature.getOwnerPetId(), false);
                    }
                }
            } catch (Exception ex) {
                System.out.println("Unable to find class for entity " + this.spawnClass);
                ex.printStackTrace();
            }
        }

        this.ageCounter = 0;
        return InteractionResult.SUCCESS;
    }

    public void readFromNBT(CompoundTag tag) {
        this.PetId = tag.getIntOr("PetId", 0);
        this.creatureType = tag.getIntOr("CreatureType", 0);
        this.health = tag.getFloatOr("Health", 0.0F);
        this.age = tag.getIntOr("Edad", 0);
        this.name = tag.getStringOr("Name", "");

        int spawnClassOld = tag.getIntOr("SpawnClass", 0);
        if (spawnClassOld > 0) {
            if (spawnClassOld == 100) {
                this.spawnClass = "Wyvern";
                this.isGhost = true;
            } else {
                this.spawnClass = "WildHorse";
            }
            tag.remove("SpawnClass");
        } else {
            this.spawnClass = tag.getStringOr("SpawnClass", "");
        }

        this.rideable = tag.getBooleanOr("Rideable", false);
        this.armor = tag.getByteOr("Armor", (byte) 0);
        this.adult = tag.getBooleanOr("Adult", false);
        this.ownerName = tag.getStringOr("OwnerName", "");
        if (tag.read("OwnerUUID", UUIDUtil.CODEC).isPresent()) {
            this.ownerUniqueId = tag.read("OwnerUUID", UUIDUtil.CODEC).orElse(null);
        }
    }

    public void writeToNBT(CompoundTag tag) {
        tag.putInt("PetId", this.PetId);
        tag.putInt("CreatureType", this.creatureType);
        tag.putFloat("Health", this.health);
        tag.putInt("Edad", this.age);
        tag.putString("Name", this.name);
        tag.putString("SpawnClass", this.spawnClass);
        tag.putBoolean("Rideable", this.rideable);
        tag.putByte("Armor", this.armor);
        tag.putBoolean("Adult", this.adult);
        tag.putString("OwnerName", this.ownerName);
        if (this.ownerUniqueId != null) {
            tag.store("OwnerUUID", UUIDUtil.CODEC, this.ownerUniqueId);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        initAndReadNBT(stack);
        tooltip.accept(Component.literal(this.spawnClass).withStyle(ChatFormatting.AQUA));

        if (!this.name.isEmpty()) {
            tooltip.accept(Component.literal(this.name).withStyle(ChatFormatting.BLUE));
        }
        if (!this.ownerName.isEmpty()) {
            tooltip.accept(Component.literal("Owned by " + this.ownerName).withStyle(ChatFormatting.DARK_BLUE));
        }
    }

    private void initAndReadNBT(ItemStack itemstack) {
        readFromNBT(MoCTools.stackTag(itemstack));
    }

    }