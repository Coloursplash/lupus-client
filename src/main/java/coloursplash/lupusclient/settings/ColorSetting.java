/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.settings;

import com.google.common.collect.ImmutableList;
import coloursplash.lupusclient.gui.screens.settings.ColorSettingScreen;
import coloursplash.lupusclient.gui.widgets.WButton;
import coloursplash.lupusclient.gui.widgets.WQuad;
import coloursplash.lupusclient.gui.widgets.WTable;
import coloursplash.lupusclient.utils.render.color.SettingColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;

import java.util.List;
import java.util.function.Consumer;

public class ColorSetting extends Setting<SettingColor> {
    private static final List<String> SUGGESTIONS = ImmutableList.of("0 0 0 255", "225 25 25 255", "25 225 25 255", "25 25 225 255", "255 255 255 255");

    private final WQuad quad;

    public ColorSetting(String name, String description, SettingColor defaultValue, Consumer<SettingColor> onChanged, Consumer<Setting<SettingColor>> onModuleActivated) {
        super(name, description, defaultValue, onChanged, onModuleActivated);

        widget = new WTable();
        quad = widget.add(new WQuad(get())).getWidget();

        WButton button = widget.add(new WButton(WButton.ButtonRegion.Edit)).getWidget();
        button.action = () -> {
            ColorSettingScreen colorSettingScreen = new ColorSettingScreen(this);
            colorSettingScreen.action = () -> quad.color = get();
            MinecraftClient.getInstance().openScreen(colorSettingScreen);
        };
    }

    @Override
    protected SettingColor parseImpl(String str) {
        try {
            String[] strs = str.split(" ");
            return new SettingColor(Integer.parseInt(strs[0]), Integer.parseInt(strs[1]), Integer.parseInt(strs[2]), Integer.parseInt(strs[3]));
        } catch (IndexOutOfBoundsException | NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    public void reset(boolean callbacks) {
        value = new SettingColor(defaultValue);
        if (callbacks) {
            resetWidget();
            changed();
        }
    }

    @Override
    public void resetWidget() {
        quad.color = get();
    }

    @Override
    protected boolean isValueValid(SettingColor value) {
        value.validate();
        return true;
    }

    @Override
    public List<String> getSuggestions() {
        return SUGGESTIONS;
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = saveGeneral();
        tag.put("value", get().toTag());
        return tag;
    }

    @Override
    public SettingColor fromTag(CompoundTag tag) {
        get().fromTag(tag.getCompound("value"));

        changed();
        return get();
    }

    public static class Builder {
        private String name = "undefined", description = "";
        private SettingColor defaultValue;
        private Consumer<SettingColor> onChanged;
        private Consumer<Setting<SettingColor>> onModuleActivated;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder defaultValue(SettingColor defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder onChanged(Consumer<SettingColor> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public Builder onModuleActivated(Consumer<Setting<SettingColor>> onModuleActivated) {
            this.onModuleActivated = onModuleActivated;
            return this;
        }

        public ColorSetting build() {
            return new ColorSetting(name, description, defaultValue, onChanged, onModuleActivated);
        }
    }
}
