/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import coloursplash.lupusclient.settings.Setting;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static coloursplash.lupusclient.commands.arguments.SettingArgumentType.getSetting;

public class SettingValueArgumentType implements ArgumentType<String> {
    public static SettingValueArgumentType value() {
        return new SettingValueArgumentType();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String text = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return text;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Setting<?> setting;

        try {
            setting = getSetting(context);
        } catch (CommandSyntaxException ignored) {
            return null;
        }

        Iterable<Identifier> identifiers = setting.getIdentifierSuggestions();
        if (identifiers != null) {
            return CommandSource.suggestIdentifiers(identifiers, builder);
        }

        return CommandSource.suggestMatching(setting.getSuggestions(), builder);
    }
}
