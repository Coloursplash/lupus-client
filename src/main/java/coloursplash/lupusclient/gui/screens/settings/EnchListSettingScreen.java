/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens.settings;

import coloursplash.lupusclient.gui.widgets.WLabel;
import coloursplash.lupusclient.gui.widgets.WWidget;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.utils.misc.Names;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class EnchListSettingScreen extends LeftRightListSettingScreen<Enchantment> {
    public EnchListSettingScreen(Setting<List<Enchantment>> setting) {
        super("Select items", setting, Registry.ENCHANTMENT);
    }

    @Override
    protected WWidget getValueWidget(Enchantment value) {
        return new WLabel(getValueName(value));
    }

    @Override
    protected String getValueName(Enchantment value) {
        return Names.get(value);
    }
}
