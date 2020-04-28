package com.github.vini2003.example;

import com.github.vini2003.spork.api.display.DisplayBuilder;
import com.github.vini2003.spork.api.display.DisplayBuilderFactory;
import com.github.vini2003.spork.api.event.EventResult;
import com.github.vini2003.spork.api.event.type.item.ItemUseEvent;
import com.github.vini2003.spork.api.manager.LobbyManager;
import com.github.vini2003.spork.utility.ItemStackUtilities;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.Collections;

public class ExampleDisplay {
	private static final DisplayBuilder displayBuilder =
			DisplayBuilderFactory.builder()
					.bindTitle(new LiteralText("Lobbies"))
					.bindStack(10, () -> {
						ItemStack tntRunStack = ItemStackUtilities.withLore(new ItemStack(Items.TNT), Collections.singletonList(new LiteralText("§7§oJoin a TNT Run lobby!")));
						tntRunStack.setCustomName(new LiteralText("§c§o§lTNT Run"));
						return tntRunStack;
					})
					.bindAction(10, (player) -> {
						LobbyManager.INSTANCE.getLobby(new Identifier("spork", "lobby")).bindPlayer(player);
					})
					.bindHeight(6)
					.build();

	public static void initialize() {
		ItemUseEvent.register(((world, player, data, stack, hand) -> {
			if (stack.getItem() == Items.SHULKER_BOX) {
				player.openContainer(displayBuilder);
			}
			return EventResult.CONTINUE;
		}));
	}
}
