/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.events.world;

import net.minecraft.block.BlockState;
import net.minecraft.util.shape.VoxelShape;

public class FluidCollisionShapeEvent {
    private static final FluidCollisionShapeEvent INSTANCE = new FluidCollisionShapeEvent();

    public BlockState state;
    public VoxelShape shape;

    public static FluidCollisionShapeEvent get(BlockState state) {
        INSTANCE.state = state;
        INSTANCE.shape = null;
        return INSTANCE;
    }
}
