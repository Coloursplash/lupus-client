/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.utils.misc;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

public class PlayerListEntryFactory extends PlayerListS2CPacket {
    private static final PlayerListEntryFactory INSTANCE = new PlayerListEntryFactory();

    private Entry _create(GameProfile profile, int latency, GameMode gameMode, Text displayName) {
        return new Entry(profile, latency, gameMode, displayName);
    }

    public static Entry create(GameProfile profile, int latency, GameMode gameMode, Text displayName) {
        return INSTANCE._create(profile, latency, gameMode, displayName);
    }
}
