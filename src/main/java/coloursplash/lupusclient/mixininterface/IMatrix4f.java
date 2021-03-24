/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixininterface;

import coloursplash.lupusclient.utils.misc.Vec4;

public interface IMatrix4f {
    void multiplyMatrix(Vec4 v, Vec4 out);
}
