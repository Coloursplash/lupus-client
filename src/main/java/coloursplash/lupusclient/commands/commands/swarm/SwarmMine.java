/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.commands.swarm;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.misc.Swarm;
import coloursplash.lupusclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class SwarmMine extends Command {

    public SwarmMine() {
        super("swarm", "(highlight)mine <playername>(default) - Baritone Mine A Block");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("mine")
                .then(argument("block", BlockStateArgumentType.blockState())
                        .executes(context -> {
                            try {
                                Swarm swarm = Modules.get().get(Swarm.class);
                                if (swarm.isActive()) {
                                    if (swarm.currentMode == Swarm.Mode.Queen && swarm.server != null)
                                        swarm.server.sendMessage(context.getInput());
                                    if (swarm.currentMode != Swarm.Mode.Queen) {
                                        swarm.targetBlock = context.getArgument("block",BlockStateArgument.class).getBlockState();
                                    } else ChatUtils.moduleError(Modules.get().get(Swarm.class),"Null block");
                                }
                            } catch (Exception e) {
                                ChatUtils.moduleError(Modules.get().get(Swarm.class),"Error in baritone command. " + e.getClass().getSimpleName());
                            }
                            return SINGLE_SUCCESS;
                        })
                )
        );
    }
}
