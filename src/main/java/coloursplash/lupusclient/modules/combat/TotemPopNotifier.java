

package coloursplash.lupusclient.modules.combat;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.game.GameJoinedEvent;
import coloursplash.lupusclient.events.packets.PacketEvent;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.friends.Friends;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.settings.StringSetting;
import coloursplash.lupusclient.utils.player.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.Random;
import java.util.UUID;

public class TotemPopNotifier extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> announce = sgGeneral.add(new BoolSetting.Builder()
            .name("announce-in-chat")
            .description("Sends a chat message for everyone to see instead of a client-side alert.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> ignoreOwn = sgGeneral.add(new BoolSetting.Builder()
            .name("ignore-own")
            .description("Doesn't announce your own totem pops.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> ignoreFriend = sgGeneral.add(new BoolSetting.Builder()
            .name("ignore-friend")
            .description("Doesn't announce your friend's totem pops.")
            .defaultValue(true)
            .build()
    );

    private final Setting<String> popMessage = sgGeneral.add(new StringSetting.Builder()
            .name("pop-message")
            .description("Chat alert to send when a player pops.")
            .defaultValue("EZ pops. {player} just popped {pops} {totems}. Lupus Client on top!")
            .build()
    );

    private final Setting<String> deathMessage = sgGeneral.add(new StringSetting.Builder()
            .name("death-message")
            .description("Chat alert to send on a player's death.")
            .defaultValue("EZZZ. {player} just died after popping {pops} {totems}. Lupus Client on top!")
            .build()
    );

    private final Object2IntMap<UUID> totemPops = new Object2IntOpenHashMap<>();
    private final Object2IntMap<UUID> chatIds = new Object2IntOpenHashMap<>();

    private final Random random = new Random();

    public TotemPopNotifier() {
        super(Categories.Combat, "totem-pop-notifier", "Sends a chat message when a player either pops a totem or dies.");
    }

    @Override
    public void onActivate() {
        totemPops.clear();
        chatIds.clear();
    }

    @EventHandler
    private void onGameJoin(GameJoinedEvent event) {
        totemPops.clear();
        chatIds.clear();
    }

    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if (!(event.packet instanceof EntityStatusS2CPacket)) return;

        EntityStatusS2CPacket p = (EntityStatusS2CPacket) event.packet;
        if (p.getStatus() != 35) return;

        Entity entity = p.getEntity(mc.world);
        if (entity == null || (entity.equals(mc.player) && ignoreOwn.get()) || (!Friends.get().attack((PlayerEntity) entity) && ignoreFriend.get())) return;

        synchronized (totemPops) {
            int pops = totemPops.getOrDefault(entity.getUuid(), 0);
            totemPops.put(entity.getUuid(), ++pops);

            String msg = popMessage.get().replace("{player}", entity.getName().getString()).replace("{pops}", String.valueOf(pops)).replace("{totems}", (pops == 1 ? "totem" : "totems"));

            if (announce.get()) mc.player.sendChatMessage(msg);
            else ChatUtils.info(getChatId(entity), "(highlight)%s (default)popped (highlight)%d (default)%s.", entity.getName().getString(), pops, pops == 1 ? "totem" : "totems");
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        synchronized (totemPops) {
            for (PlayerEntity player : mc.world.getPlayers()) {
                if (!totemPops.containsKey(player.getUuid())) continue;

                if (player.deathTime > 0 || player.getHealth() <= 0) {
                    int pops = totemPops.removeInt(player.getUuid());

                    String msg = deathMessage.get().replace("{player}", player.getName().getString()).replace("{pops}", String.valueOf(pops)).replace("{totems}", (pops == 1 ? "totem" : "totems"));

                    if (announce.get()) mc.player.sendChatMessage(msg);
                    else ChatUtils.info(getChatId(player), "(highlight)%s (default)died after popping (highlight)%d (default)%s.", player.getName().getString(), pops, pops == 1 ? "totem" : "totems");

                    chatIds.removeInt(player.getUuid());
                }
            }
        }
    }

    private int getChatId(Entity entity) {
        return chatIds.computeIntIfAbsent(entity.getUuid(), value -> random.nextInt());
    }
}