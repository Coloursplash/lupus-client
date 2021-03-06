/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.render.EChestPreview;
import coloursplash.lupusclient.modules.render.ItemHighlight;
import coloursplash.lupusclient.modules.render.ShulkerPeek;
import coloursplash.lupusclient.utils.misc.input.KeyBinds;
import coloursplash.lupusclient.utils.player.EChestMemory;
import coloursplash.lupusclient.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {
    @Shadow @Nullable protected Slot focusedSlot;

    @Shadow protected int x;
    @Shadow protected int y;
    private static final Identifier LIGHT = new Identifier("lupus-client", "container_3x9.png");
    private static final Identifier DARK = new Identifier("lupus-client", "container_3x9-dark.png");
    private static MinecraftClient mc;

    public HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo info) {
        mc = MinecraftClient.getInstance();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if (focusedSlot != null && !focusedSlot.getStack().isEmpty()) {
            // Shulker Preview
            if (Modules.get().isActive(ShulkerPeek.class) && ((KeyBinds.SHULKER_PEEK.isPressed() && Modules.get().get(ShulkerPeek.class).mode.get() == ShulkerPeek.Mode.Tooltip) || (Modules.get().get(ShulkerPeek.class).mode.get() == ShulkerPeek.Mode.Always))) {
                CompoundTag compoundTag = focusedSlot.getStack().getSubTag("BlockEntityTag");
                if (compoundTag != null) {
                    if (compoundTag.contains("Items", 9)) {
                        DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(27, ItemStack.EMPTY);
                        Inventories.fromTag(compoundTag, itemStacks);

                        draw(matrices, itemStacks, mouseX, mouseY);
                    }
                }
            }

            // EChest preview
            if (focusedSlot.getStack().getItem() == Items.ENDER_CHEST && Modules.get().isActive(EChestPreview.class)) {
                draw(matrices, EChestMemory.ITEMS, mouseX, mouseY);
            }
        }
    }

    private boolean hasItems(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getSubTag("BlockEntityTag");
        return compoundTag != null && compoundTag.contains("Items", 9);
    }

    @Inject(method = "drawMouseoverTooltip", at = @At("HEAD"), cancellable = true)
    private void onDrawMouseoverTooltip(MatrixStack matrices, int x, int y, CallbackInfo info) {
        if (focusedSlot != null && !focusedSlot.getStack().isEmpty()) {
            if (Modules.get().isActive(ShulkerPeek.class) && hasItems(focusedSlot.getStack()) && ((KeyBinds.SHULKER_PEEK.isPressed() && Modules.get().get(ShulkerPeek.class).mode.get() == ShulkerPeek.Mode.Tooltip) || (Modules.get().get(ShulkerPeek.class).mode.get() == ShulkerPeek.Mode.Always))) info.cancel();
            else if (focusedSlot.getStack().getItem() == Items.ENDER_CHEST && Modules.get().isActive(EChestPreview.class)) info.cancel();
        }
    }

    private void draw(MatrixStack matrices, DefaultedList<ItemStack> itemStacks, int mouseX, int mouseY) {
        RenderSystem.disableLighting();
        RenderSystem.disableDepthTest();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

        drawBackground(matrices, mouseX + 6, mouseY + 6);
        DiffuseLighting.enable();

        int row = 0;
        int i = 0;
        for (ItemStack itemStack : itemStacks) {
            RenderUtils.drawItem(itemStack, mouseX + 6 + 8 + i * 18, mouseY + 6 + 7 + row * 18, true);

            i++;
            if (i >= 9) {
                i = 0;
                row++;
            }
        }

        DiffuseLighting.disable();
        RenderSystem.enableDepthTest();
    }

    private void drawBackground(MatrixStack matrices, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(Modules.get().get(ShulkerPeek.class).bgMode.get() == ShulkerPeek.BackgroundMode.Light ? LIGHT : DARK);
        int width = 176;
        int height = 67;
        DrawableHelper.drawTexture(matrices, x, y, 0, 0, 0, width, height, height, width);
    }

    @Inject(method = "drawSlot", at = @At("HEAD"))
    private void onDrawSlot(MatrixStack matrices, Slot slot, CallbackInfo info) {
        int color = Modules.get().get(ItemHighlight.class).getColor(slot.getStack());
        if (color != -1) fill(matrices, slot.x, slot.y, slot.x + 16, slot.y + 16, color);
    }
}
