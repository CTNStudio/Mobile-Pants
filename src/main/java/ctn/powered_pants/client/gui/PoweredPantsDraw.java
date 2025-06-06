package ctn.powered_pants.client.gui;

import ctn.powered_pants.common.item.velocity_vault_exoframe.PoweredPantsItem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static ctn.powered_pants.PoweredPants.MOD_ID;
import static ctn.powered_pants.events.InputEvents.POWERED_PANTS_JUMP_MODE;
import static ctn.powered_pants.events.InputEvents.POWERED_PANTS_JUMP_TICK;

@OnlyIn(Dist.CLIENT)
public class PoweredPantsDraw extends LayeredDraw implements LayeredDraw.Layer {
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/gui/powered_pants.png");
	private final        Minecraft        minecraft;

	public PoweredPantsDraw(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Minecraft minecraft) {
		this.minecraft = minecraft;
		render(guiGraphics, deltaTracker);
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
		if (minecraft.options.hideGui) {
			return;
		}
		Player player = minecraft.player;
		if (player == null || player.isSpectator()) {
			return;
		}
		CompoundTag nbt = player.getPersistentData();
		if (!nbt.getBoolean(POWERED_PANTS_JUMP_MODE)) {
			return;
		}
		PoweredPantsItem item = PoweredPantsItem.isExoframeEquipped(player);
		if (item == null) {
			return;
		}

		int backgroundWidth = 182;
		int backgroundHeight = 7;
		int progressWidth = 180;
		int maxJumpLevelTime = item.getMaxJumpTime();
		int x = guiGraphics.guiWidth() / 2 - backgroundWidth / 2;
		int y = guiGraphics.guiHeight() - backgroundHeight - 23;

		// 绘制背景
		this.render(guiGraphics, x, y, 0, 0, backgroundWidth, backgroundHeight);

		int tick = nbt.getInt(POWERED_PANTS_JUMP_TICK);

		// 绘制进度条
		{
			if (maxJumpLevelTime <= 0) {
				return; // 防止除以零
			}
			this.render(guiGraphics, x, y, 0, 7, 2, backgroundHeight);
			int progressX = x + 2;
			int progressOffsetX = 182;
			final int percentage = getPercentage(tick, maxJumpLevelTime); // 计算进度百分比（0~100）
			int progressDrawWidth = getDrawWidth(progressWidth, percentage); // 根据百分比计算要绘制的宽度
			if (progressDrawWidth > progressWidth) {
				progressDrawWidth = progressWidth;
			}
			progressOffsetX -= progressDrawWidth;
			if (progressOffsetX < 2) {
				progressOffsetX = 2;
			}
			this.render(guiGraphics, progressX, y, progressOffsetX, 7, progressDrawWidth, backgroundHeight);
			int i = maxJumpLevelTime / 100 * 10;
			render(
					guiGraphics, x + getDrawWidth(progressWidth, getPercentage(i, maxJumpLevelTime)), y,
					183, 0, 3, backgroundHeight);
		}
	}

	private int getWidth(int max, int tick) {
		return getDrawWidth(max, getPercentage(tick, max));
	}

	// 获取宽度
	private int getDrawWidth(int max, int percentage) {
		return (int) (((long) max * percentage) / 100);
	}

	// 获取占比
	private int getPercentage(int tick, int max) {
		return Math.min(100, (int) (((long) tick * 100) / max));
	}

	private void render(GuiGraphics guiGraphics, int x, int y, int offsetX, int offsetY, int width, int height) {
		guiGraphics.blit(TEXTURE, x, y, offsetX, offsetY, width, height, 256, 256);
	}

	private void renderScale(GuiGraphics guiGraphics, int x, int y, int offsetX, int offsetY, int width, int height, int tick, int max, int maxJumpLevelTime) {
		if (getPercentage(tick, maxJumpLevelTime) >= getPercentage(max, maxJumpLevelTime)) {
			offsetX += 4;
		}
		render(guiGraphics, x + 2, y, offsetX, offsetY, width, height);
	}
}
