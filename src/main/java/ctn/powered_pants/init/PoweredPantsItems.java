package ctn.powered_pants.init;

import ctn.powered_pants.common.item.velocity_vault_exoframe.*;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.BiFunction;

import static ctn.powered_pants.PoweredPants.MOD_ID;
import static net.minecraft.world.item.ArmorMaterials.*;

public class PoweredPantsItems {
	public static final DeferredRegister.Items ITEM_REGISTER = DeferredRegister.createItems(MOD_ID);

	public static final DeferredItem<PoweredPantsItem> LEATHER_POWERED_PANTS =
			createPoweredPants("leather_powered_pants", LeatherPoweredPantsItem::new, LEATHER,
					new Item.Properties().durability(75));

	public static final DeferredItem<PoweredPantsItem> IRON_POWERED_PANTS =
			createPoweredPants("iron_powered_pants", IronPoweredPantsItem::new, IRON,
					new Item.Properties().durability(225));

	public static final DeferredItem<PoweredPantsItem> GOLD_POWERED_PANTS =
			createPoweredPants("gold_powered_pants", GoldPoweredPantsItem::new, GOLD,
					new Item.Properties().durability(105));

	public static final DeferredItem<PoweredPantsItem> DIAMOND_POWERED_PANTS =
			createPoweredPants("diamond_powered_pants", DiamondPoweredPantsItem::new, DIAMOND,
					new Item.Properties().durability(495));

	public static final DeferredItem<PoweredPantsItem> NETHERITE_POWERED_PANTS =
			createPoweredPants("netherite_powered_pants", NetheritePoweredPantsItem::new, NETHERITE,
					new Item.Properties().durability(555));

	public static void init() {
		for (DeferredItem<PoweredPantsItem> itemRegistryObject : List.of(
				LEATHER_POWERED_PANTS,
				IRON_POWERED_PANTS,
				GOLD_POWERED_PANTS,
				DIAMOND_POWERED_PANTS,
				NETHERITE_POWERED_PANTS
		)) {
			itemRegistryObject.get().initJumpConfing();
		}
	}

	private static <T extends PoweredPantsItem> DeferredItem<T> createPoweredPants(String name, BiFunction<Holder<ArmorMaterial>, Item.Properties, T> item, Holder<ArmorMaterial> material, Item.Properties properties) {
		return ITEM_REGISTER.register(name, () -> item.apply(material, properties));
	}
}
