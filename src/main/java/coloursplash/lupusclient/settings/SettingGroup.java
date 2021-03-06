/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.settings;

import coloursplash.lupusclient.gui.widgets.*;
import coloursplash.lupusclient.settings.BoolSetting.Builder;
import coloursplash.lupusclient.utils.misc.ISerializable;
import coloursplash.lupusclient.utils.misc.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SettingGroup implements ISerializable<SettingGroup>, Iterable<Setting<?>> {
    public final String name;
    public boolean sectionExpanded;

    final List<Setting<?>> settings = new ArrayList<>(1);

    SettingGroup(String name, boolean sectionExpanded) {
        this.name = name;
        this.sectionExpanded = sectionExpanded;
    }

    public Setting<?> get(String name) {
        for (Setting<?> setting : this) {
            if (setting.name.equals(name)) return setting;
        }

        return null;
    }

    public <T> Setting<T> add(Setting<T> boolSetting) {
        settings.add(boolSetting);
        return boolSetting;
    }

    public void fillTable(WTable table) {
        WSection section = table.add(new WSection(name, sectionExpanded)).fillX().expandX().getWidget();
        section.action = () -> sectionExpanded = section.isExpanded();

        for (Setting<?> setting : settings) {
            fillTable(section, setting);
        }

        table.row();
    }

    private void fillTable(WSection section, Setting<?> setting) {
        if (setting.widget instanceof WTextBox) ((WTextBox) setting.widget).setFocused(false);

        WLabel label = section.add(new WLabel(setting.title)).getWidget();
        label.tooltip = setting.description;

        Cell<?> cell = section.add(setting.widget).fillX();
        if (setting.widget instanceof WIntEdit || setting.widget instanceof WDoubleEdit) cell.expandX();
        WWidget widget = cell.getWidget();
        widget.tooltip = setting.description;

        WButton reset = section.add(new WButton(WButton.ButtonRegion.Reset)).getWidget();
        reset.tooltip = "Reset";
        reset.action = setting::reset;

        section.row();
    }

    @Override
    public Iterator<Setting<?>> iterator() {
        return settings.iterator();
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        tag.putString("name", name);
        tag.putBoolean("sectionExpanded", sectionExpanded);
        tag.put("settings", NbtUtils.listToTag(settings));

        return tag;
    }

    @Override
    public SettingGroup fromTag(CompoundTag tag) {
        sectionExpanded = tag.getBoolean("sectionExpanded");

        ListTag settingsTag = tag.getList("settings", 10);
        for (Tag t : settingsTag) {
            CompoundTag settingTag = (CompoundTag) t;

            Setting<?> setting = get(settingTag.getString("name"));
            if (setting != null) setting.fromTag(settingTag);
        }

        return this;
    }
}
