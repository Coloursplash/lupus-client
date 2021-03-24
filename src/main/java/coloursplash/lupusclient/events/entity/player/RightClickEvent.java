/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.events.entity.player;

public class RightClickEvent {
    private static final RightClickEvent INSTANCE = new RightClickEvent();

    public static RightClickEvent get() {
        return INSTANCE;
    }
}
