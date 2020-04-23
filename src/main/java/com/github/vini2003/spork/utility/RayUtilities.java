package com.github.vini2003.spork.utility;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

public class RayUtilities {
	/**
	 * Traces a ray for a given world, player and handling context.
	 *
	 * @param world         the specified world.
	 * @param player        the specified player, eyesight used for ray trace.
	 * @param fluidHandling the specified context.
	 * @return the requested hit result.
	 */
	public static HitResult rayTrace(World world, PlayerEntity player, RayTraceContext.FluidHandling fluidHandling) {
		float pitch = player.pitch;
		float yaw = player.yaw;
		Vec3d vecA = player.getCameraPosVec(1.0F);
		float magicA = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
		float magicB = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
		float magicC = -MathHelper.cos(-pitch * 0.017453292F);
		float magicD = MathHelper.sin(-pitch * 0.017453292F);
		float magicE = magicB * magicC;
		float magicF = magicA * magicC;
		Vec3d vecB = vecA.add((double) magicE * 5.0D, (double) magicD * 5.0D, (double) magicF * 5.0D);
		return world.rayTrace(new RayTraceContext(vecA, vecB, RayTraceContext.ShapeType.OUTLINE, fluidHandling, player));
	}
}
