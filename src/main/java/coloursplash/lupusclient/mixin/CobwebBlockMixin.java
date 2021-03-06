/*
* This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
* Copyright (c) 2021 Lupus Development.
*/
package coloursplash.lupusclient.mixin;

import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.movement.NoSlow;
import net.minecraft.block.BlockState;
import net.minecraft.block.CobwebBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CobwebBlock.class)
public class CobwebBlockMixin {
    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    private void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo info) {
        if (Modules.get().get(NoSlow.class).web() && entity == MinecraftClient.getInstance().player) info.cancel();
    }
}
