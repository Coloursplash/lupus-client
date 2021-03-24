

package coloursplash.lupusclient.modules.misc;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.entity.EntityAddedEvent;
import coloursplash.lupusclient.events.entity.EntityRemovedEvent;
import coloursplash.lupusclient.friends.Friends;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.settings.StringSetting;
import coloursplash.lupusclient.utils.entity.FakePlayerEntity;
import coloursplash.lupusclient.utils.player.ChatUtils;
import net.minecraft.entity.player.PlayerEntity;

public class VisualRange extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> ignoreFriends = sgGeneral.add(new BoolSetting.Builder()
            .name("ignore-friends")
            .description("Ignores friends.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> ignoreFakes = sgGeneral.add(new BoolSetting.Builder()
            .name("ignore-fakeplayers")
            .description("Ignores fake players.")
            .defaultValue(true)
            .build()
    );

    private final Setting<String> enterMessage = sgGeneral.add(new StringSetting.Builder()
            .name("enter-message")
            .description("The message for when a player enters your visual range.")
            .defaultValue("{player} has entered your visual range.")
            .build()
    );

    private final Setting<String> leaveMessage = sgGeneral.add(new StringSetting.Builder()
            .name("leave-message")
            .description("The message for when a player leaves your visual range.")
            .defaultValue("{player} has left your visual range.")
            .build()
    );


    public VisualRange() {
        super(Categories.Misc, "visual-range", "Notifies you when a player enters/leaves your visual range.");
    }

    @EventHandler
    private void onEntityAdded(EntityAddedEvent event) {
        if (event.entity.equals(mc.player) || !(event.entity instanceof PlayerEntity) || !Friends.get().attack((PlayerEntity) event.entity) && ignoreFriends.get() || (event.entity instanceof FakePlayerEntity && ignoreFakes.get())) return;

        String enter = enterMessage.get().replace("{player}", ((PlayerEntity) event.entity).getGameProfile().getName());
        ChatUtils.moduleInfo(this, enter);
    }

    @EventHandler
    private void onEntityRemoved(EntityRemovedEvent event) {
        if (event.entity.equals(mc.player) || !(event.entity instanceof PlayerEntity) || !Friends.get().attack((PlayerEntity) event.entity) && ignoreFriends.get() || (event.entity instanceof FakePlayerEntity && ignoreFakes.get())) return;

        String leave = leaveMessage.get().replace("{player}", ((PlayerEntity) event.entity).getGameProfile().getName());
        ChatUtils.moduleInfo(this, leave);
    }
}
