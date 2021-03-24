/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.modules.Modules;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class Panic extends Command {
    public Panic() {
        super("panic", "Disables all modules. DOES NOT remove keybinds.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            new ArrayList<>(Modules.get().getActive()).forEach(Module::toggle);

            return SINGLE_SUCCESS;
        });
    }
}
