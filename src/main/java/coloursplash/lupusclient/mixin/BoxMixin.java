/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixin;

import coloursplash.lupusclient.mixininterface.IBox;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Box.class)
public class BoxMixin implements IBox {
    @Shadow @Final @Mutable public double minX;
    @Shadow @Final @Mutable public double minY;
    @Shadow @Final @Mutable public double minZ;

    @Shadow @Final @Mutable public double maxX;
    @Shadow @Final @Mutable public double maxY;
    @Shadow @Final @Mutable public double maxZ;

    @Override
    public void expand(double v) {
        this.minX -= v;
        this.minY -= v;
        this.minZ -= v;
        this.maxX += v;
        this.maxY += v;
        this.maxZ += v;
    }
}
