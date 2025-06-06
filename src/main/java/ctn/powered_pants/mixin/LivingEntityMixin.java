package ctn.powered_pants.mixin;

import ctn.powered_pants.common.item.velocity_vault_exoframe.PoweredPantsItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static ctn.powered_pants.common.item.velocity_vault_exoframe.PoweredPantsItem.fallHandle;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
		super(p_19870_, p_19871_);
	}
	@Inject(method = "calculateFallDamage", at = @At("HEAD"), cancellable = true)
	protected void poweredPants$calculateFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
		if (!((LivingEntity) (Entity) this instanceof Player player)) {
			return;
		}
		PoweredPantsItem item;
		if ((item = PoweredPantsItem.isExoframeEquipped(player)) == null) {
			return;
		}
		fallHandle(fallDistance, damageMultiplier, player, item, cir);
	}
}
