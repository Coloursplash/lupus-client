

package coloursplash.lupusclient.modules.movement;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.entity.player.ClipAtLedgeEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;

public class SafeWalk extends Module {
    public SafeWalk() {
        super(Categories.Movement, "safe-walk", "Prevents you from walking off blocks. Useful over a void.");
    }

    @EventHandler
    private void onClipAtLedge(ClipAtLedgeEvent event) {
        event.setClip(true);
    }
}
