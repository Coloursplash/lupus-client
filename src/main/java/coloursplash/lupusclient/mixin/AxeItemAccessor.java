/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.AxeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(AxeItem.class)
public interface AxeItemAccessor {
    @Accessor("field_23139")
    static Set<Material> getEffectiveMaterials() { return null; }

    @Accessor("EFFECTIVE_BLOCKS")
    static Set<Block> getEffectiveBlocks() { return null; }
}
