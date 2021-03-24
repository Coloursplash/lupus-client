/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.commands.arguments.ModuleArgumentType;
import coloursplash.lupusclient.modules.Module;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class Toggle extends Command {


    public Toggle() {
        super("toggle", "Toggles a module.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("module", ModuleArgumentType.module())
                .executes(context -> {
                    Module m = context.getArgument("module", Module.class);
                    m.toggle();
                    m.sendToggledMsg();
                    return SINGLE_SUCCESS;
                }).then(literal("on")
                        .executes(context -> {
                            Module m = context.getArgument("module", Module.class);
                            if (!m.isActive()) m.toggle(); m.sendToggledMsg();
                            return SINGLE_SUCCESS;
                        })).then(literal("off")
                        .executes(context -> {
                            Module m = context.getArgument("module", Module.class);
                            if (m.isActive()) m.toggle(); m.sendToggledMsg();
                            return SINGLE_SUCCESS;
                        })
                )
        );
    }
}
