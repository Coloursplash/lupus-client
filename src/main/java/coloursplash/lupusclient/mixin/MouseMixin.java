/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixin;

import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.events.entity.player.RightClickEvent;
import coloursplash.lupusclient.events.lupus.MiddleMouseButtonEvent;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.render.FreeRotate;
import coloursplash.lupusclient.modules.render.Freecam;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton", at = @At("TAIL"))
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo info) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE && action == GLFW.GLFW_PRESS) {
            LupusClient.EVENT_BUS.post(MiddleMouseButtonEvent.get());
        } else if((button == GLFW.GLFW_MOUSE_BUTTON_2) && (action == GLFW.GLFW_PRESS)) {
            LupusClient.EVENT_BUS.post(RightClickEvent.get());
        }
    }

    @Redirect(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
    private void updateMouseChangeLookDirection(ClientPlayerEntity player, double cursorDeltaX, double cursorDeltaY) {
        Freecam freecam = Modules.get().get(Freecam.class);
        FreeRotate freeRotate = Modules.get().get(FreeRotate.class);

        if (freecam.isActive()) freecam.changeLookDirection(cursorDeltaX * 0.15, cursorDeltaY * 0.15);
        else if (!freeRotate.cameraMode()) player.changeLookDirection(cursorDeltaX, cursorDeltaY);
    }

    @Inject(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onUpdateMouse(DD)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void perspectiveUpdatePitchYaw(CallbackInfo info, double adjustedSens, double x, double y, int invert) {
        FreeRotate freeRotate = Modules.get().get(FreeRotate.class);
        if (freeRotate.cameraMode()) {
            freeRotate.cameraYaw += x / freeRotate.sensitivity.get().floatValue();
            freeRotate.cameraPitch += (y * invert) / freeRotate.sensitivity.get().floatValue();
            if (Math.abs(freeRotate.cameraPitch) > 90.0F) freeRotate.cameraPitch = freeRotate.cameraPitch > 0.0F ? 90.0F : -90.0F;
        }
    }
}
