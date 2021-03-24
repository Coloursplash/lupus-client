

package coloursplash.lupusclient.modules.combat;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.game.OpenScreenEvent;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.utils.world.BlockUtils;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class SelfAnvil extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
            .name("rotate")
            .description("Forces you to rotate upwards when placing the anvil.")
            .defaultValue(true)
            .build()
    );

    public SelfAnvil() {
        super(Categories.Combat, "self-anvil", "Automatically places an anvil on you to prevent other players from going into your hole.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        int slot = findSlot();
        if (slot == -1) return;

        BlockPos blockPos = mc.player.getBlockPos().add(0, 2, 0);

        if (BlockUtils.place(blockPos, Hand.MAIN_HAND, slot, rotate.get(), 0, true)) {
            toggle();
        }
    }

    @EventHandler
    private void onOpenScreen(OpenScreenEvent event) {
        if (event.screen instanceof AnvilScreen) event.cancel();
    }

    private int findSlot() {
        for (int i = 0; i < 9; i++) {
            Item item = mc.player.inventory.getStack(i).getItem();

            if (item == Items.ANVIL || item == Items.CHIPPED_ANVIL || item == Items.DAMAGED_ANVIL) {
                return i;
            }
        }

        return -1;
    }
}
