/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.events.entity.player;

import net.minecraft.util.hit.HitResult;

public class ItemUseCrosshairTargetEvent {
    private static final ItemUseCrosshairTargetEvent INSTANCE = new ItemUseCrosshairTargetEvent();

    public HitResult target;

    public static ItemUseCrosshairTargetEvent get(HitResult target) {
        INSTANCE.target = target;
        return INSTANCE;
    }
}
