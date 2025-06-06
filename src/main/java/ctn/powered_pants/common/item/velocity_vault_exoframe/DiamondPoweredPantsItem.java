package ctn.powered_pants.common.item.velocity_vault_exoframe;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;

import static ctn.powered_pants.config.PoweredPantsConfig.CONFIG;

public class DiamondPoweredPantsItem extends PoweredPantsItem {
	public DiamondPoweredPantsItem(Holder<ArmorMaterial> material, Properties rroperties) {
		super(material, rroperties);
	}

	@Override
	public void initJumpConfing() {
		maxJumpTime  = CONFIG.DIAMOND_MAX_JUMP_TIME.get();
		maxJumpAngle = Float.parseFloat(CONFIG.DIAMOND_MAX_JUMP_ANGLE.get());
	}
}
