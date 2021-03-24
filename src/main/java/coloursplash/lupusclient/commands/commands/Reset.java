/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import coloursplash.lupusclient.Config;
import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.commands.arguments.ModuleArgumentType;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class Reset extends Command {

    public Reset() {
        super("reset", "Resets specified settings.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("settings")
                .then(argument("module", ModuleArgumentType.module()).executes(context -> {
                    Module module = context.getArgument("module", Module.class);
                    module.settings.forEach(group -> group.forEach(Setting::reset));
                    return SINGLE_SUCCESS;
                }))
                .then(literal("all").executes(context -> {
                    Modules.get().getAll().forEach(module -> module.settings.forEach(group -> group.forEach(Setting::reset)));
                    return SINGLE_SUCCESS;
                }))
        ).then(literal("gui").executes(context -> {
            Config.get().guiConfig.clearWindowConfigs();
            ChatUtils.info("The ClickGUI positioning has been reset.");
            return SINGLE_SUCCESS;
        })).then(literal("bind")
                .then(argument("module", ModuleArgumentType.module()).executes(context -> {
                    Module module = context.getArgument("module", Module.class);

                    module.setKey(-1);
                    ChatUtils.prefixInfo("KeyBinds","This bind has been reset.");

                    return SINGLE_SUCCESS;
                }))
                .then(literal("all").executes(context -> {
                    Modules.get().getAll().forEach(module -> module.setKey(-1));
                    return SINGLE_SUCCESS;
                }))
        );
    }
}
