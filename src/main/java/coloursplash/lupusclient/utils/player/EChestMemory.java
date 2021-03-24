/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.utils.player;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.events.game.OpenScreenEvent;
import coloursplash.lupusclient.events.world.BlockActivateEvent;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.util.collection.DefaultedList;

public class EChestMemory {
    private static final MinecraftClient MC = MinecraftClient.getInstance();

    private static int echestOpenedState;
    public static final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(27, ItemStack.EMPTY);

    public static void init() {
        LupusClient.EVENT_BUS.subscribe(EChestMemory.class);
    }

    @EventHandler
    private static void onBlockActivate(BlockActivateEvent event) {
        if (event.blockState.getBlock() instanceof EnderChestBlock && echestOpenedState == 0) echestOpenedState = 1;
    }

    @EventHandler
    private static void onOpenScreenEvent(OpenScreenEvent event) {
        if (echestOpenedState == 1 && event.screen instanceof GenericContainerScreen) {
            echestOpenedState = 2;
            return;
        }
        if (echestOpenedState == 0) return;

        if (!(MC.currentScreen instanceof GenericContainerScreen)) return;
        GenericContainerScreenHandler container = ((GenericContainerScreen) MC.currentScreen).getScreenHandler();
        if (container == null) return;
        Inventory inv = container.getInventory();

        for (int i = 0; i < 27; i++) {
            ITEMS.set(i, inv.getStack(i));
        }

        echestOpenedState = 0;
    }
}
