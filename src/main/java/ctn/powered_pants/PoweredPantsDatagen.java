package ctn.powered_pants;

import ctn.powered_pants.datagen.DatagenI18ZhCn;
import ctn.powered_pants.datagen.DatagenItemModel;
import ctn.powered_pants.datagen.PoweredPantsTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = PoweredPants.MOD_ID)
public class PoweredPantsDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper exFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		// 客户端数据生成
		generator.addProvider(event.includeClient(), new DatagenI18ZhCn(output));
		generator.addProvider(event.includeClient(), new DatagenItemModel(output, exFileHelper));
		PoweredPantsTags.PmBlock pmPmBlockTags = new PoweredPantsTags.PmBlock(output, lookupProvider, exFileHelper);
		generator.addProvider(event.includeClient(), pmPmBlockTags);
		generator.addProvider(event.includeClient(), new PoweredPantsTags.PmItem(output, lookupProvider, pmPmBlockTags.contentsGetter(), exFileHelper));

		// 服务端数据生成

	}
}
