/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.utils.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.events.game.GameLeftEvent;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.json.UUIDSerializer;
import coloursplash.lupusclient.utils.network.HttpUtils;
import coloursplash.lupusclient.utils.network.MeteorExecutor;
import net.minecraft.entity.player.PlayerEntity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MeteorPlayers {
    private static final Type uuidBooleanMapType = new TypeToken<Map<UUID, Boolean>>() {}.getType();

    private static final Object2BooleanMap<UUID> players = new Object2BooleanOpenHashMap<>();

    private static final List<UUID> toCheck = new ArrayList<>();
    private static int checkTimer;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(UUID.class, new UUIDSerializer())
            .create();

    public static void init() {
        LupusClient.EVENT_BUS.subscribe(MeteorPlayers.class);
    }

    @EventHandler
    private static void onGameLeft(GameLeftEvent event) {
        synchronized (players) {
            players.clear();
        }

        synchronized (toCheck) {
            toCheck.clear();
        }
    }

    @EventHandler
    private static void onTick(TickEvent.Post event) {
        if (toCheck.isEmpty()) return;

        if (checkTimer >= 10) {
            checkTimer = 0;

            MeteorExecutor.execute(MeteorPlayers::check);
        }
        else {
            checkTimer++;
        }
    }

    private static void check() {
        String body;
        synchronized (toCheck) {
            body = gson.toJson(toCheck);
            toCheck.clear();
        }

        InputStream in = HttpUtils.post("http://meteorclient.com/api/online/usingLupus", body);

        Map<UUID, Boolean> uuids = gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), uuidBooleanMapType);

        synchronized (players) {
            for (UUID uuid : uuids.keySet()) {
                players.put(uuid, uuids.get(uuid));
            }
        }
    }

    public static boolean get(PlayerEntity player) {
        UUID uuid = player.getUuid();

        if (players.containsKey(uuid)) {
            return players.getBoolean(uuid);
        }

        synchronized (toCheck) {
            toCheck.add(uuid);
        }

        synchronized (players) {
            players.put(uuid, false);
        }

        return false;
    }
}
