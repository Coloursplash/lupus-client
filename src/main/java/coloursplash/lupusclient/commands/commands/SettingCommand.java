/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.commands.arguments.ModuleArgumentType;
import coloursplash.lupusclient.commands.arguments.SettingArgumentType;
import coloursplash.lupusclient.commands.arguments.SettingValueArgumentType;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static coloursplash.lupusclient.commands.arguments.SettingArgumentType.getSetting;

public class SettingCommand extends Command {
    public SettingCommand() {
        super("s", "Allows you to view and change module settings.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                argument("module", ModuleArgumentType.module())
                .then(
                        argument("setting", SettingArgumentType.setting())
                        .executes(context -> {
                            // Get setting value
                            Setting<?> setting = getSetting(context);

                            ChatUtils.info("Setting (highlight)%s(default) is (highlight)%s(default).", setting.title, setting.get());

                            return SINGLE_SUCCESS;
                        })
                        .then(
                                argument("value", SettingValueArgumentType.value())
                                .executes(context -> {
                                    // Set setting value
                                    Setting<?> setting = getSetting(context);
                                    String value = context.getArgument("value", String.class);

                                    if (setting.parse(value)) {
                                        ChatUtils.info("Setting (highlight)%s(default) changed to (highlight)%s(default).", setting.title, value);
                                    }

                                    return SINGLE_SUCCESS;
                                })
                        )
                )
        );
    }
}
