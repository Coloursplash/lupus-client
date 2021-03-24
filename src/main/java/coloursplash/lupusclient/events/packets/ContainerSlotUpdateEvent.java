/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.events.packets;

import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;

public class ContainerSlotUpdateEvent {
    private static final ContainerSlotUpdateEvent INSTANCE = new ContainerSlotUpdateEvent();

    public ScreenHandlerSlotUpdateS2CPacket packet;

    public static ContainerSlotUpdateEvent get(ScreenHandlerSlotUpdateS2CPacket packet) {
        INSTANCE.packet = packet;
        return INSTANCE;
    }
}
