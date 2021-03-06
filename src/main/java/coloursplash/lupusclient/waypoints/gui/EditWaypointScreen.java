/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.waypoints.gui;

import coloursplash.lupusclient.gui.screens.WindowScreen;
import coloursplash.lupusclient.gui.screens.settings.ColorSettingScreen;
import coloursplash.lupusclient.gui.widgets.*;
import coloursplash.lupusclient.settings.ColorSetting;
import coloursplash.lupusclient.utils.Utils;
import coloursplash.lupusclient.utils.world.Dimension;
import coloursplash.lupusclient.waypoints.Waypoint;
import coloursplash.lupusclient.waypoints.Waypoints;
import net.minecraft.client.MinecraftClient;

public class EditWaypointScreen extends WindowScreen {
    private final Waypoint waypoint;
    private final boolean newWaypoint;

    public EditWaypointScreen(Waypoint waypoint) {
        super(waypoint == null ? "New Waypoint" : "Edit Waypoint", true);

        this.newWaypoint = waypoint == null;
        this.waypoint = newWaypoint ? new Waypoint() : waypoint;

        if (newWaypoint) {
            MinecraftClient mc = MinecraftClient.getInstance();

            this.waypoint.x = (int) mc.player.getX();
            this.waypoint.y = (int) mc.player.getY() + 2;
            this.waypoint.z = (int) mc.player.getZ();

            this.waypoint.actualDimension = Utils.getDimension();

            switch (Utils.getDimension()) {
                case Overworld: this.waypoint.overworld = true; break;
                case Nether:    this.waypoint.nether = true; break;
                case End:       this.waypoint.end = true; break;
            }
        }

        initWidgets();
    }

    private void initWidgets() {
        // Name
        add(new WLabel("Name:"));
        WTextBox name = add(new WTextBox(waypoint.name, 400)).fillX().expandX().getWidget();
        name.action = () -> waypoint.name = name.getText().trim();
        row();

        // Icon
        add(new WLabel("Icon:"));
        WTable t = add(new WTable()).getWidget();
        t.add(new WButton("Prev")).getWidget().action = waypoint::prevIcon;
        t.add(new WWaypointIcon(waypoint));
        t.add(new WButton("Next")).getWidget().action = waypoint::nextIcon;
        row();

        // Color:
        add(new WLabel("Color:"));
        t = add(new WTable()).getWidget();
        t.add(new WQuad(waypoint.color));
        t.add(new WButton(WButton.ButtonRegion.Edit)).getWidget().action = () -> MinecraftClient.getInstance().openScreen(new ColorSettingScreen(new ColorSetting("", "", waypoint.color, color -> waypoint.color.set(color), null)));
        row();

        add(new WHorizontalSeparator());

        // X
        add(new WLabel("X:"));
        WIntTextBox x = add(new WIntTextBox(waypoint.x, 100)).getWidget();
        x.action = () -> waypoint.x = x.getValue();
        row();

        // Y
        add(new WLabel("Y:"));
        WIntTextBox y = add(new WIntTextBox(waypoint.y, 100)).getWidget();
        y.action = () -> {
            if (y.getValue() < 0) y.setValue(0);
            else if (y.getValue() > 255) y.setValue(255);

            waypoint.y = y.getValue();
        };
        row();

        // T
        add(new WLabel("Z:"));
        WIntTextBox z = add(new WIntTextBox(waypoint.z, 100)).getWidget();
        z.action = () -> waypoint.z = z.getValue();
        row();

        add(new WHorizontalSeparator());

        // Visible
        add(new WLabel("Visible:"));
        WCheckbox visible = add(new WCheckbox(waypoint.visible)).getWidget();
        visible.action = () -> waypoint.visible = visible.checked;
        row();

        // Max visible distance
        add(new WLabel("Max Visible Distance"));
        WIntEdit maxVisibleDist = add(new WIntEdit(waypoint.maxVisibleDistance, 0, 10000)).getWidget();
        maxVisibleDist.action = () -> waypoint.maxVisibleDistance = maxVisibleDist.get();
        row();

        // Scale
        add(new WLabel("Scale:"));
        WDoubleEdit scale = add(new WDoubleEdit(waypoint.scale, 0, 4)).getWidget();
        scale.action = () -> waypoint.scale = scale.get();

        add(new WHorizontalSeparator());

        add(new WLabel("Actual Dimension:"));
        WDropbox<Dimension> dimensionDropbox = add(new WDropbox<>(waypoint.actualDimension)).getWidget();
        dimensionDropbox.action = () -> waypoint.actualDimension = dimensionDropbox.getValue();
        row();

        // Overworld
        add(new WLabel("Visible in Overworld:"));
        WCheckbox overworld = add(new WCheckbox(waypoint.overworld)).getWidget();
        overworld.action = () -> waypoint.overworld = overworld.checked;
        row();

        // Nether
        add(new WLabel("Visible in Nether:"));
        WCheckbox nether = add(new WCheckbox(waypoint.nether)).getWidget();
        nether.action = () -> waypoint.nether = nether.checked;
        row();

        // End
        add(new WLabel("Visible in End:"));
        WCheckbox end = add(new WCheckbox(waypoint.end)).getWidget();
        end.action = () -> waypoint.end = end.checked;
        row();

        // Save
        add(new WButton("Save")).fillX().expandX().getWidget().action = () -> {
            if (newWaypoint) Waypoints.get().add(waypoint);
            else Waypoints.get().save();

            onClose();
        };
    }
}
