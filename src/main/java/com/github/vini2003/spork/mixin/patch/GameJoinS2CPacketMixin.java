package com.github.vini2003.spork.mixin.patch;

import com.github.vini2003.spork.api.dimension.DimensionRegistry;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.GameMode;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(GameJoinS2CPacket.class)
public abstract class GameJoinS2CPacketMixin {
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

	@Shadow public abstract boolean isHardcore();

	/**
	 * @author vini2003
	 * @reason trick vanilla into thinking
	 * our dimensions are the overworld.
	 */
	@Inject(at = @At("HEAD"), method = "write(Lnet/minecraft/util/PacketByteBuf;)V")
	public void write(PacketByteBuf buffer, CallbackInfo callbackInformation) throws IOException {
		if (!DimensionRegistry.INSTANCE.shouldSynchronize(dimension)) {
			dimension = DimensionType.OVERWORLD;
		}
	}
}
