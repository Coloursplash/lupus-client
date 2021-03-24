/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixin;

import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.events.lupus.CharTypedEvent;
import coloursplash.lupusclient.events.lupus.KeyEvent;
import coloursplash.lupusclient.gui.GuiKeyEvents;
import coloursplash.lupusclient.gui.WidgetScreen;
import coloursplash.lupusclient.utils.Utils;
import coloursplash.lupusclient.utils.misc.input.Input;
import coloursplash.lupusclient.utils.misc.input.KeyAction;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int i, int j, CallbackInfo info) {
        if (key != GLFW.GLFW_KEY_UNKNOWN) {
            if (client.currentScreen instanceof WidgetScreen && i == GLFW.GLFW_REPEAT) {
                ((WidgetScreen) client.currentScreen).keyRepeated(key, j);
            }

            if (GuiKeyEvents.postKeyEvents()) {
                Input.setKeyState(key, i != GLFW.GLFW_RELEASE);

                KeyEvent event = LupusClient.EVENT_BUS.post(KeyEvent.get(key, KeyAction.get(i)));

                if (event.isCancelled()) info.cancel();
            }
        }
    }

    @Inject(method = "onChar", at = @At("HEAD"), cancellable = true)
    private void onChar(long window, int i, int j, CallbackInfo info) {
        if (Utils.canUpdate() && !client.isPaused() && (client.currentScreen == null || client.currentScreen instanceof WidgetScreen)) {
            CharTypedEvent event = LupusClient.EVENT_BUS.post(CharTypedEvent.get((char) i));

            if (event.isCancelled()) info.cancel();
        }
    }
}
