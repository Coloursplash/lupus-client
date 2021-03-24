/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.friends.Friends;
import coloursplash.lupusclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.minecraft.command.CommandSource.suggestMatching;

public class Friend extends Command {
    public Friend() {
        super("friend", "Manages friends.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add")
                .then(argument("friend", FriendArgumentType.friend())
                        .executes(context -> {
                            coloursplash.lupusclient.friends.Friend friend =
                                    context.getArgument("friend", coloursplash.lupusclient.friends.Friend.class);
                            if (Friends.get().add(friend)) {
                                ChatUtils.prefixInfo("Friends","Added (highlight)%s (default)to friends.", friend.name);
                            } else {
                                ChatUtils.prefixError("Friends","That person is already your friend.");
                            }

                            return SINGLE_SUCCESS;
                        })))
                .then(literal("remove").then(argument("friend", FriendArgumentType.friend())
                        .executes(context -> {
                            coloursplash.lupusclient.friends.Friend friend =
                                    context.getArgument("friend", coloursplash.lupusclient.friends.Friend.class);
                            if (Friends.get().remove(friend)) {
                                ChatUtils.prefixInfo("Friends","Removed (highlight)%s (default)from friends.", friend.name);
                            } else {
                                ChatUtils.prefixError("Friends", "That person is not your friend.");
                            }

                            return SINGLE_SUCCESS;
                        })))
                .then(literal("list").executes(context -> {
                    ChatUtils.prefixInfo("Friends","You have (highlight)%d (default)friends:", Friends.get().count());

                    for (coloursplash.lupusclient.friends.Friend friend : Friends.get()) {
                        ChatUtils.info(" - (highlight)%s", friend.name);
                    }

                    return SINGLE_SUCCESS;
                }));
    }

    private static class FriendArgumentType implements ArgumentType<coloursplash.lupusclient.friends.Friend> {

        public static FriendArgumentType friend() {
            return new FriendArgumentType();
        }

        @Override
        public coloursplash.lupusclient.friends.Friend parse(StringReader reader) throws CommandSyntaxException {
            return new coloursplash.lupusclient.friends.Friend(reader.readString());
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return suggestMatching(mc.getNetworkHandler().getPlayerList().stream()
                    .map(entry -> entry.getProfile().getName()).collect(Collectors.toList()), builder);
        }

        @Override
        public Collection<String> getExamples() {
            // :)
            return Arrays.asList("086", "seasnail8169", "squidoodly");
        }
    }

}
