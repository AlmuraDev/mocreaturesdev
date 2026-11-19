package drzhark.mocreatures.item;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.entity.item.MoCEntityLitterBox;
import drzhark.mocreatures.init.MoCEntities;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;

public class MoCItemLitterBox extends MoCItem {

    public MoCItemLitterBox(Item.Properties properties) {
        super(properties.stacksTo(16));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            MoCEntityLitterBox litterBox = MoCEntities.LITTERBOX.get().create(level);
            if (litterBox != null) {
                litterBox.setPos(player.getX(), player.getY(), player.getZ());
                level.addFreshEntity(litterBox);
                MoCTools.playCustomSound(litterBox, SoundEvents.WOOD_PLACE);
            }
        }
        return InteractionResultHolder.success(stack);
    }
}
