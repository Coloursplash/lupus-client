/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.commands.arguments.ModuleArgumentType;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class Bind extends Command {
    public Bind() {
        super("bind", "Binds a module to a specified key.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("module", ModuleArgumentType.module())
                .executes(context -> {
                    Module m = context.getArgument("module", Module.class);

                    ChatUtils.prefixInfo("KeyBinds","Press a key you want this module to be bound to.");
                    Modules.get().setModuleToBind(m);

                    return SINGLE_SUCCESS;
                }));
    }
}