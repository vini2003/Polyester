package com.github.vini2003.polyester.api.structure;

import com.github.vini2003.polyester.api.block.BlockInformation;
import com.github.vini2003.polyester.api.data.Position;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.datafixer.NbtOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.HashMap;
import java.util.Map;

public class Structure {
	public final Map<Position, BlockInformation> blocks = new HashMap<>();

	public void bindBlock(Position position, BlockInformation blockInformation) {
		blocks.put(position, blockInformation);
	}

	public void bindInformation(BlockInformation information) {
		bindBlock(information.getPosition(), information);
	}

	public void unbindBlock(Position position) {
		blocks.remove(position);
	}

	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();

		ListTag list = new ListTag();

		blocks.forEach((blockPosition, blockInformation) -> {
			list.add(blockInformation.serialize());
		});

		tag.put("list", list);

		return tag;
	}

	public static Structure deserialize(CompoundTag tag) {
		Structure structure = new Structure();

		((ListTag) tag.get("list")).forEach(data -> {
			BlockInformation information = BlockInformation.deserialize((CompoundTag) data);

			structure.bindBlock(information.getPosition(),  information);
		});

		return structure;
	}
}
