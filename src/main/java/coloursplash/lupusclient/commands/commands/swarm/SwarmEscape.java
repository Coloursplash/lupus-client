/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.commands.swarm;

import baritone.api.BaritoneAPI;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.misc.Swarm;
import coloursplash.lupusclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class SwarmEscape extends Command {

    public SwarmEscape() {
        super("swarm", "(highlight)escape(default) - Removes this player from the active swarm.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("escape").executes(context -> {
                    Swarm swarm = Modules.get().get(Swarm.class);
                    if (swarm.isActive()) {
                        if (swarm.currentMode != Swarm.Mode.Queen) {
                            swarm.closeAllServerConnections();
                            if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing())
                                BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
                            swarm.currentMode = Swarm.Mode.Idle;
                            Modules.get().get(Swarm.class).toggle();
                        } else {
                            ChatUtils.moduleInfo(Modules.get().get(Swarm.class), "You are the queen.");
                        }
                    }
                    return SINGLE_SUCCESS;
                })
        );
    }
}
