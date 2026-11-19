package drzhark.mocreatures.init;

import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber(modid = MoCConstants.MOD_ID)
public class MoCVillagerTrades {

    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        var trades = event.getTrades();

        if (VillagerProfession.BUTCHER.equals(event.getType())) {
            trades.get(1).add((e, r) -> offer(new ItemStack(MoCItems.DUCK_RAW.get(), 14 + r.nextInt(5)), new ItemStack(Items.EMERALD), 10, 2, 0.05F));
            trades.get(1).add((e, r) -> offer(new ItemStack(MoCItems.OSTRICH_RAW.get(), 10 + r.nextInt(3)), new ItemStack(Items.EMERALD), 10, 2, 0.05F));
            trades.get(2).add((e, r) -> offer(new ItemStack(Items.EMERALD), new ItemStack(MoCItems.DUCK_COOKED.get(), 6 + r.nextInt(3)), 10, 5, 0.05F));
            trades.get(2).add((e, r) -> offer(new ItemStack(Items.EMERALD), new ItemStack(MoCItems.OSTRICH_COOKED.get(), 5 + r.nextInt(3)), 10, 5, 0.05F));
        }

        if (VillagerProfession.CLERIC.equals(event.getType())) {
            trades.get(1).add((e, r) -> offer(new ItemStack(MoCItems.ANCIENTSILVERSCRAP.get(), 4 + r.nextInt(3)), new ItemStack(Items.EMERALD), 10, 2, 0.05F));
            trades.get(4).add((e, r) -> offer(new ItemStack(Items.EMERALD), new ItemStack(MoCItems.MYSTIC_PEAR.get(), 2 + r.nextInt(2)), 3, 15, 0.1F));
        }

        if (VillagerProfession.FISHERMAN.equals(event.getType())) {
            trades.get(1).add((e, r) -> offer(new ItemStack(Items.EMERALD, 2 + r.nextInt(3)), new ItemStack(MoCItems.FISH_NET.get()), 10, 5, 0.05F));
            trades.get(2).add((e, r) -> offer(new ItemStack(Items.EMERALD), new ItemStack(MoCItems.CRAB_COOKED.get(), 6 + r.nextInt(3)), 10, 5, 0.05F));
        }

        if (VillagerProfession.LEATHERWORKER.equals(event.getType())) {
            trades.get(1).add((e, r) -> offer(new ItemStack(MoCItems.FUR.get(), 9 + r.nextInt(4)), new ItemStack(Items.EMERALD), 10, 2, 0.05F));
            trades.get(1).add((e, r) -> offer(new ItemStack(MoCItems.ANIMALHIDE.get(), 6 + r.nextInt(4)), new ItemStack(Items.EMERALD), 10, 2, 0.05F));
        }

        if (VillagerProfession.LIBRARIAN.equals(event.getType())) {
            trades.get(1).add((e, r) -> new MerchantOffer(new ItemStack(Items.PAPER), new ItemStack(Items.FEATHER), new ItemStack(MoCItems.SCROLLFREEDOM.get()), 12, 10, 0.1F));
        }

        if (VillagerProfession.TOOLSMITH.equals(event.getType()) ||
                VillagerProfession.WEAPONSMITH.equals(event.getType()) ||
                VillagerProfession.ARMORER.equals(event.getType())) {
            trades.get(2).add((e, r) -> offer(new ItemStack(Items.EMERALD, 4 + r.nextInt(3)), new ItemStack(MoCItems.ANCIENTSILVERINGOT.get()), 10, 5, 0.05F));
            trades.get(2).add((e, r) -> offer(new ItemStack(MoCItems.ANCIENTSILVERINGOT.get(), 3 + r.nextInt(2)), new ItemStack(Items.EMERALD), 10, 5, 0.05F));
        }
    }

    private static MerchantOffer offer(ItemStack in, ItemStack out, int maxUses, int xp, float priceMult) {
        return new MerchantOffer(in, out, maxUses, xp, priceMult);
    }
}
