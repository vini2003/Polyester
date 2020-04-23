package com.github.vini2003.spork.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.command.arguments.BlockPosArgumentType;
import net.minecraft.command.arguments.DimensionArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;

import static net.minecraft.command.arguments.BlockPosArgumentType.blockPos;
import static net.minecraft.command.arguments.BlockPosArgumentType.getBlockPos;
import static net.minecraft.command.arguments.DimensionArgumentType.dimension;
import static net.minecraft.command.arguments.DimensionArgumentType.getDimensionArgument;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class DimensionCommands {
    private static int teleport(CommandContext<ServerCommandSource> context, DimensionType type, BlockPos position) throws CommandSyntaxException {
        PlayerEntity player = context.getSource().getPlayer();
        FabricDimensions.teleport(player, type, (entity, world, direction, pitch, yaw) -> new BlockPattern.TeleportTarget(new Vec3d(position).add(0.5d, 0d, 0.5d), Vec3d.ZERO, (int) yaw));
        player.teleport(position.getX(), position.getY(), position.getZ());
        return 1;
    }

    public static void initialize() {
        CommandRegistry.INSTANCE.register(false, dispatcher -> {
            LiteralCommandNode<ServerCommandSource> baseNode =
                literal("dimension").requires(source -> source.hasPermissionLevel(4)).build();

            LiteralCommandNode<ServerCommandSource> teleportNode =
                literal("teleport")
                    .then(argument("name", dimension())
                        .executes(context -> teleport(context, getDimensionArgument(context, "name"), new BlockPos(0, 64, 0)))
                            .then(argument("position", blockPos())
                                .executes(context -> teleport(context, getDimensionArgument(context, "dimension"), getBlockPos(context, "position")))))
                .build();

            dispatcher.getRoot().addChild(baseNode);
            baseNode.addChild(teleportNode);
        });
    }
}