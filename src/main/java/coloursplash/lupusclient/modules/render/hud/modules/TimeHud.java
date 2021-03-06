

package coloursplash.lupusclient.modules.render.hud.modules;

import coloursplash.lupusclient.modules.render.hud.HUD;

public class TimeHud extends DoubleTextHudElement {
    public TimeHud(HUD hud) {
        super(hud, "time", "Displays the world time.", "Time: ");
    }

    @Override
    protected String getRight() {
        if (isInEditor()) return "00:00";

        int ticks = (int) (mc.world.getTimeOfDay() % 24000);
        ticks += 6000;
        if (ticks > 24000) ticks -= 24000;
        return String.format("%02d:%02d", ticks / 1000, (int) (ticks % 1000 / 1000.0 * 60));
    }
}
