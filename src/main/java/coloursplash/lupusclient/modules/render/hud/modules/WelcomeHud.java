

package coloursplash.lupusclient.modules.render.hud.modules;

import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.player.NameProtect;
import coloursplash.lupusclient.modules.render.hud.HUD;
import coloursplash.lupusclient.settings.ColorSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.utils.render.color.SettingColor;

public class WelcomeHud extends DoubleTextHudElement {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<SettingColor> color = sgGeneral.add(new ColorSetting.Builder()
            .name("color")
            .description("Color of welcome text.")
            .defaultValue(new SettingColor(120, 43, 153))
            .build()
    );

    public WelcomeHud(HUD hud) {
        super(hud, "welcome", "Displays a welcome message.", "Welcome to Lupus Client, ");
        rightColor = color.get();
    }

    @Override
    protected String getRight() {
        return Modules.get().get(NameProtect.class).getName(mc.getSession().getUsername()) + "!";
    }
}
