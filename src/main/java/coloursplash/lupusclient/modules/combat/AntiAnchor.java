
package coloursplash.lupusclient.modules.combat;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.utils.player.InvUtils;
import coloursplash.lupusclient.utils.player.Rotations;
import coloursplash.lupusclient.utils.world.BlockUtils;
import net.minecraft.block.*;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Hand;

public class AntiAnchor extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // Setting for the rotation
    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
            .name("rotate")
            .description("Forces you to rotate upwards when placing a slab above you.")
            .defaultValue(true)
            .build()
    );

    // Constructor
    public AntiAnchor(){
        super(Categories.Combat, "anti-anchor", "Automatically prevents Anchor Aura by placing a slab on your head.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        /*
            Check:
            if there is an anchor above our head
            and if we havent already placed a block
         */
        if (   mc.world.getBlockState(mc.player.getBlockPos().add(0, 2, 0)).getBlock() == Blocks.RESPAWN_ANCHOR
                && mc.world.getBlockState(mc.player.getBlockPos().add(0, 1, 0)).getBlock() == Blocks.AIR){
            // Get the block of the slab
            int slot = InvUtils.findItemInHotbar(itemStack -> Block.getBlockFromItem(itemStack.getItem()) instanceof net.minecraft.block.SlabBlock);
            if (slot != -1) {

                // Start sneaking (we have to sneak for placing the slab between us and the anchor)
                mc.player.input.sneaking = true;
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));

                // Check the rotation
                rotateCheck(slot);
            }

        }

    }

    private void rotateCheck(int slot) {
        // if rotate, rotate and then start placing
        if (rotate.get()) Rotations.rotate(mc.player.yaw, -90, 15, () -> placeBlock(slot));
            // If not, just dont rotate and start placing
        else placeBlock(slot);
    }

    private void placeBlock(int slot) {
        // Start place
        BlockUtils.place(mc.player.getBlockPos().add(0, 1, 0), Hand.MAIN_HAND, slot, rotate.get(), 0, false);
    }
}
