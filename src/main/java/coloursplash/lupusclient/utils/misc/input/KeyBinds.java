/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.utils.misc.input;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {
    private static final String CATEGORY = "Lupus Client";

    public static KeyBinding OPEN_CLICK_GUI = new KeyBinding("key.lupus-client.open-click-gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, CATEGORY);
    public static KeyBinding SHULKER_PEEK = new KeyBinding("key.lupus-client.shulker-peek", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, CATEGORY);

    public static void Register() {
        KeyBindingHelper.registerKeyBinding(OPEN_CLICK_GUI);
        KeyBindingHelper.registerKeyBinding(SHULKER_PEEK);
    }
}
