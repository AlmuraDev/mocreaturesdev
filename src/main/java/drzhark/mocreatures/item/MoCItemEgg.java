package drzhark.mocreatures.item;
import drzhark.mocreatures.init.MoCDataComponents;
import net.minecraft.world.entity.EntitySpawnReason;

import drzhark.mocreatures.entity.item.MoCEntityEgg;
import drzhark.mocreatures.init.MoCEntities;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class MoCItemEgg extends MoCItem {

    public MoCItemEgg(Item.Properties properties) {
        super(properties.stacksTo(16));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isCreative()) {
            stack.shrink(1);
        }

        if (!level.isClientSide() && player.onGround()) {
            int eggType = stack.getOrDefault(MoCDataComponents.EGG_TYPE.get(), 0);
            if (eggType == 30) eggType = 31; // Ostrich => Stolen Ostrich

            MoCEntityEgg entityEgg = MoCEntities.EGG.get().create(level, EntitySpawnReason.SPAWN_ITEM_USE);
            if (entityEgg != null) {
                entityEgg.setEggType(eggType);
                entityEgg.setPos(player.getX(), player.getY(), player.getZ());
                entityEgg.setDeltaMovement(
                        (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.3F,
                        level.getRandom().nextFloat() * 0.05F,
                        (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.3F
                );
                level.addFreshEntity(entityEgg);
                System.out.println("[DEBUG] Placing egg with type: " + eggType);
            }
        }

        return InteractionResultHolder.success(stack);
    }

    public void fillItemCategory(CreativeModeTab.Output output) {
        // Add all the egg variants to the creative tab
        addRange(output, 0, 10);   // Fishies
        addSingle(output, 11);     // Shark
        addRange(output, 21, 28);  // Snakes
        addList(output, 30, 31);   // Ostriches
        addSingle(output, 33);     // Komodo
        addRange(output, 41, 45);  // Scorpions
        addRange(output, 50, 61);  // Wyverns
        addRange(output, 62, 66);  // Manticores
        addRange(output, 70, 72);  // Medium Fish
        addRange(output, 80, 86);  // Small Fish
        addSingle(output, 90);     // Piranha
    }

    private void addSingle(CreativeModeTab.Output list, int type) {
        ItemStack stack = new ItemStack(this);
        stack.set(MoCDataComponents.EGG_TYPE.get(), type);
        list.accept(stack);
    }

    private void addRange(CreativeModeTab.Output list, int start, int end) {
        for (int i = start; i <= end; i++) {
            addSingle(list, i);
        }
    }

    private void addList(CreativeModeTab.Output list, int... values) {
        for (int value : values) {
            addSingle(list, value);
        }
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        int eggType = stack.getOrDefault(MoCDataComponents.EGG_TYPE.get(), 0);
        return super.getDescriptionId(stack) + "." + eggType;
    }
}
