/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.settings;

import coloursplash.lupusclient.gui.screens.settings.PotionSettingScreen;
import coloursplash.lupusclient.gui.widgets.WButton;
import coloursplash.lupusclient.gui.widgets.WItemWithLabel;
import coloursplash.lupusclient.utils.misc.MyPotion;
import net.minecraft.client.MinecraftClient;

import java.util.function.Consumer;

public class PotionSetting extends EnumSetting<MyPotion> {
    public PotionSetting(String name, String description, MyPotion defaultValue, Consumer<MyPotion> onChanged, Consumer<Setting<MyPotion>> onModuleActivated) {
        super(name, description, defaultValue, onChanged, onModuleActivated);

        widget = new WItemWithLabel(get().potion);
        widget.add(new WButton("Select")).getWidget().action = () -> MinecraftClient.getInstance().openScreen(new PotionSettingScreen(this));
    }

    @Override
    public void resetWidget() {
        ((WItemWithLabel) widget).set(get().potion);
    }

    public static class Builder extends EnumSetting.Builder<MyPotion> {
        @Override
        public EnumSetting<MyPotion> build() {
            return new PotionSetting(name, description, defaultValue, onChanged, onModuleActivated);
        }
    }
}
