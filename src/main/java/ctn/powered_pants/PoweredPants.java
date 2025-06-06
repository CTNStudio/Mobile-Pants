package ctn.powered_pants;

import com.mojang.logging.LogUtils;
import ctn.powered_pants.config.PoweredPantsConfig;
import ctn.powered_pants.init.PoweredPantsItems;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import static ctn.powered_pants.init.PoweredPantsItems.ITEM_REGISTER;
import static ctn.powered_pants.init.PoweredPantsModeTab.PROJECT_MOON_TAB_REGISTER;
import static ctn.powered_pants.init.PoweredPantsSoundEvent.SOUND_EVENTS;

@Mod(PoweredPants.MOD_ID)
public class PoweredPants {
	public static final String MOD_ID = "powered_pants";
	public static final Logger LOGGER = LogUtils.getLogger();

	public PoweredPants(IEventBus modEventBus, ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.COMMON, PoweredPantsConfig.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		ITEM_REGISTER.register(modEventBus);
		PROJECT_MOON_TAB_REGISTER.register(modEventBus);
		SOUND_EVENTS.register(modEventBus);
		modEventBus.addListener(this::commonSetup);
		NeoForge.EVENT_BUS.register(this);
	}

	private static void init() {
        PoweredPantsItems.init();
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		LOGGER.info("HELLO FROM COMMON SETUP");
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		LOGGER.info("HELLO from server starting");
	}

	@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			LOGGER.info("HELLO FROM CLIENT SETUP");
			LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
		}
	}

	@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
	public static class ModEventHandlera {
		@SubscribeEvent
		public static void onConfigLoaded(final ModConfigEvent.Loading event) {
			if (event.getConfig().getSpec() == PoweredPantsConfig.SPEC) {
				init();
			}
		}

		@SubscribeEvent
		public static void onConfigReloading(final ModConfigEvent.Reloading event) {
			if (event.getConfig().getSpec() == PoweredPantsConfig.SPEC) {
				init();
			}
		}
	}
}
