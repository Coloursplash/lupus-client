

package coloursplash.lupusclient.modules.player;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.game.OpenScreenEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

public class NoInteract extends Module {
    public NoInteract() {
        super(Categories.Player, "no-interact", "Blocks interactions with certain types of inputs.");
    }

    @EventHandler
    private void onScreenOpen(OpenScreenEvent event) {
        if (event.screen == null) return;
        if (!event.screen.isPauseScreen() && !(event.screen instanceof AbstractInventoryScreen) && (event.screen instanceof HandledScreen)) event.setCancelled(true);
    }
}
