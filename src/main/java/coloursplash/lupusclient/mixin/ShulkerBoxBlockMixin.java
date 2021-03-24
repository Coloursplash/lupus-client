/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixin;

import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.render.ShulkerPeek;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxBlockMixin {
    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    private void onAppendTooltip(ItemStack stack, BlockView view, List<Text> tooltip, TooltipContext options, CallbackInfo info) {
        if (Modules.get() != null && Modules.get().isActive(ShulkerPeek.class)) info.cancel();
    }
}
