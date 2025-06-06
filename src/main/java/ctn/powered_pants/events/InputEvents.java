package ctn.powered_pants.events;

import com.mojang.blaze3d.platform.InputConstants;
import ctn.powered_pants.common.item.velocity_vault_exoframe.PoweredPantsItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static ctn.powered_pants.PoweredPants.MOD_ID;
import static ctn.powered_pants.init.PoweredPantsSoundEvent.PROMPT;
import static net.minecraft.sounds.SoundEvents.CANDLE_EXTINGUISH;
import static net.minecraft.sounds.SoundSource.PLAYERS;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

@EventBusSubscriber(modid = MOD_ID)
public class InputEvents {
	public static final String POWERED_PANTS_JUMP_TICK = "powered_pants_jump_tick";
	public static final String POWERED_PANTS_JUMP      = "powered_pants_jump";
	public static final String POWERED_PANTS_JUMP_MODE = "powered_pants_jump_mode";
	public static final String POWERED_PANTS_JUMPED_ALREADY = "powered_pants_jumped_already";
	public static final String POWERED_PANTS_JUMPED_ALREADY_TICK = "powered_pants_jumped_already_tick";

	@SubscribeEvent
	public static void tickEventHandler(PlayerTickEvent.Pre event) {
		velocityVaultExoframeJump(event);
	}

	// velocityVaultExoframe 跳跃
	private static void velocityVaultExoframeJump(PlayerTickEvent.Pre event) {
		final Player player = event.getEntity();
		final CompoundTag nbt = player.getPersistentData();
		if (player instanceof ServerPlayer || !player.isAlive()) {
			return;
		}

		// 检测是否在打开了屏幕
		if (Minecraft.getInstance().screen != null) {
			nbt.putInt(POWERED_PANTS_JUMP_TICK, 0);
			nbt.putBoolean(POWERED_PANTS_JUMP, false);
			nbt.putBoolean(POWERED_PANTS_JUMP_MODE, false);
			return;
		}

		// 检测是否在地上
		if (!player.onGround()) {
			nbt.putInt(POWERED_PANTS_JUMP_TICK, 0);
			nbt.putBoolean(POWERED_PANTS_JUMP, false);
			nbt.putBoolean(POWERED_PANTS_JUMP_MODE, false);
			return;
		}

		PoweredPantsItem item;
		// 检测是否装备了对应道具
		if ((item = PoweredPantsItem.isExoframeEquipped(player)) == null) {
			nbt.putInt(POWERED_PANTS_JUMP_TICK, 0);
			nbt.putBoolean(POWERED_PANTS_JUMP, false);
			nbt.putBoolean(POWERED_PANTS_JUMP_MODE, false);
			return;
		}

		// 检测是否按下空格键
		if (!InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW_KEY_SPACE)) {
			// 进行跳跃
			if (nbt.getInt(POWERED_PANTS_JUMP_TICK) != 0) {
				nbt.putBoolean(POWERED_PANTS_JUMP, true);
				nbt.putBoolean(POWERED_PANTS_JUMP_MODE, false);
				nbt.putBoolean(POWERED_PANTS_JUMPED_ALREADY, true);
				Minecraft minecraft = Minecraft.getInstance();
				if (minecraft.player != null) {
					minecraft.player.jumpFromGround();
				}
			}
		} else if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW_KEY_SPACE)) {
			// 开始蓄力
			if (nbt.getInt(POWERED_PANTS_JUMP_TICK) == 0) {
				nbt.putBoolean(POWERED_PANTS_JUMP, false);
				nbt.putBoolean(POWERED_PANTS_JUMP_MODE, true);
			}

			// 释放特效
			int tick = nbt.getInt(POWERED_PANTS_JUMP_TICK);
			if (item.getMaxJumpTime() == tick) {
				if (Minecraft.getInstance().getConnection() != null) {
					Level level = player.level();
					player.playSound(PROMPT.get());
					level.playSound(null, player.getOnPos(), CANDLE_EXTINGUISH, PLAYERS);
					for (int i = 0; i < player.getRandom().nextInt(5, 12); i++) {
						level.addParticle(
								ParticleTypes.EGG_CRACK,
								player.getX(), player.getY() + 0.3, player.getZ(),
								0, player.getRandom().nextInt(1, 5) * 0.01,
								player.getRandom().nextInt(1, 5) * 0.01);
					}
				}
			}

			// 递增蓄力时间
			nbt.putInt(POWERED_PANTS_JUMP_TICK, nbt.getInt(POWERED_PANTS_JUMP_TICK) + 1);
		}
	}
}