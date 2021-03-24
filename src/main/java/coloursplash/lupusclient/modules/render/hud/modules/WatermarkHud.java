

package coloursplash.lupusclient.modules.render.hud.modules;

import coloursplash.lupusclient.Config;
import coloursplash.lupusclient.modules.render.hud.HUD;

public class WatermarkHud extends DoubleTextHudElement {
    public WatermarkHud(HUD hud) {
        super(hud, "watermark", "Displays a Lupus Client watermark.", "Lupus Client ");
    }

    @Override
    protected String getRight() {
        return Config.get().version.getOriginalString();
    }
}
