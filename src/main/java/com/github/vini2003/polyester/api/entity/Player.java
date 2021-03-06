package com.github.vini2003.polyester.api.entity;

import com.github.vini2003.polyester.api.inventory.InventoryComponent;
import com.github.vini2003.polyester.api.inventory.InventoryComponentFromInventory;
import com.github.vini2003.polyester.api.data.Position;
import com.github.vini2003.polyester.api.lobby.Lobby;
import com.github.vini2003.polyester.api.team.Team;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public interface Player {
	/**
	 * Retrieves the lobby this player is bound to.
	 *
	 * @return the requested lobby.
	 */
	Lobby getLobby();

	/**
	 * Binds the lobby this player is attached to.
	 *
	 * @param lobby the specified lobby.
	 */
	void bindLobby(Lobby lobby);

	/**
	 * Unbinds the lobby this player is attached to.
	 */
	void unbindLobby();

	/**
	 * Retrieves the team this player is bound to.
	 *
	 * @return the requested team.
	 */
	Team getTeam();

	/**
	 * Binds the team this player is bound to.
	 *
	 * @param team the specified team.
	 */
	void bindTeam(Team team);

	/**
	 * unbinds the team this player is bound to.
	 */
	void unbindTeam();

	/**
	 * Retrieves the entity this interface is implemented in.
	 *
	 * @return the requested entity.
	 */
	default ServerPlayerEntity target() {
		return (ServerPlayerEntity) this;
	}

	/**
	 * Retrieves a player from a player entity.
	 *
	 * @param entity the specified entity.
	 * @return the requested player.
	 */
	static Player of(PlayerEntity entity) {
		return (Player) entity;
	}

	/**
	 * Retrieves a player from a command source.
	 *
	 * @param source the specified source.
	 * @return the requested player.
	 */
	static Player of(ServerCommandSource source) {
		try {
			return (Player) source.getPlayer();
		} catch (Exception ignored) {
			return null;
		}
	}

	/**
	 * Retrieves a player from a command context.
	 *
	 * @param context the specified context.
	 * @return the requested player.
	 */
	static Player of(CommandContext<ServerCommandSource> context) {
		return Player.of(context.getSource());
	}

	/**
	 * Retrieves the UUID of this player.
	 *
	 * @return the requested UUID.
	 */
	default String getUuid() {
		return target().getGameProfile().getId().toString();
	}

	/**
	 * Retrieves the name of this player.
	 *
	 * @return the requested name.
	 */
	default String getName() {
		return target().getGameProfile().getName();
	}

	/**
	 * Retrieves the stack held by this player.
	 *
	 * @return the requested stack.
	 */
	default ItemStack getHeldStack() {
		return target().getStackInHand(target().getActiveHand());
	}

	/**
	 * Sets the stack held by this player.
	 *
	 * @param stack the specified stack.
	 */
	default void setHeldStack(ItemStack stack) {
		target().setStackInHand(target().getActiveHand(), stack);
	}

	/**
	 * Retrieves the position of this player.
	 *
	 * @return the requested position.
	 */
	default Position getPosition() {
		return Position.of(target().getPos());
	}

	/**
	 * Sets the position of this player.
	 *
	 * @param position the specified position.
	 */
	default void setPosition(Position position) {
		target().setPos(position.getX(), position.getY(), position.getZ());
	}

	/**
	 * Retrieves the game mode of this player.
	 *
	 * @return the requested game mode.
	 */
	default GameMode getGameMode() {
		return target().interactionManager.getGameMode();
	}

	/**
	 * Sets the game mode of this player.
	 *
	 * @param gameMode the specified game mode.
	 */
	default void setGameMode(GameMode gameMode) {
		target().interactionManager.setGameMode(gameMode);
		gameMode.setAbilitites(target().abilities);
	}

	/**
	 * Sends a chat message to this player.
	 *
	 * @param message the specified message.
	 * @param type    the specified message's type.
	 */
	default void sendChatMessage(Text message, MessageType type) {
		target().sendChatMessage(message, type);
	}

	/**
	 * Sends a chat message to this player.
	 *
	 * @param message the specified message.
	 */
	default void sendChatMessage(Text message) {
		sendChatMessage(message, MessageType.CHAT);
	}

	/**
	 * Sends a message to this player.
	 *
	 * @param message the specified message.
	 */
	default void sendMessage(Text message) {
		target().sendMessage(message);
	}

	/**
	 * Retrieves the world this player is in.
	 *
	 * @return the requested world.
	 */
	default World getWorld() {
		return target().getEntityWorld();
	}

	/**
	 * Retrieves the dimension this player is in.
	 *
	 * @return the requested dimension.
	 */
	default DimensionType getDimension() {
		return target().dimension;
	}

	/**
	 * Asserts whether this player is in a lobby.
	 *
	 * @return true if yes; false if no.
	 */
	default boolean hasLobby() {
		return getLobby() != null;
	}

	/**
	 * Asserts whether this player is in a lobby
	 * and that lobby has a preset loaded.
	 *
	 * @return true if yes; false if no.
	 */
	default boolean hasPreset() {
		return hasLobby() && getLobby().getPreset() != null;
	}

	/**
	 * Retrieves the preset  identifier of the lobby
	 * this player is in.
	 *
	 * @return the requested identifier.
	 */
	default Identifier getPresetIdentifier() {
		return hasPreset() ? getLobby().getPreset().getIdentifier() : null;
	}

	/**
	 * Retrieves the inventory component of
	 * this entity.
	 *
	 * @return the requested component.
	 */
	default InventoryComponent getInventory() {
		return InventoryComponentFromInventory.of(target().inventory);
	}

	/**
	 * Retrieves the ender chest inventory
	 * component of this entity.
	 *
	 * @return the requested component.
	 */
	default InventoryComponent getEnderChestInventory() {
		return InventoryComponentFromInventory.of(target().inventory);
	}
}
