/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens.settings;

import coloursplash.lupusclient.gui.widgets.WLabel;
import coloursplash.lupusclient.gui.widgets.WWidget;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.settings.Setting;

import java.util.List;

public class ModuleListSettingScreen extends LeftRightListSettingScreen<Module> {
    public ModuleListSettingScreen(Setting<List<Module>> setting) {
        super("Select Modules", setting, Modules.REGISTRY);
    }

    @Override
    protected WWidget getValueWidget(Module value) {
        return new WLabel(value.title);
    }

    @Override
    protected String getValueName(Module value) {
        return value.title;
    }
}
