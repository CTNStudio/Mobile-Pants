package ctn.powered_pants.events;

import ctn.powered_pants.common.item.velocity_vault_exoframe.PoweredPantsItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static ctn.powered_pants.PoweredPants.MOD_ID;
import static ctn.powered_pants.events.InputEvents.POWERED_PANTS_JUMPED_ALREADY;
import static ctn.powered_pants.events.InputEvents.POWERED_PANTS_JUMPED_ALREADY_TICK;

@EventBusSubscriber(modid = MOD_ID)
public class TickEvents {

	@SubscribeEvent
	public static void tick(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (player.isSpectator() || player.isCreative()) {
			return;
		}
		if (!level.isClientSide) {
			return;
		}
		var nbt = player.getPersistentData();
		if (PoweredPantsItem.isExoframeEquipped(player) == null || player.onGround()) {
			return;
		}
		if (nbt.getBoolean(POWERED_PANTS_JUMPED_ALREADY)) {
			level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, player.getX(), player.getY(), player.getZ(), 0, 0, 0);
			if (player.getDeltaMovement().y <= 0) {
				nbt.putBoolean(POWERED_PANTS_JUMPED_ALREADY, false);
			}
		}

	}
}
