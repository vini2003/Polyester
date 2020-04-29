package com.github.vini2003.spork.mixin.patch;

import com.github.vini2003.spork.api.dimension.ImplementedDimensionType;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.GameMode;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;

@Mixin(PlayerRespawnS2CPacket.class)
public class PlayerRespawnS2CPacketMixin {
	@Shadow private DimensionType dimension;

	@Shadow private long sha256Seed;

	@Shadow private GameMode gameMode;

	@Shadow private LevelGeneratorType generatorType;

	/**
	 * @author vini2003
	 * @reason trick vanilla into thinking
	 * our dimensions are the overworld.
	 */
	@Overwrite
	public void write(PacketByteBuf buf) throws IOException {
		buf.writeInt(dimension instanceof ImplementedDimensionType ? DimensionType.OVERWORLD.getRawId() : dimension.getRawId());
		buf.writeLong(this.sha256Seed);
		buf.writeByte(this.gameMode.getId());
		buf.writeString(dimension instanceof ImplementedDimensionType ? LevelGeneratorType.DEFAULT.getName() : generatorType.getName());
	}
}
