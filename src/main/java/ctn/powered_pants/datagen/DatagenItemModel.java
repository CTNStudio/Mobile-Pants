package ctn.powered_pants.datagen;

import ctn.powered_pants.init.PoweredPantsItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static ctn.powered_pants.PoweredPants.MOD_ID;

public class DatagenItemModel extends ItemModelProvider {
	public DatagenItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		basicItem(PoweredPantsItems.IRON_POWERED_PANTS.get());
		basicItem(PoweredPantsItems.NETHERITE_POWERED_PANTS.get());
		basicItem(PoweredPantsItems.LEATHER_POWERED_PANTS.get());
		basicItem(PoweredPantsItems.GOLD_POWERED_PANTS.get());
		basicItem(PoweredPantsItems.DIAMOND_POWERED_PANTS.get());
	}
}
