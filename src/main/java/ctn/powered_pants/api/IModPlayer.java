package ctn.powered_pants.api;

import net.minecraft.world.phys.Vec3;

public interface IModPlayer {
	void modPlayerJump(Vec3 vec3, double jumpPower);

	float modGetJumpPower();
}
