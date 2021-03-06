/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.commands;

//Created by squidoodly 01/07/2020

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.systems.Ignores;
import coloursplash.lupusclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class Ignore extends Command {
    public Ignore() {
        super("ignore", "Lets you ignore messages from specific players.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("username", StringArgumentType.string()).executes(context -> {
            String username = context.getArgument("username", String.class);

            if (Ignores.get().remove(username)) {
                ChatUtils.prefixInfo("Ignore","Removed (highlight)%s (default)from list of ignored people.", username);
            } else {
                Ignores.get().add(username);
                ChatUtils.prefixInfo("Ignore","Added (highlight)%s (default)to list of ignored people.", username);
            }

            return SINGLE_SUCCESS;
        })).executes(context -> {
            ChatUtils.prefixInfo("Ignore","Ignoring (highlight)%d (default)people:", Ignores.get().count());
            for (String player : Ignores.get()) {
                ChatUtils.info("- (highlight)%s", player);
            }

            return SINGLE_SUCCESS;
        });
    }
}
