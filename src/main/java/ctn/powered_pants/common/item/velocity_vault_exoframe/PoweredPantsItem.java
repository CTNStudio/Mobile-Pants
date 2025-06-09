package ctn.powered_pants.common.item.velocity_vault_exoframe;

import ctn.powered_pants.api.IModPlayer;
import ctn.powered_pants.events.InputEvents;
import ctn.powered_pants.events.TickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Stack;

import static net.minecraft.core.particles.ParticleTypes.POOF;
import static net.minecraft.util.ParticleUtils.spawnSmashAttackParticles;

@EventBusSubscriber
public abstract class PoweredPantsItem extends ArmorItem {
	protected int   maxJumpTime  = 300;
	protected float maxJumpAngle = 45;

	public PoweredPantsItem(Holder<ArmorMaterial> material, Properties rroperties) {
		super(material, Type.LEGGINGS, rroperties.stacksTo(1));
	}

	/**
	 * 检测玩家是否装备了Velocity Vault Exoframe
	 */
	public static PoweredPantsItem isExoframeEquipped(Player player) {
		for (ItemStack stack : player.getArmorSlots()) {
			if (stack == null || stack.isEmpty()) {
				continue;
			}
			if (stack.getItem() instanceof PoweredPantsItem poweredPantsItem) {
				return poweredPantsItem;
			}
		}
		return null;
	}

	public abstract void initJumpConfing();

	public <T extends LivingEntity & IModPlayer> void jump(T player) {
	    ((Player) player).awardStat(Stats.JUMP);
	    CompoundTag persistentData = player.getPersistentData();
	    if (persistentData.getBoolean(InputEvents.POWERED_PANTS_JUMP)) {
	        int jumpTickCount = persistentData.getInt(InputEvents.POWERED_PANTS_JUMP_TICK);

	        // 获取玩家当前的速度向量和跳跃力量
	        var deltaMovement = player.getDeltaMovement();
	        var jumpPower = player.modGetJumpPower();

	        // 跳跃高度系数
	        float jumpHeight;

	        // 跳跃方向向量
	        Vec3 jumpDirection;

	        // 是否为对角线跳跃
	        boolean isDiagonalJump = true;

	        // 计算跳跃阶段相关参数
	        int maxJumpDurationPerStage = maxJumpTime / 100;
	        int stageDuration = maxJumpDurationPerStage * 10;

		    // 根据跳跃时间计算角度
		    float xRotation = -Math.min(maxJumpAngle, (float) jumpTickCount / 5);
		    if (jumpTickCount <= stageDuration) {
		        jumpHeight = 1;
		        isDiagonalJump = false;
	        } else {
		        jumpHeight = (float) Math.min(maxJumpTime, jumpTickCount) / 5;
		        if (xRotation > -25) {
					xRotation = -25;
				}
	        }

	        // 计算跳跃方向
	        jumpDirection = player.calculateViewVector(xRotation, player.getYRot());

	        // 执行跳跃逻辑
	        if (isDiagonalJump) {
	            var vec = deltaMovement.add(jumpDirection.scale(0.42 + jumpPower + 0.1 * jumpHeight));
	            player.modPlayerJump(vec, vec.y);
	        } else {
	            player.modPlayerJump(deltaMovement, jumpPower + 0.1 * jumpHeight);
	        }

	        // 消耗饥饿值
	        if (player.isSprinting()) {
	            ((Player) player).causeFoodExhaustion(0.2F);
	        } else {
	            ((Player) player).causeFoodExhaustion(0.05F);
	        }

	        // 重置跳跃状态
	        persistentData.putBoolean(InputEvents.POWERED_PANTS_JUMP, false);
	        persistentData.putInt(InputEvents.POWERED_PANTS_JUMP_TICK, 0);
	    }
	}


		public static void fallHandle(float fallDistance, float damageMultiplier, Player player, PoweredPantsItem item, CallbackInfoReturnable<Integer> cir) {
	    Level level = player.level();

	    // 生成坠落时的冲击粒子效果
	    for (int i = 0; i < fallDistance; i++) {
	        spawnSmashAttackParticles(level, player.getOnPos(), (int) fallDistance);
	    }

	    // 添加额外的烟雾粒子
	    for (int i = 0; i < fallDistance * 2; i++) {
	        level.addParticle(
	            POOF,
	            player.getX(), player.getY(), player.getZ(),
	            level.random.nextInt(-2, 2) * 0.01,
	            level.random.nextInt(-2, 2) * 0.01,
	            level.random.nextInt(-2, 2) * 0.01
	        );
	    }

		// TODO
	    if (fallDistance >= 3) {
	        if (level instanceof ServerLevel serverLevel) {
	            double playerX = player.getX();
	            double playerY = player.getY();
	            double playerZ = player.getZ();

	            // 计算影响半径，最大为 50
	            double effectRadius = Math.min(50, fallDistance);

	            // 创建攻击范围 AABB
	            AABB attackAABB = new AABB(
	                playerX - effectRadius, playerY - effectRadius, playerZ - effectRadius,
	                playerX + effectRadius, playerY + effectRadius, playerZ + effectRadius
	            );

	            // 获取范围内可攻击的目标
	            List<LivingEntity> targetEntities = getAttackableTarget(player, serverLevel, attackAABB);

		        // 对每个目标施加击退和伤害
		        for (LivingEntity entity : targetEntities) {
					ATTACK_STACK.push(new PoweredPantsAttack(player.position(), fallDistance, player, entity));
	            }
	        }
	    }

	    // 判断是否免疫坠落伤害
	    if (!player.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
	        // 修改返回的坠落伤害值
	        double jumpTimeFactor = item.getMaxJumpTime() * 0.1;
	        double adjustedFallDistance = fallDistance - 3.0F - 1 * jumpTimeFactor * 1.5;
	        int damageValue = Mth.ceil(adjustedFallDistance * damageMultiplier);
	        cir.setReturnValue(damageValue);
	    }
	}

	private static @NotNull List<LivingEntity> getAttackableTarget(LivingEntity entity, ServerLevel serverLevel, AABB aabb) {
		return serverLevel.getEntitiesOfClass(LivingEntity.class, aabb, (livingEntity) -> !livingEntity.is(entity) && livingEntity.isAlive());
	}

	public int getMaxJumpTime() {
		return maxJumpTime;
	}

	public float getMaxJumpAngle() {
		return maxJumpAngle;
	}

	private record PoweredPantsAttack(Vec3 pos, float fallDistance, Player player, LivingEntity entity) {
		public void run() {
			final double maxDistance = fallDistance * 0.8;
			final Level world = player.level();
			assert world instanceof ServerLevel;

			// 计算击退方向（玩家指向目标）
			final double knockbackDirectionX = pos.x() - entity.getX();
			final double knockbackDirectionZ = pos.z() - entity.getZ();
			final double currentDistance = pos.distanceTo(entity.position());

			// 距离百分比（0.0 到 1.0）
			final double distanceRatio = Math.min(1.0, currentDistance / maxDistance);
			final double scaledValue = maxDistance / distanceRatio;

			// 造成坠落伤害
			entity.hurt(world.damageSources().playerAttack(player), fallDistance);

			// 触发击退
			entity.knockback(fallDistance / 2.0, knockbackDirectionX, knockbackDirectionZ);
			Vec3 updatedDeltaMovement = entity.getDeltaMovement().add(0, fallDistance / 10.0, 0).scale(scaledValue / 35);
			entity.setDeltaMovement(updatedDeltaMovement);
		}
	}

	private static final Stack<PoweredPantsAttack> ATTACK_STACK = new Stack<>();

	@SubscribeEvent
	public static void tickEvent(ServerTickEvent event) {
		if (ATTACK_STACK.isEmpty()) {
			return;
		}

		for(int i = 0; i < 10; i++) {
			final var attackObj = ATTACK_STACK.pop();
			attackObj.run();
		}
	}
}
