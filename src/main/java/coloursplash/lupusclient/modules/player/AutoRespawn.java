

package coloursplash.lupusclient.modules.player;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.game.OpenScreenEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super(Categories.Player, "auto-respawn", "Automatically respawns after death.");
    }

    @EventHandler
    private void onOpenScreenEvent(OpenScreenEvent event) {
        if (!(event.screen instanceof DeathScreen)) return;

        mc.player.requestRespawn();
        event.cancel();
    }
}
