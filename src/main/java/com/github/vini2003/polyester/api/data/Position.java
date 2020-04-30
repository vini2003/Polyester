package com.github.vini2003.polyester.api.data;

import net.minecraft.client.util.math.Vector3d;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * A Position is a basic implementation
 * of a mathematical vector which holds
 * x, y and z components, and provides
 * utility methods for converting it
 * to other position types.
 */
public class Position extends Vec3d {
	public Position(double x, double y, double z) {
		super((float) x, (float) y, (float) z);
	}

	private Position(float x, float y, float z) {
		super(x, y, z);
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public double z() {
		return z;
	}

	public static Position of(Number x, Number y, Number z) {
		return new Position(x.floatValue(), y.floatValue(), z.floatValue());
	}

	public static Position of(BlockPos blockPosition) {
		return new Position(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
	}

	public static Position of(net.minecraft.util.math.Position position) {
		return new Position(position.getX(), position.getY(), position.getZ());
	}

	public double getSquaredDistance(Position position) {
		return (((y() - position.y()) * (y() - position.y())) + ((x() - position.x()) * (x() - position.x())));
	}

	public BlockPos asBlockPosition() {
		return new BlockPos(this.x, this.y, this.z);
	}

	public Vector3d asDoubleVector() {
		return new Vector3d(this.x, this.y, this.z);
	}

	public Vector3f asFloatVector() {
		return new Vector3f((float) this.x, (float) this.y, (float) this.z);
	}
}
