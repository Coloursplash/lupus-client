

package coloursplash.lupusclient.modules.combat;

//Created by squidoodly 16/07/2020
// Not empty class anymore :bruh: - notseanbased

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.entity.player.AttackEntityEvent;
import coloursplash.lupusclient.friends.Friends;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.modules.Modules;
import net.minecraft.entity.player.PlayerEntity;

public class AntiFriendHit extends Module {
    public AntiFriendHit() {
        super(Categories.Combat, "anti-friend-hit", "Cancels out attacks that would hit friends.");
    }

    @EventHandler
    private void onAttackEntity(AttackEntityEvent event) {
        if (event.entity instanceof PlayerEntity && Modules.get().isActive(AntiFriendHit.class) && !Friends.get().attack((PlayerEntity) event.entity)) event.cancel();
    }
}
