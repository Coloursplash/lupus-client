/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens.settings;

import coloursplash.lupusclient.gui.widgets.WLabel;
import coloursplash.lupusclient.gui.widgets.WWidget;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.utils.misc.Names;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class SoundEventListSettingScreen extends LeftRightListSettingScreen<SoundEvent> {
    public SoundEventListSettingScreen(Setting<List<SoundEvent>> setting) {
        super("Select sounds", setting, Registry.SOUND_EVENT);
    }

    @Override
    protected WWidget getValueWidget(SoundEvent value) {
        return new WLabel(getValueName(value));
    }

    @Override
    protected String getValueName(SoundEvent value) {
        return Names.getSoundName(value.getId());
    }
}
