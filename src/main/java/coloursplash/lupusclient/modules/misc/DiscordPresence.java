

package coloursplash.lupusclient.modules.misc;

//Created by squidoodly

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.Config;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.settings.StringSetting;
import coloursplash.lupusclient.utils.Utils;

public class DiscordPresence extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> line1 = sgGeneral.add(new StringSetting.Builder()
            .name("line-1")
            .description("The text it displays on line 1 of the RPC.")
            .defaultValue("{player} || {server}")
            .onChanged(booleanSetting -> updateDetails())
            .build()
    );

    private final Setting<String> line2 = sgGeneral.add(new StringSetting.Builder()
            .name("line-2")
            .description("The text it displays on line 2 of the RPC.")
            .defaultValue("Lupus Client on top")
            .onChanged(booleanSetting -> updateDetails())
            .build()
    );

    public DiscordPresence() {
        super(Categories.Misc, "discord-presence", "Displays a RPC for you on Discord to show that you're playing Lupus Client!");
    }

    private static final DiscordRichPresence rpc = new DiscordRichPresence();
    private static final DiscordRPC instance = DiscordRPC.INSTANCE;
    private SmallImage currentSmallImage;
    private int ticks;

    @Override
    public void onActivate() {
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        instance.Discord_Initialize("818499395614933032", handlers, true, null);

        rpc.startTimestamp = System.currentTimeMillis() / 1000L;
        rpc.largeImageKey = "lupus_client";
        String largeText = "Lupus Client " + Config.get().version.getOriginalString();
        rpc.largeImageText = largeText;
        currentSmallImage = SmallImage.MineGame;
        updateDetails();

        instance.Discord_UpdatePresence(rpc);
        instance.Discord_RunCallbacks();
    }

    @Override
    public void onDeactivate() {
        instance.Discord_ClearPresence();
        instance.Discord_Shutdown();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!Utils.canUpdate()) return;
        ticks++;

        if (ticks >= 200) {
            currentSmallImage = currentSmallImage.next();
            currentSmallImage.apply();
            instance.Discord_UpdatePresence(rpc);

            ticks = 0;
        }

        updateDetails();
        instance.Discord_RunCallbacks();
    }

    private String getLine(Setting<String> line) {
        if (line.get().length() > 0) return line.get().replace("{player}", getName()).replace("{server}", getServer());
        else return null;
    }

    private String getServer(){
        if (mc.isInSingleplayer()) return "SinglePlayer";
        else return Utils.getWorldName();
    }

    private String getName(){
        return mc.player.getGameProfile().getName();
    }

    private void updateDetails() {
        if (isActive() && Utils.canUpdate()) {
            rpc.details = getLine(line1);
            rpc.state = getLine(line2);
            instance.Discord_UpdatePresence(rpc);
        }
    }

    private enum SmallImage {
        MineGame("minegame", "MineGame159"),
        Squid("squidoodly", "squidoodly");

        private final String key, text;

        SmallImage(String key, String text) {
            this.key = key;
            this.text = text;
        }

        void apply() {
            rpc.smallImageKey = key;
            rpc.smallImageText = text;
        }

        SmallImage next() {
            if (this == MineGame) return Squid;
            return MineGame;
        }
    }
}
