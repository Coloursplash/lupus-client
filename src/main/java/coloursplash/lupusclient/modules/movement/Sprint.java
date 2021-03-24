

package coloursplash.lupusclient.modules.movement;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;

public class Sprint extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> whenStationary = sgGeneral.add(new BoolSetting.Builder()
            .name("when-stationary")
            .description("Continues sprinting even if you do not move.")
            .defaultValue(true)
            .build()
    );

    public Sprint() {
        super(Categories.Movement, "sprint", "Automatically sprints.");
    }
    
    @Override
    public void onDeactivate() {
        mc.player.setSprinting(false);
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player.forwardSpeed > 0 && !whenStationary.get()) {
            mc.player.setSprinting(true);
        } else if (whenStationary.get()) {
            mc.player.setSprinting(true);
        }
    }
}
