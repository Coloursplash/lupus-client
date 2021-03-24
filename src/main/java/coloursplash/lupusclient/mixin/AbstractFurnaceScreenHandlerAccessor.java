/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractFurnaceScreenHandler.class)
public interface AbstractFurnaceScreenHandlerAccessor {
    @Invoker("isSmeltable")
    boolean isSmeltable(ItemStack itemStack);

    @Invoker("isFuel")
    boolean isFuel(ItemStack itemStack);
}
