

package coloursplash.lupusclient.modules.movement;

import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.DoubleSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;

public class Velocity extends Module {
    private final SettingGroup sgDefault = settings.getDefaultGroup();

    public final Setting<Boolean> entities = sgDefault.add(new BoolSetting.Builder()
            .name("entities")
            .description("Modifies the amount of knockback you take from entities and attacks.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> explosions = sgDefault.add(new BoolSetting.Builder()
            .name("explosions")
            .description("Modifies your knockback from explosions.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> liquids = sgDefault.add(new BoolSetting.Builder()
            .name("liquids")
            .description("Modifies the amount you are pushed by flowing liquids.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Double> horizontal = sgDefault.add(new DoubleSetting.Builder()
            .name("horizontal-multiplier")
            .description("How much velocity you will take horizontally.")
            .defaultValue(0)
            .sliderMin(0)
            .sliderMax(1)
            .build()
    );

    private final Setting<Double> vertical = sgDefault.add(new DoubleSetting.Builder()
            .name("vertical-multiplier")
            .description("How much velocity you will take vertically.")
            .defaultValue(0)
            .sliderMin(0)
            .sliderMax(1)
            .build()
    );

    public Velocity() {
        super(Categories.Movement, "velocity", "Prevents you from being moved by external forces.");
    }

    public double getHorizontal() {
        return isActive() ? horizontal.get() : 1;
    }

    public double getVertical() {
        return isActive() ? vertical.get() : 1;
    }
}