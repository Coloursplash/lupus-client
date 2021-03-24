

package coloursplash.lupusclient.modules.misc;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.events.world.ConnectToServerEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.DoubleSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import net.minecraft.client.network.ServerInfo;

public class AutoReconnect extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    
    public final Setting<Double> time = sgGeneral.add(new DoubleSetting.Builder()
            .name("delay")
            .description("The amount of seconds to wait before reconnecting to the server.")
            .defaultValue(1.0)
            .min(0.0)
            .build()
    );

    public ServerInfo lastServerInfo;

    public AutoReconnect() {
        super(Categories.Misc, "auto-reconnect", "Automatically reconnects when disconnected from a server.");
        LupusClient.EVENT_BUS.subscribe(new StaticListener());
    }

    private class StaticListener {
        @EventHandler
        private void onConnectToServer(ConnectToServerEvent event) {
            lastServerInfo = mc.isInSingleplayer() ? null : mc.getCurrentServerEntry();
        }
    }
}
