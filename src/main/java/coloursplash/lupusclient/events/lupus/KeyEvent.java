/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.events.lupus;

import coloursplash.lupusclient.events.Cancellable;
import coloursplash.lupusclient.utils.misc.input.KeyAction;

public class KeyEvent extends Cancellable {
    private static final KeyEvent INSTANCE = new KeyEvent();

    public int key;
    public KeyAction action;

    public static KeyEvent get(int key, KeyAction action) {
        INSTANCE.setCancelled(false);
        INSTANCE.key = key;
        INSTANCE.action = action;
        return INSTANCE;
    }
}
