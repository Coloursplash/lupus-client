/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.commands.swarm;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.commands.arguments.ModuleArgumentType;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.misc.Swarm;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class SwarmModuleToggle extends Command {

    public SwarmModuleToggle() {
        super("swarm", "(highlight)module <module> <true/false>(default) - Toggle a module on or off.");
    }


    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("module").then(argument("m", ModuleArgumentType.module()).then(argument("bool", BoolArgumentType.bool()).executes(context -> {
            Swarm swarm = Modules.get().get(Swarm.class);
            if (swarm.currentMode == Swarm.Mode.Queen && swarm.server != null) {
                swarm.server.sendMessage(context.getInput());
            } else {
                Module module = context.getArgument("m", Module.class);
                if (module.isActive() != context.getArgument("bool", Boolean.class)) {
                    module.toggle();
                }
            }
            return SINGLE_SUCCESS;
        }))));
    }
}
