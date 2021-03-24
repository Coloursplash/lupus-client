

package coloursplash.lupusclient.modules.render;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.IntSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;

public class CustomFOV extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> fov = sgGeneral.add(new IntSetting.Builder()
            .name("fOV") // not typo, just makes it show FOV instead of Fov moment.
            .description("Your custom FOV.")
            .defaultValue(100)
            .sliderMin(1)
            .sliderMax(179)
            .build()
    );

    private float _fov;

    public CustomFOV() {
        super(Categories.Render, "custom-fov", "Allows your FOV to be more customizable.");
    }

    @Override
    public void onActivate() {
        _fov = (float) mc.options.fov;
        mc.options.fov = fov.get();
    }

    public void getFOV() {
        mc.options.fov = fov.get();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (fov.get() != mc.options.fov) {
            getFOV();
        }
    }

    @Override
    public void onDeactivate() {
     mc.options.fov = _fov;
    }
}
