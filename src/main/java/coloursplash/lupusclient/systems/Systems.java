/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.systems;

import coloursplash.lupusclient.Config;
import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.accounts.Accounts;
import coloursplash.lupusclient.commands.Commands;
import coloursplash.lupusclient.friends.Friends;
import coloursplash.lupusclient.macros.Macros;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.waypoints.Waypoints;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Systems {
    @SuppressWarnings("rawtypes")
    private static final Map<Class<? extends System>, System<?>> systems = new HashMap<>();

    private static final List<Runnable> preLoadTasks = new ArrayList<>(1);
    private static System<?> config;

    public static void init() {
        config = add(new Config());
        config.load();
        config.init();

        add(new Modules());
        add(new Commands());
        add(new Friends());
        add(new Macros());
        add(new Accounts());
        add(new Waypoints());
        add(new Ignores());

        for (System<?> system : systems.values()) {
            if (system != config) system.init();
        }
    }

    private static System<?> add(System<?> system) {
        systems.put(system.getClass(), system);
        LupusClient.EVENT_BUS.subscribe(system);

        return system;
    }

    public static void save(File folder) {
        LupusClient.LOG.info("Saving");
        long start = java.lang.System.currentTimeMillis();

        for (System<?> system : systems.values()) system.save(folder);

        LupusClient.LOG.info("Saved in {} milliseconds", java.lang.System.currentTimeMillis() - start);
    }
    public static void save() {
        save(null);
    }

    public static void addPreLoadTask(Runnable task) {
        preLoadTasks.add(task);
    }

    public static void load(File folder) {
        LupusClient.LOG.info("Loading");
        long start = java.lang.System.currentTimeMillis();

        for (Runnable task : preLoadTasks) task.run();

        for (System<?> system : systems.values()) {
            if (system != config) system.load(folder);
        }

        LupusClient.LOG.info("Loaded in {} milliseconds", java.lang.System.currentTimeMillis() - start);
    }
    public static void load() {
        load(null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends System<?>> T get(Class<T> klass) {
        return (T) systems.get(klass);
    }
}
