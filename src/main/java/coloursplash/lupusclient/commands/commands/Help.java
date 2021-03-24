/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.commands.Commands;
import coloursplash.lupusclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class Help extends Command {
    public Help() {
        super("help", "List of all commands.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            ChatUtils.info("--- List of all (highlight)%d(default) commands ---", Commands.get().getCount());
            Commands.get().forEach(command -> ChatUtils.info("(highlight)%s(default): %s", command.getName(), command.getDescription()));
            return SINGLE_SUCCESS;
        });
    }
}