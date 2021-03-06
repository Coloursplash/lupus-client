/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.utils.misc.input;

import coloursplash.lupusclient.gui.GuiKeyEvents;
import coloursplash.lupusclient.utils.misc.CursorStyle;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class Input {
    private static final boolean[] keys = new boolean[512];

    private static CursorStyle lastCursorStyle = CursorStyle.Default;

    public static void setKeyState(int key, boolean pressed) {
        if (key >= 0 && key < keys.length) keys[key] = pressed;
    }

    public static void setKeyState(KeyBinding key, boolean pressed) {
        setKeyState(KeyBindingHelper.getBoundKeyOf(key).getCode(), pressed);
    }

    public static boolean isPressed(KeyBinding keyBinding) {
        int key = KeyBindingHelper.getBoundKeyOf(keyBinding).getCode();
        return isPressed(key);
    }

    public static boolean isPressed(int key) {
        if (!GuiKeyEvents.postKeyEvents()) return false;

        if (key == GLFW.GLFW_KEY_UNKNOWN) return false;
        return key < keys.length && keys[key];
    }

    public static void setCursorStyle(CursorStyle style) {
        if (lastCursorStyle != style) {
            GLFW.glfwSetCursor(MinecraftClient.getInstance().getWindow().getHandle(), style.getGlfwCursor());
            lastCursorStyle = style;
        }
    }
}
