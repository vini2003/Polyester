package com.github.vini2003.spork.mixin.event;

import com.github.vini2003.spork.api.entity.Player;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements Player {

}
