

package coloursplash.lupusclient.modules.misc;

import coloursplash.lupusclient.mixin.AbstractFurnaceScreenHandlerAccessor;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.utils.player.ChatUtils;
import coloursplash.lupusclient.utils.player.InvUtils;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class AutoSmelter extends Module {
    private int step;
    private boolean first;
    private int timer;
    private boolean waitingForItemsToSmelt;

    public AutoSmelter() {
        super(Categories.Misc, "auto-smelter", "Automatically smelts all items in your inventory that can be smelted.");
    }

    @Override
    public void onActivate() {
        first = true;
        waitingForItemsToSmelt = false;
    }

    public void onFurnaceClose() {
        first = true;
        waitingForItemsToSmelt = false;
    }

    public void tick(AbstractFurnaceScreenHandler c) {
        timer++;

        // When the furnace is opened.
        if (!first) {
            first = true;

            step = 0;
            timer = 0;
        }

        // Check for fuel.
        if (checkFuel(c)) return;

        // Wait for the smelting to be complete.
        if (c.getCookProgress() != 0 || timer < 5) return;

        if (step == 0) {
            // Take the smelted results.
            if (takeResults(c)) return;

            step++;
            timer = 0;
        } else if (step == 1) {
            // Wait for the items to smelt.
            if (waitingForItemsToSmelt) {
                if (c.slots.get(0).getStack().isEmpty()) {
                    step = 0;
                    timer = 0;
                    waitingForItemsToSmelt = false;
                }
                return;
            }

            // Insert items.
            if (insertItems(c)) return;

            waitingForItemsToSmelt = true;
        }
    }

    private boolean insertItems(AbstractFurnaceScreenHandler c) {
        if (!c.slots.get(0).getStack().isEmpty()) return true;

        int slot = -1;

        for (int i = 3; i < c.slots.size(); i++) {
            if (((AbstractFurnaceScreenHandlerAccessor) c).isSmeltable(c.slots.get(i).getStack())) {
                slot = i;
                break;
            }
        }

        if (slot == -1) {
            ChatUtils.moduleError(this, "You do not have any items in your inventory that can be smelted... disabling.");
            toggle();
            return true;
        }

        InvUtils.clickSlot(slot, 0, SlotActionType.PICKUP);
        InvUtils.clickSlot(0, 0, SlotActionType.PICKUP);

        return false;
    }

    private boolean checkFuel(AbstractFurnaceScreenHandler c) {
        if (c.getFuelProgress() <= 1 && !((AbstractFurnaceScreenHandlerAccessor) c).isFuel(c.slots.get(1).getStack())) {
            if (!c.slots.get(1).getStack().isEmpty()) {
                InvUtils.clickSlot(1, 0, SlotActionType.QUICK_MOVE);

                if (!c.slots.get(1).getStack().isEmpty()) {
                    ChatUtils.moduleError(this, "Your inventory is currently full... disabling.");
                    toggle();
                    return true;
                }
            }

            int slot = -1;
            for (int i = 3; i < c.slots.size(); i++) {
                if (((AbstractFurnaceScreenHandlerAccessor) c).isFuel(c.slots.get(i).getStack())) {
                    slot = i;
                    break;
                }
            }

            if (slot == -1) {
                ChatUtils.moduleError(this, "You do not have any fuel in your inventory... disabling.");
                toggle();
                return true;
            }

            InvUtils.clickSlot(slot, 0, SlotActionType.PICKUP);
            InvUtils.clickSlot(1, 0, SlotActionType.PICKUP);
        }

        return false;
    }

    private boolean takeResults(AbstractFurnaceScreenHandler c) {
        InvUtils.clickSlot(2, 0, SlotActionType.QUICK_MOVE);

        if (!c.slots.get(2).getStack().isEmpty()) {
            ChatUtils.moduleError(this, "Your inventory is full... disabling.");
            toggle();
            return true;
        }

        return false;
    }
}
