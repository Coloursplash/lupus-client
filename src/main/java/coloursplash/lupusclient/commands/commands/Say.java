/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.commands;

//Created by squidoodly 18/04/2020

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import coloursplash.lupusclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class Say extends Command {

    public Say() {
        super("say", "Sends messages in chat.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("message", StringArgumentType.greedyString()).executes(context -> {
            mc.getNetworkHandler().sendPacket(new ChatMessageC2SPacket(context.getArgument("message", String.class)));
            return SINGLE_SUCCESS;
        }));
    }
}
