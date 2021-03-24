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

public class SwarmRelease extends Command {

    public SwarmRelease() {
        super("swarm", "(highlight)release(default) - Release your bots.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("release").executes(context -> {
                    Swarm swarm = Modules.get().get(Swarm.class);
                    if (swarm.isActive()) {
                        if (swarm.currentMode == Swarm.Mode.Queen && swarm.server != null) {
                            swarm.server.sendMessage("s stop");
                            swarm.server.closeAllClients();
                        }
                    }
                    return SINGLE_SUCCESS;
                })
        );
    }
}
