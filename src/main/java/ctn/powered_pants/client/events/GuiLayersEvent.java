package ctn.powered_pants.client.events;


import ctn.powered_pants.client.gui.PoweredPantsDraw;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

import static ctn.powered_pants.PoweredPants.MOD_ID;
import static net.neoforged.neoforge.client.gui.VanillaGuiLayers.SELECTED_ITEM_NAME;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GuiLayersEvent {
	@SubscribeEvent
	public static void registerGuiLayersEvent(RegisterGuiLayersEvent event) {
		event.registerBelow(SELECTED_ITEM_NAME, ResourceLocation.fromNamespaceAndPath(MOD_ID, "powered_pants_draw"), (guiGraphics, deltaTracker) -> new PoweredPantsDraw(guiGraphics, deltaTracker, Minecraft.getInstance()));
	}
}
