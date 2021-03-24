

package coloursplash.lupusclient.modules.render.hud.modules;

import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.movement.Timer;
import coloursplash.lupusclient.modules.render.hud.HUD;

public class SpeedHud extends DoubleTextHudElement {
    public SpeedHud(HUD hud) {
        super(hud, "speed", "Displays your horizontal speed.", "Speed: ");
    }

    @Override
    protected String getRight() {
        if (isInEditor()) return "0,0";

        double tX = Math.abs(mc.player.getX() - mc.player.prevX);
        double tZ = Math.abs(mc.player.getZ() - mc.player.prevZ);
        double length = Math.sqrt(tX * tX + tZ * tZ);

        if (Modules.get().isActive(Timer.class)){
            length *= Modules.get().get(Timer.class).getMultiplier();
        }

        return String.format("%.1f", length * 20);
    }
}
