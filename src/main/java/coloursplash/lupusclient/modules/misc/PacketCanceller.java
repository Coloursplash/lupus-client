

package coloursplash.lupusclient.modules.misc;

import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import coloursplash.lupusclient.events.packets.PacketEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.PacketBoolSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.utils.network.PacketUtils;
import net.minecraft.network.Packet;

public class PacketCanceller extends Module {
    public static Object2BooleanMap<Class<? extends Packet<?>>> S2C_PACKETS = new Object2BooleanArrayMap<>();
    public static Object2BooleanMap<Class<? extends Packet<?>>> C2S_PACKETS = new Object2BooleanArrayMap<>();
    
    static {
        for (Class<? extends Packet<?>> packet : PacketUtils.getS2CPackets()) S2C_PACKETS.put(packet, false);
        for (Class<? extends Packet<?>> packet : PacketUtils.getC2SPackets()) C2S_PACKETS.put(packet, false);
    }
    
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    
    private final Setting<Object2BooleanMap<Class<? extends Packet<?>>>> s2cPackets = sgGeneral.add(new PacketBoolSetting.Builder()
            .name("S2C-packets")
            .description("Server-to-client packets to cancel.")
            .defaultValue(S2C_PACKETS)
            .build()
    );

    private final Setting<Object2BooleanMap<Class<? extends Packet<?>>>> c2sPackets = sgGeneral.add(new PacketBoolSetting.Builder()
            .name("C2S-packets")
            .description("Client-to-server packets to cancel.")
            .defaultValue(C2S_PACKETS)
            .build()
    );

    public PacketCanceller() {
        super(Categories.Misc, "packet-canceller", "Allows you to cancel certain packets.");
    }

    @EventHandler(priority = EventPriority.HIGHEST + 1)
    private void onReceivePacket(PacketEvent.Receive event) {
        if (s2cPackets.get().getBoolean(event.packet.getClass())) event.cancel();
    }

    @EventHandler(priority = EventPriority.HIGHEST + 1)
    private void onSendPacket(PacketEvent.Send event) {
        if (c2sPackets.get().getBoolean(event.packet.getClass())) event.cancel();
    }
}
