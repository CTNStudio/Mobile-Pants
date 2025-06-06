package ctn.powered_pants.common.item.velocity_vault_exoframe;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;

import static ctn.powered_pants.config.PoweredPantsConfig.CONFIG;

public class NetheritePoweredPantsItem extends PoweredPantsItem {
	public NetheritePoweredPantsItem(Holder<ArmorMaterial> material, Properties rroperties) {
		super(material, rroperties);
	}

	@Override
	public void initJumpConfing() {
		maxJumpTime  = CONFIG.NETHERITE_MAX_JUMP_TIME.get();
		maxJumpAngle = Float.parseFloat(CONFIG.NETHERITE_MAX_JUMP_ANGLE.get());
	}
}
