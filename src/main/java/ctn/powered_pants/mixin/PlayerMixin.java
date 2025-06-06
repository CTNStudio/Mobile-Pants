package ctn.powered_pants.mixin;

import ctn.powered_pants.api.IModPlayer;
import ctn.powered_pants.common.item.velocity_vault_exoframe.PoweredPantsItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static ctn.powered_pants.common.item.velocity_vault_exoframe.PoweredPantsItem.isExoframeEquipped;
import static net.neoforged.neoforge.common.CommonHooks.onLivingJump;

@Mixin(Player.class)
@Implements(@Interface(iface = IModPlayer.class, prefix = "poweredPantsIm$"))
public abstract class PlayerMixin extends LivingEntity implements IModPlayer {
	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract void awardStat(ResourceLocation statKey);

	@Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
	public void poweredPants$jumpFromGround(CallbackInfo ci) {
		PoweredPantsItem item;
		if ((item = isExoframeEquipped((Player) (LivingEntity) this)) == null) {
			return;
		}
		ci.cancel();
		item.jump(this);
	}

	@Unique
	public void poweredPantsIm$modPlayerJump(Vec3 vec3, double jumpPower) {
		this.awardStat(Stats.JUMP);
		this.setDeltaMovement(vec3.x, jumpPower, vec3.z);
		if (this.isSprinting()) {
			float f = this.getYRot() * ((float) Math.PI / 180F);
			this.setDeltaMovement(this.getDeltaMovement().add(-Mth.sin(f) * 0.2F, 0.0D, Mth.cos(f) * 0.2F));
		}
		this.hasImpulse = true;
		onLivingJump(this);
	}

	@Unique
	public float poweredPantsIm$modGetJumpPower() {
		return super.getJumpPower();
	}
}
