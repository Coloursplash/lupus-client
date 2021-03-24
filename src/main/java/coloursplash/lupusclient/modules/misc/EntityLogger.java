

package coloursplash.lupusclient.modules.misc;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.entity.EntityAddedEvent;
import coloursplash.lupusclient.friends.Friends;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.EntityTypeListSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.utils.player.ChatUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;

public class EntityLogger extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Object2BooleanMap<EntityType<?>>> entities = sgGeneral.add(new EntityTypeListSetting.Builder()
            .name("entites")
            .description("Select specific entities.")
            .defaultValue(new Object2BooleanOpenHashMap<>(0))
            .build()
    );

    private final Setting<Boolean> playerNames = sgGeneral.add(new BoolSetting.Builder()
            .name("player-names")
            .description("Shows the player's name.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> friends = sgGeneral.add(new BoolSetting.Builder()
            .name("friends")
            .description("Logs friends.")
            .defaultValue(true)
            .build()
    );

    public EntityLogger() {
        super(Categories.Misc, "entity-logger", "Sends a client-side chat alert if a specified entity appears in render distance.");
    }

    @EventHandler
    private void onEntityAdded(EntityAddedEvent event) {
        if (event.entity.getUuid().equals(mc.player.getUuid())) return;

        if (entities.get().getBoolean(event.entity.getType())) {
            if (event.entity instanceof PlayerEntity) {
                if (!friends.get() && Friends.get().get((PlayerEntity) event.entity) != null) return;
            }

            String name;
            if (playerNames.get() && event.entity instanceof PlayerEntity) name = ((PlayerEntity) event.entity).getGameProfile().getName() + " (Player)";
            else name = event.entity.getType().getName().getString();

            ChatUtils.moduleInfo(this, "(highlight)%s (default)has spawned at (highlight)%.0f(default), (highlight)%.0f(default), (highlight)%.0f(default).", name, event.entity.getX(), event.entity.getY(), event.entity.getZ());
        }
    }
}
