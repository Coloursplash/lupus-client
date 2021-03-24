

package coloursplash.lupusclient.modules.player;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.packets.PacketEvent;
import coloursplash.lupusclient.mixin.PlayerPositionLookS2CPacketAccessor;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class NoRotate extends Module {
    public NoRotate() {
        super(Categories.Player, "no-rotate", "Attempts to block rotations sent from server to client.");
    }

    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if (event.packet instanceof PlayerPositionLookS2CPacket) {
            ((PlayerPositionLookS2CPacketAccessor) event.packet).setPitch(mc.player.pitch);
            ((PlayerPositionLookS2CPacketAccessor) event.packet).setYaw(mc.player.yaw);
        }
    }
}
