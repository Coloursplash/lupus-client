/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.waypoints.gui;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.pathing.goals.GoalGetToBlock;
import coloursplash.lupusclient.gui.widgets.*;
import coloursplash.lupusclient.utils.Utils;
import coloursplash.lupusclient.utils.render.color.Color;
import coloursplash.lupusclient.utils.world.Dimension;
import coloursplash.lupusclient.waypoints.Waypoint;
import coloursplash.lupusclient.waypoints.Waypoints;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class WWaypoint extends WTable {
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color GRAY = new Color(200, 200, 200);

    public WWaypoint(Waypoint waypoint, Runnable onRemoved) {
        // Icon
        add(new WWaypointIcon(waypoint));

        // Name
        WLabel name = add(new WLabel(waypoint.name)).getWidget();
        boolean goodDimension = false;
        Dimension dimension = Utils.getDimension();
        if (waypoint.overworld && dimension == Dimension.Overworld) goodDimension = true;
        else if (waypoint.nether && dimension == Dimension.Nether) goodDimension = true;
        else if (waypoint.end && dimension == Dimension.End) goodDimension = true;
        name.color = goodDimension ? WHITE : GRAY;

        // Visible, edit, remove
        WTable right = add(new WTable()).fillX().right().getWidget();
        WCheckbox visible = right.add(new WCheckbox(waypoint.visible)).getWidget();
        visible.action = () -> {
            waypoint.visible = visible.checked;
            Waypoints.get().save();
        };
        right.add(new WButton(WButton.ButtonRegion.Edit)).getWidget().action = () -> MinecraftClient.getInstance().openScreen(new EditWaypointScreen(waypoint));
        right.add(new WMinus()).getWidget().action = () -> {
            Waypoints.get().remove(waypoint);
            if (onRemoved != null) onRemoved.run();
        };

        // Goto
        if (waypoint.actualDimension == Utils.getDimension()) {
            WButton path = new WButton("Goto");
            path.action = () -> {
                if (MinecraftClient.getInstance().player == null || MinecraftClient.getInstance().world == null) return;
                IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
                if (baritone.getPathingBehavior().isPathing()) baritone.getPathingBehavior().cancelEverything();
                Vec3d vec = Waypoints.get().getCoords(waypoint);
                BlockPos pos = new BlockPos(vec.x, vec.y, vec.z);
                baritone.getCustomGoalProcess().setGoalAndPath(new GoalGetToBlock(pos));
            };
            right.add(path);
        }
    }
}
