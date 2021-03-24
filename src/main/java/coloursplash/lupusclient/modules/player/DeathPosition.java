

package coloursplash.lupusclient.modules.player;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalXZ;
import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.entity.TookDamageEvent;
import coloursplash.lupusclient.gui.widgets.WButton;
import coloursplash.lupusclient.gui.widgets.WLabel;
import coloursplash.lupusclient.gui.widgets.WTable;
import coloursplash.lupusclient.gui.widgets.WWidget;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.utils.Utils;
import coloursplash.lupusclient.utils.player.ChatUtils;
import coloursplash.lupusclient.waypoints.Waypoint;
import coloursplash.lupusclient.waypoints.Waypoints;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DeathPosition extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> createWaypoint = sgGeneral.add(new BoolSetting.Builder()
            .name("create-waypoint")
            .description("Creates a waypoint when you die.")
            .defaultValue(true)
            .build()
    );

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final WLabel label = new WLabel("No latest death found.");

    public DeathPosition() {
        super(Categories.Player, "death-position", "Sends you the coordinates to your latest death.");
    }

    private final Map<String, Double> deathPos = new HashMap<>();
    private Waypoint waypoint;

    @SuppressWarnings("unused")
    @EventHandler
    private void onTookDamage(TookDamageEvent event) {
        if (mc.player == null) return;

        if (event.entity.getUuid() != null && event.entity.getUuid().equals(mc.player.getUuid()) && event.entity.getHealth() <= 0) {
            deathPos.put("x", mc.player.getX());
            deathPos.put("z", mc.player.getZ());
            label.setText(String.format("Latest death: %.1f, %.1f, %.1f", mc.player.getX(), mc.player.getY(), mc.player.getZ()));

            String time = dateFormat.format(new Date());
            ChatUtils.moduleInfo(this, "Died at (highlight)%.0f(default), (highlight)%.0f(default), (highlight)%.0f (default)on (highlight)%s(default).", mc.player.getX(), mc.player.getY(), mc.player.getZ(), time);

            // Create waypoint
            if (createWaypoint.get()) {
                waypoint = new Waypoint();
                waypoint.name = "Death " + time;

                waypoint.x = (int) mc.player.getX();
                waypoint.y = (int) mc.player.getY() + 2;
                waypoint.z = (int) mc.player.getZ();
                waypoint.maxVisibleDistance = Integer.MAX_VALUE;
                waypoint.actualDimension = Utils.getDimension();

                switch (Utils.getDimension()) {
                    case Overworld:
                        waypoint.overworld = true;
                        break;
                    case Nether:
                        waypoint.nether = true;
                        break;
                    case End:
                        waypoint.end = true;
                        break;
                }

                Waypoints.get().add(waypoint);
            }
        }
    }

    @Override
    public WWidget getWidget() {
        WTable table = new WTable();
        table.add(label);
        WButton path = new WButton("Path");
        table.add(path);
        path.action = this::path;
        WButton clear = new WButton("Clear");
        table.add(clear);
        clear.action = this::clear;
        return table;
    }

    private void path() {
        if (deathPos.isEmpty() && mc.player != null) {
            ChatUtils.moduleWarning(this,"No latest death found.");
        } else {
            if (mc.world != null) {
                double x = deathPos.get("x"), z = deathPos.get("z");
                if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing())
                    BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalXZ((int) x, (int) z));
            }
        }
    }

    private void clear() {
        Waypoints.get().remove(waypoint);
        label.setText("No latest death.");
    }
}
