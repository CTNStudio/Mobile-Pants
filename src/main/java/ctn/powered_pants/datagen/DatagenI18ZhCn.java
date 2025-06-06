package ctn.powered_pants.datagen;

import ctn.powered_pants.init.PoweredPantsItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

import static ctn.powered_pants.PoweredPants.MOD_ID;
import static ctn.powered_pants.init.PoweredPantsModeTab.POWERED_PANTS_TAB;

public class DatagenI18ZhCn extends LanguageProvider {
	public DatagenI18ZhCn(PackOutput output) {
		super(output, MOD_ID, "zh_cn");
	}

	@Override
	protected void addTranslations() {
		add(POWERED_PANTS_TAB, "动力机动裤");
		add(PoweredPantsItems.LEATHER_POWERED_PANTS.get(), "皮质动力机动裤");
		add(PoweredPantsItems.IRON_POWERED_PANTS.get(), "铁质动力机动裤");
		add(PoweredPantsItems.GOLD_POWERED_PANTS.get(), "金质动力机动裤");
		add(PoweredPantsItems.DIAMOND_POWERED_PANTS.get(), "钻石质动力机动裤");
		add(PoweredPantsItems.NETHERITE_POWERED_PANTS.get(), "下界合金质动力机动裤");
	}

	/**
	 * 创造模式物品栏名称翻译
	 */
	private void add(DeferredHolder<CreativeModeTab, CreativeModeTab> itemGroup, String name) {
		super.add("itemGroup." + itemGroup.getId().toString().replace(":", "."), name);
	}
}
