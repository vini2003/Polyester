package com.github.vini2003.polyester.api.structure;

import com.github.vini2003.polyester.api.block.BlockInformation;
import com.github.vini2003.polyester.api.data.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.BlockRotation;

import java.util.HashMap;
import java.util.Map;

public class Structure {
	public final Map<Position, CompoundTag> blocks = new HashMap<>();

	public void add(Position position, BlockInformation blockInformation) {
		blocks.put(position, blockInformation.serialize());
	}

	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();

		ListTag list = new ListTag();

		blocks.forEach((blockPosition, blockInformation) -> {
			list.add(blockInformation);
		});

		tag.put("list", list);

		return tag;
	}

	public static Structure deserialize(CompoundTag tag) {
		Structure structure = new Structure();

		((ListTag) tag.get("list")).forEach(data -> {
			BlockInformation information = BlockInformation.deserialize((CompoundTag) data);

			structure.add(information.getPosition(),  information);
		});

		return structure;
	}

	public void rotate() {
		Map<Position, CompoundTag> rotated = new HashMap<>();

		blocks.forEach((position, information) -> {
			rotated.put(Position.of(position.asBlockPosition().rotate(BlockRotation.CLOCKWISE_90)), information);
		});

		blocks.clear();
		blocks.putAll(rotated);
	}
}
