/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.events.entity;

import net.minecraft.entity.Entity;

public class EntityDestroyEvent {
    private static final EntityDestroyEvent INSTANCE = new EntityDestroyEvent();

    public Entity entity;

    public static EntityDestroyEvent get(Entity entity) {
        INSTANCE.entity = entity;
        return INSTANCE;
    }
}
