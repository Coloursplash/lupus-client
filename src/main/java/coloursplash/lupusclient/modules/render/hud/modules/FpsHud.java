

package coloursplash.lupusclient.modules.render.hud.modules;

import coloursplash.lupusclient.mixin.MinecraftClientAccessor;
import coloursplash.lupusclient.modules.render.hud.HUD;

public class FpsHud extends DoubleTextHudElement {
    public FpsHud(HUD hud) {
        super(hud, "fps", "Displays your FPS.", "FPS: ");
    }

    @Override
    protected String getRight() {
        return Integer.toString(((MinecraftClientAccessor) mc).getFps());
    }
}
