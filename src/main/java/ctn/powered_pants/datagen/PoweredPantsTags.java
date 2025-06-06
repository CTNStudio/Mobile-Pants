package ctn.powered_pants.datagen;

import ctn.powered_pants.init.PoweredPantsItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import static ctn.powered_pants.PoweredPants.MOD_ID;

public class PoweredPantsTags {
	public static class PmBlock extends BlockTagsProvider {
		public PmBlock(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, MOD_ID, existingFileHelper);
		}

		@Override
		protected void addTags(HolderLookup.@NotNull Provider provider) {

		}
	}

	public static class PmItem extends ItemTagsProvider {
		public PmItem(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<net.minecraft.world.level.block.Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, blockTags, MOD_ID, existingFileHelper);
		}

		@Override
		protected void addTags(HolderLookup.@NotNull Provider provider) {
			tag(ItemTags.LEG_ARMOR).add(
					PoweredPantsItems.IRON_POWERED_PANTS.get(),
					PoweredPantsItems.NETHERITE_POWERED_PANTS.get(),
					PoweredPantsItems.LEATHER_POWERED_PANTS.get(),
					PoweredPantsItems.GOLD_POWERED_PANTS.get(),
					PoweredPantsItems.DIAMOND_POWERED_PANTS.get());

			tag(ItemTags.LEG_ARMOR_ENCHANTABLE).add(
					PoweredPantsItems.IRON_POWERED_PANTS.get(),
					PoweredPantsItems.NETHERITE_POWERED_PANTS.get(),
					PoweredPantsItems.LEATHER_POWERED_PANTS.get(),
					PoweredPantsItems.GOLD_POWERED_PANTS.get(),
					PoweredPantsItems.DIAMOND_POWERED_PANTS.get());
		}
	}
}
