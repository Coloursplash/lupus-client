/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.commands.swarm;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.misc.Swarm;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class SwarmQueen extends Command {

    public SwarmQueen() {
        super("swarm", "(highlight)queen (default)- Start a new swarm as the Queen.");
    }


    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("queen").executes(context -> {
                    Swarm swarm = Modules.get().get(Swarm.class);
                    if (swarm.isActive()) {
                        if (swarm.server == null)
                            swarm.runServer();
                    }
                    return SINGLE_SUCCESS;
                })

        );
    }
}
