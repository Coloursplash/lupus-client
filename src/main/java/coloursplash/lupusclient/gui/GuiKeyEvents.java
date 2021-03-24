/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui;

public class GuiKeyEvents {
    public static int postKeyEvents = 0;

    public static void setPostKeyEvents(boolean post) {
        postKeyEvents += post ? 1 : -1;
    }
    public static boolean postKeyEvents() {
        return postKeyEvents <= 0;
    }
    public static void resetPostKeyEvents() {
        postKeyEvents = 0;
    }
}
