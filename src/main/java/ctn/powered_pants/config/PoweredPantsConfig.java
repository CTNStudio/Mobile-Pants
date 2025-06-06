package ctn.powered_pants.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.NoSuchElementException;

import static com.mojang.text2speech.Narrator.LOGGER;
import static ctn.powered_pants.PoweredPants.MOD_ID;
import static net.neoforged.neoforge.common.ModConfigSpec.*;

public class PoweredPantsConfig {
	public static final PoweredPantsConfig CONFIG;
	public static final ModConfigSpec      SPEC;

	static {
		Pair<PoweredPantsConfig, ModConfigSpec> pair = new Builder().configure(PoweredPantsConfig::new);
		CONFIG = pair.getLeft();
		SPEC   = pair.getRight();
	}

	// LEATHER
	public final IntValue            LEATHER_MAX_JUMP_TIME; // 最大跳跃时间
	public final ConfigValue<String> LEATHER_MAX_JUMP_ANGLE; // 最大跳跃角度
	// IRON
	public final IntValue            IRON_MAX_JUMP_TIME; // 最大跳跃时间
	public final ConfigValue<String> IRON_MAX_JUMP_ANGLE; // 最大跳跃角度
	// GOLD
	public final IntValue            GOLD_MAX_JUMP_TIME; // 最大跳跃时间
	public final ConfigValue<String> GOLD_MAX_JUMP_ANGLE; // 最大跳跃角度
	// DIAMOND
	public final IntValue            DIAMOND_MAX_JUMP_TIME; // 最大跳跃时间
	public final ConfigValue<String> DIAMOND_MAX_JUMP_ANGLE; // 最大跳跃角度
	// NETHERITE
	public final IntValue            NETHERITE_MAX_JUMP_TIME; // 最大跳跃时间
	public final ConfigValue<String> NETHERITE_MAX_JUMP_ANGLE; // 最大跳跃角度

	// 在某些配置类中
	PoweredPantsConfig(Builder builder) {
		builder.push("PoweredPants");
		LEATHER_MAX_JUMP_TIME = builderConfig(builder, "最大跳跃时间", "leather_max_jump_time", 100);
		LEATHER_MAX_JUMP_ANGLE = builderConfig(builder, "最大跳跃角度", "leather_max_jump_angle", 45.0F);
		IRON_MAX_JUMP_TIME = builderConfig(builder, "最大跳跃时间", "iron_max_jump_time", 200);
		IRON_MAX_JUMP_ANGLE = builderConfig(builder, "最大跳跃角度", "iron_max_jump_angle", 50.0F);
		GOLD_MAX_JUMP_TIME = builderConfig(builder, "最大跳跃时间", "gold_max_jump_time", 300);
		GOLD_MAX_JUMP_ANGLE = builderConfig(builder, "最大跳跃角度", "gold_max_jump_angle", 55.0F);
		DIAMOND_MAX_JUMP_TIME = builderConfig(builder, "最大跳跃时间", "diamond_max_jump_time", 400);
		DIAMOND_MAX_JUMP_ANGLE = builderConfig(builder, "最大跳跃角度", "diamond_max_jump_angle", 60.0F);
		NETHERITE_MAX_JUMP_TIME = builderConfig(builder, "最大跳跃时间", "netherite_max_jump_time", 500);
		NETHERITE_MAX_JUMP_ANGLE = builderConfig(builder, "最大跳跃角度", "netherite_max_jump_angle", 65.0F);
		builder.pop();
	}

	private static IntValue builderConfig(Builder builder, String comment, String translation, int defaultValue, int minValue, int maxValue) {
		return builder.translation(translationKey(translation)).worldRestart().comment(comment).defineInRange(translation, defaultValue, minValue, maxValue);
	}

	private static IntValue builderConfig(Builder builder, String comment, String translation, int defaultValue) {
		return builderConfig(builder, comment, translation, defaultValue, 1, Integer.MAX_VALUE);
	}

	private static ConfigValue<String> builderConfig(Builder builder, String comment, String translation, String materialName) {
		return builder.translation(translationKey(translation)).worldRestart().comment(comment).define(translation, materialName);
	}

	private static ConfigValue<String> builderConfig(Builder builder, String comment, String translation, float materialName) {
		return builderConfig(builder, comment, translation, String.valueOf(materialName));
	}

	private static ConfigValue<String> builderConfig(Builder builder, String comment, String translation) {
		return builderConfig(builder, comment, translation, "minecraft:copper_ingot");
	}

	public static String translationKey(String string) {
		return MOD_ID + ".configgui." + string;
	}

	public static String commentKey(String string) {
		return MOD_ID + ".configgui." + string + ".tooltip";
	}

	public static ResourceLocation getItemResourceLocation(String configValue) {
		String[] split = configValue.split(":");
		return ResourceLocation.fromNamespaceAndPath(split[0], split[1]);
	}

	public static JsonElement getItemJson(String configValue) {
		if (configValue == null) {
			return null;
		}
		ResourceLocation resourceLocation = getItemResourceLocation(configValue);
		Reader reader = null;
		try {
			reader = new InputStreamReader(Minecraft.getInstance().getResourceManager().getResource(resourceLocation).orElseThrow().open());
			return JsonParser.parseReader(reader).getAsJsonObject();
		} catch (IOException e) {
			LOGGER.error("Failed to read resource: {}", resourceLocation);
		} catch (JsonParseException e) {
			LOGGER.error("Failed to parse JSON from resource: {}", resourceLocation);
		} catch (NoSuchElementException e) {
			LOGGER.error("Failed to get resource: {}", resourceLocation);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOGGER.error("Failed to close reader for resource: {}", resourceLocation);
				}
			}
		}
		return null;
	}
}
