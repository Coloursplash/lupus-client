

package coloursplash.lupusclient.modules.player;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.packets.PacketEvent;
import coloursplash.lupusclient.mixin.BlockHitResultAccessor;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.math.Direction;

public class BuildHeight extends Module {
    public BuildHeight() {
        super(Categories.Player, "build-height", "Allows you to interact with objects at the build limit.");
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (!(event.packet instanceof PlayerInteractBlockC2SPacket)) return;

        PlayerInteractBlockC2SPacket p = (PlayerInteractBlockC2SPacket) event.packet;
        if (p.getBlockHitResult().getPos().y >= 255 && p.getBlockHitResult().getSide() == Direction.UP) {
            ((BlockHitResultAccessor) p.getBlockHitResult()).setSide(Direction.DOWN);
        }
    }
}
