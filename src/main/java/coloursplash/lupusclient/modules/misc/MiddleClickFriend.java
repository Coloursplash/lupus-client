

package coloursplash.lupusclient.modules.misc;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.lupus.MiddleMouseButtonEvent;
import coloursplash.lupusclient.friends.Friend;
import coloursplash.lupusclient.friends.Friends;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import net.minecraft.entity.player.PlayerEntity;

public class MiddleClickFriend extends Module {
    public MiddleClickFriend() {
        super(Categories.Misc, "middle-click-friend", "Adds or removes a player as a friend via middle click.");
    }

    @EventHandler
    private void onMiddleMouseButton(MiddleMouseButtonEvent event) {
        if (mc.currentScreen != null) return;
        if (mc.targetedEntity instanceof PlayerEntity) Friends.get().addOrRemove(new Friend((PlayerEntity) mc.targetedEntity));
    }
}
