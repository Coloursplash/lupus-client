

package coloursplash.lupusclient.modules.movement;

import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.DoubleSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;

public class Timer extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
            .name("speed")
            .description("Speed multiplier.")
            .defaultValue(1)
            .min(0.1)
            .sliderMin(0.1)
            .sliderMax(10)
            .build()
    );

    public Timer() {
        super(Categories.Movement, "timer", "Changes the speed of everything in your game.");
    }
    // If you put your timer to 0.1 you're a dumbass.
    public double getMultiplier() {
        return isActive() ? speed.get() : 1;
    }
}
