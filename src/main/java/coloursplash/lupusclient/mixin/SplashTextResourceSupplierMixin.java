/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixin;

import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SplashTextResourceSupplier.class)
public class SplashTextResourceSupplierMixin {
    @Inject(method = "apply", at = @At("HEAD"))
    private void onApply(List<String> list, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        list.add("Lupus on Crack!");
        list.add("Star Lupus Client on GitHub!");
        list.add("Based utility mod.");
        list.add("based");
        list.add(":EZ:");
        list.add(":kekw:");
        list.add("OK retard.");
        list.add("cat");
        list.add("snale");
        list.add("monkey");
        list.add("mingane");
        list.add("https://bigrat.monster");
        list.add("snale moment");
        list.add("inertia moment");
        list.add("squidoodly based god");
        list.add("r/squidoodly");
    }
}
