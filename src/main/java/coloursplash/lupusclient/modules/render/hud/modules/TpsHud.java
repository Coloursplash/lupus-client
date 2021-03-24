

package coloursplash.lupusclient.modules.render.hud.modules;

import coloursplash.lupusclient.modules.render.hud.HUD;
import coloursplash.lupusclient.utils.world.TickRate;

public class TpsHud extends DoubleTextHudElement {
    public TpsHud(HUD hud) {
        super(hud, "tps", "Displays the server's TPS.", "TPS: ");
    }

    @Override
    protected String getRight() {
        return String.format("%.1f", TickRate.INSTANCE.getTickRate());
    }
}
