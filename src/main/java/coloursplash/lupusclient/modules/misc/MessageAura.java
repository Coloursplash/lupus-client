

package coloursplash.lupusclient.modules.misc;

//Updated by squidoodly 24/07/2020

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.entity.EntityAddedEvent;
import coloursplash.lupusclient.friends.Friend;
import coloursplash.lupusclient.friends.Friends;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.settings.StringSetting;
import net.minecraft.entity.player.PlayerEntity;

public class MessageAura extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> message = sgGeneral.add(new StringSetting.Builder()
            .name("message")
            .description("The specified message sent to the player.")
            .defaultValue("Lupus on Crack!")
            .build()
    );

    private final Setting<Boolean> ignoreFriends = sgGeneral.add(new BoolSetting.Builder()
            .name("ignore-friends")
            .description("Will not send any messages to people friended.")
            .defaultValue(false)
            .build()
    );

    public MessageAura() {
        super(Categories.Misc, "message-aura", "Sends a specified message to any player that enters render distance.");
    }

    @EventHandler
    private void onEntityAdded(EntityAddedEvent event) {
        if (!(event.entity instanceof PlayerEntity) || event.entity.getUuid().equals(mc.player.getUuid())) return;

        if (!ignoreFriends.get() || (ignoreFriends.get() && !Friends.get().contains(new Friend((PlayerEntity)event.entity)))) {
            mc.player.sendChatMessage("/msg " + ((PlayerEntity) event.entity).getGameProfile().getName() + " " + message.get());
        }
    }
}
