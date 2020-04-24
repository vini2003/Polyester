package com.github.vini2003.example;

import com.github.vini2003.spork.api.event.EventResult;
import com.github.vini2003.spork.api.event.type.block.BlockStepEvent;
import com.github.vini2003.spork.api.lobby.Lobby;
import com.github.vini2003.spork.api.preset.Preset;
import com.github.vini2003.spork.api.text.TextWrapper;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;

public class ExamplePreset extends Preset {
	private static final Identifier IDENTIFIER = new Identifier("example", "preset");

	private final BlockStepEvent.Listener STEP_LISTENER = ((world, entity, data) -> {
		if (data.getBlock() == Blocks.GRASS_BLOCK) {
			world.setBlockState(data.getPosition().asBlockPosition(), Blocks.SAND.getDefaultState());
		}
		return EventResult.CONTINUE;
	});

	@Override
	public Identifier getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void apply(Lobby lobby) {
		lobby.enqueueAction((state) -> state.getTime().getValue().equals(12000), (action) -> {
			lobby.getPlayers().forEach(player -> player.sendMessage(TextWrapper.builder().with("One minute has passed!").build()));
		});

		BlockStepEvent.register(STEP_LISTENER);
	}

	@Override
	public void retract(Lobby lobby) {
		lobby.unqueueAllActions();

		BlockStepEvent.unregister(STEP_LISTENER);
	}
}
