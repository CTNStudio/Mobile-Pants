package ctn.powered_pants.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static ctn.powered_pants.PoweredPants.MOD_ID;


public class PoweredPantsModeTab extends CreativeModeTabs {
	public static final DeferredRegister<CreativeModeTab> PROJECT_MOON_TAB_REGISTER =
			DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> POWERED_PANTS_TAB =
			register(
					"powere_dpants", (name) -> createCreativeModeTab(
							name, (displayParameters, output) -> {
								output.accept(PoweredPantsItems.LEATHER_POWERED_PANTS.get());
								output.accept(PoweredPantsItems.IRON_POWERED_PANTS.get());
								output.accept(PoweredPantsItems.GOLD_POWERED_PANTS.get());
								output.accept(PoweredPantsItems.DIAMOND_POWERED_PANTS.get());
								output.accept(PoweredPantsItems.NETHERITE_POWERED_PANTS.get());
							}, () -> PoweredPantsItems.DIAMOND_POWERED_PANTS.get().getDefaultInstance()));

	private static DeferredHolder<CreativeModeTab, CreativeModeTab> register(String name, Function<String, CreativeModeTab.Builder> builder) {
		return PROJECT_MOON_TAB_REGISTER.register(name, builder.apply(name)::build);
	}

	private static CreativeModeTab.Builder createCreativeModeTab(
			String name,
			CreativeModeTab.DisplayItemsGenerator displayItemsGenerator,
			Supplier<ItemStack> icon) {
		return createCreativeModeTab(name, displayItemsGenerator).icon(icon);
	}

	private static CreativeModeTab.Builder createCreativeModeTab(String name, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
		return CreativeModeTab.builder().title(getComponent(name)).displayItems(displayItemsGenerator);
	}

	private static @NotNull MutableComponent getComponent(String imagePath) {
		return Component.translatable("itemGroup." + MOD_ID + "." + imagePath);
	}
}
