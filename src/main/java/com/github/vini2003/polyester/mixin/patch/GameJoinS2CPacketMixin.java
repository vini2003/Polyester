package com.github.vini2003.polyester.mixin.patch;

import com.github.vini2003.polyester.api.dimension.registry.DimensionRegistry;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(GameJoinS2CPacket.class)
public abstract class GameJoinS2CPacketMixin {
	@Shadow
	private DimensionType dimension;

	/**
	 * @author vini2003
	 * @reason trick vanilla into thinking
	 * our dimensions are the overworld.
	 */
	@Inject(at = @At("HEAD"), method = "write(Lnet/minecraft/util/PacketByteBuf;)V")
	public void write(PacketByteBuf buffer, CallbackInfo callbackInformation) throws IOException {
		if (DimensionRegistry.INSTANCE.isTracked(dimension)) {
			dimension = DimensionType.OVERWORLD;
		}
	}
}
