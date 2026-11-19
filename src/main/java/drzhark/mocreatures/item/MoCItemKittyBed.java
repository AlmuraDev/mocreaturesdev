package drzhark.mocreatures.item;
import net.minecraft.world.entity.EntitySpawnReason;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.entity.item.MoCEntityKittyBed;
import drzhark.mocreatures.init.MoCEntities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;

public class MoCItemKittyBed extends MoCItem {

    private int sheetType;

    public MoCItemKittyBed(Item.Properties properties) {
        super(properties);
    }

    public MoCItemKittyBed(Item.Properties properties, int type) {
        this(properties);
        this.sheetType = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (!player.isCreative()) stack.shrink(1);

            MoCEntityKittyBed kittyBed = MoCEntities.KITTY_BED.get().create(level, EntitySpawnReason.SPAWN_ITEM_USE);
            if (kittyBed != null) {
                kittyBed.setSheetColor(this.sheetType);
                kittyBed.setPos(player.getX(), player.getY(), player.getZ());
                level.addFreshEntity(kittyBed);
                MoCTools.playCustomSound(kittyBed, SoundEvents.WOOD_PLACE);
            }
        }

        return InteractionResultHolder.success(stack);
    }
}
