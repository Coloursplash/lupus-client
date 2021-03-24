/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.events.entity.player;

import coloursplash.lupusclient.events.Cancellable;

public class SendMessageEvent extends Cancellable {
    private static final SendMessageEvent INSTANCE = new SendMessageEvent();

    public String msg;

    public static SendMessageEvent get(String msg) {
        INSTANCE.setCancelled(false);
        INSTANCE.msg = msg;
        return INSTANCE;
    }
}


