/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.waypoints.gui;

import coloursplash.lupusclient.gui.renderer.GuiRenderer;
import coloursplash.lupusclient.gui.widgets.WWidget;
import coloursplash.lupusclient.waypoints.Waypoint;

public class WWaypointIcon extends WWidget {
    private final Waypoint waypoint;

    public WWaypointIcon(Waypoint waypoint) {
        this.waypoint = waypoint;
    }

    @Override
    protected void onCalculateSize(GuiRenderer renderer) {
        width = 32;
        height = 32;
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.post(() -> waypoint.renderIcon(x, y, 0, 1, 32));
    }
}
