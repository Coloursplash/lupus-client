/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.modules;

import coloursplash.lupusclient.Config;
import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.events.lupus.ModuleBindChangedEvent;
import coloursplash.lupusclient.events.lupus.ModuleVisibilityChangedEvent;
import coloursplash.lupusclient.gui.WidgetScreen;
import coloursplash.lupusclient.gui.screens.ModuleScreen;
import coloursplash.lupusclient.gui.widgets.WWidget;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.settings.Settings;
import coloursplash.lupusclient.utils.Utils;
import coloursplash.lupusclient.utils.misc.ISerializable;
import coloursplash.lupusclient.utils.player.ChatUtils;
import coloursplash.lupusclient.utils.render.color.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Formatting;

import java.util.Objects;

public abstract class Module implements ISerializable<Module> {
    protected final MinecraftClient mc;

    public final Category category;
    public final String name;
    public final String title;
    public final String description;
    public final Color color;

    public final Settings settings = new Settings();

    private boolean active;
    private boolean visible = true;

    public boolean serialize = true;

    private int key = -1;
    public boolean toggleOnKeyRelease = false;

    public Module(Category category, String name, String description) {
        this.mc = MinecraftClient.getInstance();
        this.category = category;
        this.name = name;
        this.title = Utils.nameToTitle(name);
        this.description = description;
        this.color = Color.fromHsv(Utils.random(0.0, 360.0), 0.35, 1);
    }

    public WidgetScreen getScreen() {
        return new ModuleScreen(this);
    }

    public WWidget getWidget() {
        return null;
    }

    public void openScreen() {
        mc.openScreen(getScreen());
    }

    public void doAction(boolean onActivateDeactivate) {
        toggle(onActivateDeactivate);
    }
    public void doAction() {
        doAction(true);
    }

    public void onActivate() {}
    public void onDeactivate() {}

    public void toggle(boolean onActivateDeactivate) {
        if (!active) {
            active = true;
            Modules.get().addActive(this);

            for (SettingGroup sg : settings) {
                for (Setting setting : sg) {
                    if (setting.onModuleActivated != null) setting.onModuleActivated.accept(setting);
                }
            }

            if (onActivateDeactivate) {
                LupusClient.EVENT_BUS.subscribe(this);
                onActivate();
            }
        }
        else {
            active = false;
            Modules.get().removeActive(this);

            if (onActivateDeactivate) {
                LupusClient.EVENT_BUS.unsubscribe(this);
                onDeactivate();
            }
        }
    }
    public void toggle() {
        toggle(true);
    }

    public String getInfoString() {
        return null;
    }

    @Override
    public CompoundTag toTag() {
        if (!serialize) return null;
        CompoundTag tag = new CompoundTag();

        tag.putString("name", name);
        tag.putInt("key", key);
        tag.putBoolean("toggleOnKeyRelease", toggleOnKeyRelease);
        tag.put("settings", settings.toTag());

        tag.putBoolean("active", active);
        tag.putBoolean("visible", visible);

        return tag;
    }

    @Override
    public Module fromTag(CompoundTag tag) {
        // General
        key = tag.getInt("key");
        toggleOnKeyRelease = tag.getBoolean("toggleOnKeyRelease");

        // Settings
        Tag settingsTag = tag.get("settings");
        if (settingsTag instanceof CompoundTag) settings.fromTag((CompoundTag) settingsTag);

        boolean active = tag.getBoolean("active");
        if (active != isActive()) toggle(Utils.canUpdate());
        setVisible(tag.getBoolean("visible"));

        return this;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        LupusClient.EVENT_BUS.post(ModuleVisibilityChangedEvent.get(this));
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isActive() {
        return active;
    }

    public void sendToggledMsg() {
        if (Config.get().chatCommandsInfo) ChatUtils.info(42069, "Toggled (highlight)%s(default) %s(default).", title, isActive() ? Formatting.GREEN + "on" : Formatting.RED + "off");
    }

    public void setKey(int key, boolean postEvent) {
        this.key = key;
        if (postEvent) LupusClient.EVENT_BUS.post(ModuleBindChangedEvent.get(this));
    }
    public void setKey(int key) {
        setKey(key, true);
    }

    public int getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return Objects.equals(name, module.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
