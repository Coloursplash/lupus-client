/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixin;

import coloursplash.lupusclient.mixininterface.IItemEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemEntity.class)
public class ItemEntityMixin implements IItemEntity {
    private Vec3d rotation = new Vec3d(0, 0, 0);

    @Override
    public Vec3d getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(Vec3d rotation) {
        this.rotation = rotation;
    }
}
