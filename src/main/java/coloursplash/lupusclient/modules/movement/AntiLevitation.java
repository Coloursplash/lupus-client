

package coloursplash.lupusclient.modules.movement;

import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;

public class AntiLevitation extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    
    private final Setting<Boolean> applyGravity = sgGeneral.add(new BoolSetting.Builder()
            .name("gravity")
            .description("Applies gravity.")
            .defaultValue(false)
            .build()
    );

    public AntiLevitation() {
        super(Categories.Movement, "anti-levitation", "Prevents the levitation effect from working.");
    }

    public boolean isApplyGravity() {
        return applyGravity.get();
    }
}
