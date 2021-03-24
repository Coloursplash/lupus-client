/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens.topbar;

import coloursplash.lupusclient.gui.widgets.WButton;
import coloursplash.lupusclient.utils.Utils;
import coloursplash.lupusclient.waypoints.Waypoint;
import coloursplash.lupusclient.waypoints.Waypoints;
import coloursplash.lupusclient.waypoints.gui.EditWaypointScreen;
import coloursplash.lupusclient.waypoints.gui.WWaypoint;
import net.minecraft.client.MinecraftClient;

public class TopBarWaypoints extends TopBarWindowScreen {
    public TopBarWaypoints() {
        super(TopBarType.Waypoints);

        refreshWidgetsOnInit = true;
    }

    @Override
    protected void initWidgets() {
        // Waypoints
        for (Waypoint waypoint : Waypoints.get()) {
            add(new WWaypoint(waypoint, () -> {
                clear();
                initWidgets();
            })).fillX().expandX();
            row();
        }

        // Add
        if (Utils.canUpdate()) {
            WButton add = add(new WButton("Add")).fillX().expandX().getWidget();
            add.action = () -> MinecraftClient.getInstance().openScreen(new EditWaypointScreen(null));
        }
    }
}
