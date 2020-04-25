package com.github.vini2003.spork.mixin.patch;

import com.github.vini2003.spork.api.dimension.ImplementedDimensionType;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.GameMode;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;

@Mixin(GameJoinS2CPacket.class)
public class GameJoinS2CPacketMixin {
	@Shadow
	private int playerEntityId;

	@Shadow
	private GameMode gameMode;

	@Shadow
	private boolean hardcore;

	@Shadow
	private DimensionType dimension;

	@Shadow
	private long seed;

	@Shadow
	private int maxPlayers;

	@Shadow
	private LevelGeneratorType generatorType;

	@Shadow
	private int chunkLoadDistance;

	@Shadow
	private boolean reducedDebugInfo;

	@Shadow
	private boolean showsDeathScreen;

	/**
	 * @author vini2003
	 * @reason trick vanilla into thinking
	 * our dimensions are the overworld.
	 */
	@Overwrite
	public void write(PacketByteBuf buffer) throws IOException {
		buffer.writeInt(this.playerEntityId);
		int i = this.gameMode.getId();
		if (this.hardcore) {
			i |= 8;
		}

		buffer.writeByte(i);
		buffer.writeInt(this.dimension instanceof ImplementedDimensionType ? DimensionType.OVERWORLD.getRawId() : this.dimension.getRawId());
		buffer.writeLong(this.seed);
		buffer.writeByte(this.maxPlayers);
		buffer.writeString(this.generatorType.getName());
		buffer.writeVarInt(this.chunkLoadDistance);
		buffer.writeBoolean(this.reducedDebugInfo);
		buffer.writeBoolean(this.showsDeathScreen);
	}
}
