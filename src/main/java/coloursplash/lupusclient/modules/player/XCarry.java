

package coloursplash.lupusclient.modules.player;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.packets.PacketEvent;
import coloursplash.lupusclient.mixin.CloseHandledScreenC2SPacketAccessor;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

public class XCarry extends Module {
    private boolean invOpened;

    public XCarry() {
        super(Categories.Player, "XCarry", "Allows you to store four extra items in your crafting grid.");
    }

    @Override
    public void onActivate() {
        invOpened = false;
    }

    @Override
    public void onDeactivate() {
        if (invOpened) {
            mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(mc.player.playerScreenHandler.syncId));
        }
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (!(event.packet instanceof CloseHandledScreenC2SPacket)) return;

        if (((CloseHandledScreenC2SPacketAccessor) event.packet).getSyncId() == mc.player.playerScreenHandler.syncId) {
            invOpened = true;
            event.cancel();
        }
    }
}
