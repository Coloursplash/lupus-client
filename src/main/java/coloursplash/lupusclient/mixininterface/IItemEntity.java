/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixininterface;

import net.minecraft.util.math.Vec3d;

public interface IItemEntity {
    Vec3d getRotation();
    void setRotation(Vec3d rotation);
}