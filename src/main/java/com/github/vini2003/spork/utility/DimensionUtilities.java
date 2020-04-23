package com.github.vini2003.spork.utility;

import com.github.vini2003.spork.api.entity.Player;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class DimensionUtilities {
	public static void teleport(PlayerEntity player, DimensionType type, BlockPos position) {
		FabricDimensions.teleport(player, type, (entity, world, direction, pitch, yaw) -> new BlockPattern.TeleportTarget(new Vec3d(position).add(0.5d, 0d, 0.5d), Vec3d.ZERO, (int) yaw));
		player.teleport(position.getX(), position.getY(), position.getZ());
	}
}
