/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import coloursplash.lupusclient.commands.commands.Baritone;
import coloursplash.lupusclient.commands.commands.Bind;
import coloursplash.lupusclient.commands.commands.ClearChat;
import coloursplash.lupusclient.commands.commands.Damage;
import coloursplash.lupusclient.commands.commands.Dismount;
import coloursplash.lupusclient.commands.commands.Drop;
import coloursplash.lupusclient.commands.commands.Enchant;
import coloursplash.lupusclient.commands.commands.Excavate;
import coloursplash.lupusclient.commands.commands.FakePlayerCommand;
import coloursplash.lupusclient.commands.commands.Friend;
import coloursplash.lupusclient.commands.commands.HClip;
import coloursplash.lupusclient.commands.commands.Help;
import coloursplash.lupusclient.commands.commands.Ignore;
import coloursplash.lupusclient.commands.commands.Inventory;
import coloursplash.lupusclient.commands.commands.NBT;
import coloursplash.lupusclient.commands.commands.Panic;
import coloursplash.lupusclient.commands.commands.Peek;
import coloursplash.lupusclient.commands.commands.Plugins;
import coloursplash.lupusclient.commands.commands.Profile;
import coloursplash.lupusclient.commands.commands.Reload;
import coloursplash.lupusclient.commands.commands.Reset;
import coloursplash.lupusclient.commands.commands.Say;
import coloursplash.lupusclient.commands.commands.Server;
import coloursplash.lupusclient.commands.commands.SettingCommand;
import coloursplash.lupusclient.commands.commands.Teleport;
import coloursplash.lupusclient.commands.commands.Toggle;
import coloursplash.lupusclient.commands.commands.VClip;
import coloursplash.lupusclient.commands.commands.swarm.SwarmCloseConnections;
import coloursplash.lupusclient.commands.commands.swarm.SwarmEscape;
import coloursplash.lupusclient.commands.commands.swarm.SwarmFollow;
import coloursplash.lupusclient.commands.commands.swarm.SwarmGoto;
import coloursplash.lupusclient.commands.commands.swarm.SwarmInfinityMiner;
import coloursplash.lupusclient.commands.commands.swarm.SwarmMine;
import coloursplash.lupusclient.commands.commands.swarm.SwarmModuleToggle;
import coloursplash.lupusclient.commands.commands.swarm.SwarmQueen;
import coloursplash.lupusclient.commands.commands.swarm.SwarmRelease;
import coloursplash.lupusclient.commands.commands.swarm.SwarmScatter;
import coloursplash.lupusclient.commands.commands.swarm.SwarmSlave;
import coloursplash.lupusclient.commands.commands.swarm.SwarmStop;
import coloursplash.lupusclient.systems.System;
import coloursplash.lupusclient.systems.Systems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;

public class Commands extends System<Commands> {
    private final CommandDispatcher<CommandSource> DISPATCHER = new CommandDispatcher<>();
    private final CommandSource COMMAND_SOURCE = new ChatCommandSource(MinecraftClient.getInstance());
    private final List<Command> commands = new ArrayList<>();
    private final Map<Class<? extends Command>, Command> commandInstances = new HashMap<>();

    public Commands() {
        super(null);
    }

    public static Commands get() {
        return Systems.get(Commands.class);
    }

    @Override
    public void init() {
        add(new Baritone());
        add(new Bind());
        add(new VClip());
        add(new HClip());
        add(new ClearChat());
        add(new Dismount());
        add(new Damage());
        add(new Drop());
        add(new Enchant());
        add(new FakePlayerCommand());
        add(new Friend());
        add(new Help());
        add(new Ignore());
        add(new Inventory());
        add(new NBT());
        add(new Panic());
        add(new Peek());
        add(new Plugins());
        add(new Profile());
        add(new Reload());
        add(new Reset());
        add(new Say());
        add(new Server());
        add(new SwarmModuleToggle());
        add(new SwarmQueen());
        add(new SwarmSlave());
        add(new SwarmEscape());
        add(new SwarmGoto());
        add(new SwarmFollow());
        add(new SwarmScatter());
        add(new SwarmMine());
        add(new SwarmInfinityMiner());
        add(new SwarmRelease());
        add(new SwarmStop());
        add(new SwarmCloseConnections());
        add(new Toggle());
        add(new SettingCommand());
        add(new Teleport());
        add(new Excavate());
    }

    public void dispatch(String message) throws CommandSyntaxException {
        dispatch(message, new ChatCommandSource(MinecraftClient.getInstance()));
    }

    public void dispatch(String message, CommandSource source) throws CommandSyntaxException {
        ParseResults<CommandSource> results = DISPATCHER.parse(message, source);
        // `results` carries information about whether or not the command failed to parse, which path was took, etc.
        // it might be useful to inspect later, before executing.
        DISPATCHER.execute(results);
    }

    public CommandDispatcher<CommandSource> getDispatcher() {
        return DISPATCHER;
    }

    public CommandSource getCommandSource() {
        return COMMAND_SOURCE;
    }

    private final static class ChatCommandSource extends ClientCommandSource {
        public ChatCommandSource(MinecraftClient client) {
            super(null, client);
        }
    }

    public void add(Command command) {
        // Remove the previous command with the same name
        commands.removeIf(command1 -> command1.getName().equals(command.getName()));
        commandInstances.values().removeIf(command1 -> command1.getName().equals(command.getName()));

        // Add the command
        command.registerTo(DISPATCHER);
        commands.add(command);
        commandInstances.put(command.getClass(), command);
    }

    public int getCount() {
        return commands.size();
    }

    public void forEach(Consumer<Command> consumer) {
        commands.forEach(consumer);
    }

    @SuppressWarnings("unchecked")
    public <T extends Command> T get(Class<T> klass) {
        return (T) commandInstances.get(klass);
    }
}
