package drzhark.mocreatures.item;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.server.network.Filterable;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import drzhark.mocreatures.MoCreatures;

import drzhark.mocreatures.init.MoCItems;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ItemHorseGuide extends Item {

	public ItemHorseGuide(Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (level.isClientSide()) {
			// Create a temporary written book stack with our content
			ItemStack bookStack = createBookStack();
			MoCreatures.proxy.openBook(bookStack);
		}
		return InteractionResult.SUCCESS;
	}


	/**
	 * Creates a properly formatted written book ItemStack for 1.20.1
	 */
	private ItemStack createBookStack() {
		ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
		List<Filterable<Component>> pages = new ArrayList<>();

		// Page 1 - Welcome
		pages.add(Filterable.passThrough(Component.literal(
				"Welcome to the Mo' Creatures Horse Guide!\n\n" +
						"Learn detailed methods to breed and raise all horses, special breeds, and mythical creatures.\n\n"
						+
						"Use this guide to master horse breeding in Mo' Creatures!")));

		// Page 2 - Tier 1 Horses (Breed Only)
		pages.add(Filterable.passThrough(Component.literal(
				"§lTier 1 Horses§r\n(Breed Only)\n\n" +
						"§6Vanilla:§r Natural spawn\n\n" +
						"§6White/Light Grey:§r\nWhite + Pegasus/Fairy/Unicorn/Nightmare\n\n" +
						"§6Buckskin:§r\nPalomino Snowflake + Bay/Brown")));

		// Page 3 - Tier 1 continued
		pages.add(Filterable.passThrough(Component.literal(
				"§lTier 1 (continued)§r\n\n" +
						"§6Blood Bay:§r\nDark Brown/Bay + Buckskin\n\n" +
						"§6Mahogany/Dark Bay:§r\nGrulla Overo + Black Horse\n\n" +
						"§6Black:§r\nNightmare/Fairy/Unicorn/Pegasus + Black Horse")));

		// Page 4 - Tier 2 Horses
		pages.add(Filterable.passThrough(Component.literal(
				"§lTier 2 Horses§r\n\n" +
						"§6Palomino Snowflake:§r (wild spawn)\n\n" +
						"§6Grulla Overo/Grullo:§r (wild spawn)\n\n" +
						"§6Bay:§r (wild spawn)\n\n" +
						"§6Dappled Grey:§r\nGrey Spotted + Pegasus/Unicorn/Nightmare/Fairy")));

		// Page 5 - Tier 3 Horses
		pages.add(Filterable.passThrough(Component.literal(
				"§lTier 3 Horses§r\n\n" +
						"§6Bay Tovero:§r\nGrulla Overo + Brown Spotted/Blood Bay\n\n" +
						"§6Palomino Tovero:§r\nGrulla Overo + White/Light Grey\n\n" +
						"§6Grulla/Grullo Tovero:§r\nDappled Grey + White/Light Grey")));

		// Page 6 - Tier 4 Horses
		pages.add(Filterable.passThrough(Component.literal(
				"§lTier 4 Horses§r\n(Zebra Taming)\n\n" +
						"§6Black Leopard:§r\nGrulla Tovero + Black\n\n" +
						"§6Black Tovero:§r\nBay Tovero + Black\n\n" +
						"§6These horses can tame zebras!§r")));

		// Page 7 - Unique Horses
		pages.add(Filterable.passThrough(Component.literal(
				"§lUnique Horses§r\n\n" +
						"§6Zebra:§r Chest Carrier\n\n" +
						"§6Zorse:§r Sterile hybrid\n\n" +
						"§6Zonkey:§r Sterile, Chest Carrier\n\n" +
						"§6Mule:§r Sterile, Chest Carrier")));

		// Page 8 - Mythical Horses
		pages.add(Filterable.passThrough(Component.literal(
				"§lMythical Horses§r\n\n" +
						"§4Nightmare:§r No Armor\n\n" +
						"§3Unicorn:§r Can wear armor\n\n" +
						"§6Pegasus:§r Can wear armor, flies\n\n" +
						"§8Bat Horse:§r No Armor, night creature")));

		// Page 9 - Advanced Mythicals
		pages.add(Filterable.passThrough(Component.literal(
				"§lAdvanced Mythicals§r\n\n" +
						"§5Dark Pegasus:§r\nChest Carrier, powerful\n\n" +
						"§dFairy Horse:§r\nChest Carrier, Can wear armor, magical abilities")));

		// Page 10 - Undead & Skeleton
		pages.add(Filterable.passThrough(Component.literal(
				"§lUndead & Skeleton§r\n\n" +
						"§8Created:§r Essence of Undead\n\n" +
						"§8Decays:§r Eventually becomes Skeleton\n\n" +
						"§8Heal/Restore:§r Essence of Light/Undead\n\n" +
						"§8Cannot wear armor§r")));

		// Page 11 - Breeding & Care Tips
		pages.add(Filterable.passThrough(Component.literal(
				"§lBreeding & Care Tips§r\n\n" +
						"• Enclose horses in fenced areas\n" +
						"• Feed Bread/Sugar to grow foals faster\n" +
						"• Keep chunks loaded for breeding\n" +
						"• Only tamed horses will breed\n" +
						"• Be patient - breeding takes time!")));

		// Page 12 - Final Tips
		pages.add(Filterable.passThrough(Component.literal(
				"§lFinal Tips§r\n\n" +
						"§6Remember:§r\n" +
						"• Mythical horses are rare and valuable\n" +
						"• Some breeds require specific combinations\n" +
						"• Experiment with different pairings\n\n" +
						"§lGood luck with your horse breeding adventures!§r")));

		book.set(DataComponents.WRITTEN_BOOK_CONTENT,
				new WrittenBookContent(Filterable.passThrough("Mo' Creatures Horse Guide"), "forgoted", 0, pages, true));
		return book;
	}

	public static ItemStack createDisplayStack() {
		ItemStack stack = new ItemStack(MoCItems.HORSE_GUIDE.get());
		stack.set(DataComponents.CUSTOM_NAME, Component.literal("Mo' Creatures Horse Guide").withStyle(st -> st.withItalic(false)));
		stack.set(DataComponents.LORE, new ItemLore(List.of(
				Component.literal("by forgoted").withStyle(st -> st.withColor(0x999999).withItalic(false)))));
		return stack;
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
}
