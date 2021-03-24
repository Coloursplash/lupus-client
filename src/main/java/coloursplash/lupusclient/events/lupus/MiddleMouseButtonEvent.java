/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.events.lupus;

public class MiddleMouseButtonEvent {
    private static final MiddleMouseButtonEvent INSTANCE = new MiddleMouseButtonEvent();

    public static MiddleMouseButtonEvent get() {
        return INSTANCE;
    }
}
