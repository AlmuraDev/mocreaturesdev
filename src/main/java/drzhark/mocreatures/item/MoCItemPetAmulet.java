package drzhark.mocreatures.item;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.hunter.MoCEntityBigCat;
import drzhark.mocreatures.entity.neutral.MoCEntityKitty;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import drzhark.mocreatures.entity.tameable.MoCPetData;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAppear;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MoCItemPetAmulet extends MoCItem {

    private String name = "";
    private float health;
    private int age;
    private int creatureType;
    private String spawnClass = "";
    private String ownerName = "";
    private UUID ownerUniqueId;
    private int amuletType;
    private boolean adult;
    private int PetId;

    public MoCItemPetAmulet(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    public MoCItemPetAmulet(Item.Properties properties, int type) {
        this(properties);
        this.amuletType = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        double dist = 1D;
        double newPosY = player.getY();
        double newPosX = player.getX() - (dist * Mth.cos((MoCTools.realAngle(player.getYRot() - 90F)) / 57.29578F));
        double newPosZ = player.getZ() - (dist * Mth.sin((MoCTools.realAngle(player.getYRot() - 90F)) / 57.29578F));

        ItemStack emptyAmulet = new ItemStack(MoCItems.FISH_NET.get(), 1);
        if (this.amuletType == 1) {
            emptyAmulet = new ItemStack(MoCItems.PET_AMULET.get(), 1);
        }

        if (!level.isClientSide()) {
            initAndReadNBT(stack);
            if (this.spawnClass.isEmpty()) {
                return InteractionResultHolder.success(stack);
            }

            try {
                String formattedClass = this.spawnClass.replace(MoCConstants.MOD_PREFIX, "").toLowerCase();
                switch (formattedClass) {
                    case "mochorse" -> formattedClass = "wildhorse";
                    case "polarbear" -> formattedClass = "wildpolarbear";
                    case "petmanticore" -> formattedClass = "manticorepet";
                    case "ray" -> {
                        if (creatureType == 1) formattedClass = "mantaray";
                        else if (creatureType == 2) formattedClass = "stingray";
                    }
                }

                Mob entity = (Mob) EntityType.byString(MoCConstants.MOD_PREFIX + formattedClass).orElseThrow().create(level);
                if (entity instanceof IMoCTameable storedCreature) {
                    entity.setPos(newPosX, newPosY, newPosZ);
                    storedCreature.setTypeMoC(creatureType);
                    storedCreature.setTamed(true);
                    storedCreature.setPetName(name);
                    storedCreature.setOwnerPetId(PetId);
                    storedCreature.setOwnerId(player.getUUID());
                    ownerName = player.getName().getString();
                    entity.setHealth(health);
                    // Set adult status BEFORE age to prevent automatic adult detection conflicts
                    storedCreature.setAdult(adult);
                    storedCreature.setMoCAge(age);

                    if (entity instanceof MoCEntityBigCat cat) cat.setHasAmulet(true);
                    if (formattedClass.equals("kitty")) ((MoCEntityKitty) entity).setKittyState(3);

                    UUID newOwnerUUID = player.getUUID();
                    if (ownerUniqueId == null) {
                        ownerUniqueId = newOwnerUUID;
                        maybeRegisterPet(storedCreature, player);
                    } else if (!ownerUniqueId.equals(newOwnerUUID)) {
                        transferOwnership(storedCreature, player);
                    }

                    if (level.addFreshEntity(entity)) {
                        MoCMessageHandler.sendToNear(level, player.getX(), player.getY(), player.getZ(), 64, new MoCMessageAppear(entity.getId()));

                        if (ownerUniqueId == null || name.isEmpty()) {
                            MoCTools.tameWithName(player, storedCreature);
                        }

                        player.setItemInHand(hand, emptyAmulet);
                        MoCPetData petData = MoCreatures.instance.mapData.getPetData(storedCreature.getOwnerId());
                        if (petData != null) {
                            petData.setInAmulet(storedCreature.getOwnerPetId(), false);
                        }
                    }
                }
            } catch (Exception e) {
                if (MoCreatures.proxy.debug) {
                    MoCreatures.LOGGER.warn("Error spawning creature from amulet: ", e);
                }
            }
        }

        return InteractionResultHolder.success(stack);
    }

    private void maybeRegisterPet(IMoCTameable creature, Player player) {
        if (MoCreatures.instance.mapData == null) return;

        MoCPetData data = MoCreatures.instance.mapData.getPetData(player.getUUID());
        int max = MoCreatures.proxy.maxTamed;
        if (MoCTools.isThisPlayerAnOP(player)) max = MoCreatures.proxy.maxOPTamed;

        if (data == null) {
            if (max > 0 || !MoCreatures.proxy.enableOwnership) {
                MoCreatures.instance.mapData.updateOwnerPet(creature);
            }
        } else if (data.getTamedList().size() < max || !MoCreatures.proxy.enableOwnership) {
            MoCreatures.instance.mapData.updateOwnerPet(creature);
        }
    }

    private void transferOwnership(IMoCTameable creature, Player player) {
        if (MoCreatures.instance.mapData == null) return;

        UUID oldOwner = this.ownerUniqueId;
        UUID newOwner = player.getUUID();
        MoCPetData oldData = MoCreatures.instance.mapData.getPetData(oldOwner);
        MoCPetData newData = MoCreatures.instance.mapData.getPetData(newOwner);
        int max = MoCreatures.proxy.maxTamed;
        if (MoCTools.isThisPlayerAnOP(player)) max = MoCreatures.proxy.maxOPTamed;

        if ((newData != null && newData.getTamedList().size() < max) || (newData == null && max > 0) || !MoCreatures.proxy.enableOwnership) {
            MoCreatures.instance.mapData.updateOwnerPet(creature);
        }

        if (oldData != null) {
            // TODO: What the fuck
            //oldData.getTamedList().removeIf(nbt -> nbt.getType("PetId") == this.PetId);
        }
    }

    public void readFromNBT(CompoundTag nbt) {
        this.PetId = nbt.getIntOr("PetId", 0);
        this.creatureType = nbt.getIntOr("CreatureType", 0);
        this.health = nbt.getFloatOr("Health", 0.0F);
        this.age = nbt.getIntOr("Edad", 0);
        this.name = nbt.getStringOr("Name", "");
        this.spawnClass = nbt.getStringOr("SpawnClass", "");
        this.adult = nbt.getBooleanOr("Adult", false);
        this.ownerName = nbt.getStringOr("OwnerName", "");
        if (nbt.hasUUID("OwnerUUID")) {
            this.ownerUniqueId = nbt.getUUID("OwnerUUID");
        }
    }

    public void writeToNBT(CompoundTag nbt) {
        nbt.putInt("PetId", this.PetId);
        nbt.putInt("CreatureType", this.creatureType);
        nbt.putFloat("Health", this.health);
        nbt.putInt("Edad", this.age);
        nbt.putString("Name", this.name);
        nbt.putString("SpawnClass", this.spawnClass);
        nbt.putBoolean("Adult", this.adult);
        nbt.putString("OwnerName", this.ownerName);
        if (this.ownerUniqueId != null) {
            nbt.putUUID("OwnerUUID", ownerUniqueId);
        }
    }

    private void initAndReadNBT(ItemStack stack) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        readFromNBT(stack.getTag());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        initAndReadNBT(stack);
        if (!spawnClass.isEmpty()) tooltip.add(Component.literal(spawnClass).withStyle(ChatFormatting.AQUA));
        if (!name.isEmpty()) tooltip.add(Component.literal(name).withStyle(ChatFormatting.BLUE));
        if (!ownerName.isEmpty()) tooltip.add(Component.literal("Owned by " + ownerName).withStyle(ChatFormatting.DARK_BLUE));
    }
}
