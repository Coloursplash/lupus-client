/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.commands.swarm;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalXZ;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.misc.Swarm;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class SwarmGoto extends Command {

    public SwarmGoto() {
        super("swarm", "(highlight)goto <x> <z>(default) - Path to a destination.");
    }

    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("goto")
                .then(argument("x", IntegerArgumentType.integer())
                        .then(argument("z", IntegerArgumentType.integer()).executes(context -> {
                                    int x = context.getArgument("x", Integer.class);
                                    int z = context.getArgument("z", Integer.class);
                                    Swarm swarm = Modules.get().get(Swarm.class);
                                    if (swarm.isActive()) {
                                        if (swarm.currentMode == Swarm.Mode.Queen && swarm.server != null) {
                                            swarm.server.sendMessage(context.getInput());
                                        } else if (swarm.currentMode != Swarm.Mode.Queen) {
                                            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalXZ(x, z));
                                        }
                                    }
                                    return SINGLE_SUCCESS;
                                })
                        )
                )
        );
    }

}
