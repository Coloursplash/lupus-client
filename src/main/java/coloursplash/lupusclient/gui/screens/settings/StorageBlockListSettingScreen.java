/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens.settings;

import coloursplash.lupusclient.gui.screens.WindowScreen;
import coloursplash.lupusclient.gui.widgets.WCheckbox;
import coloursplash.lupusclient.gui.widgets.WLabel;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.StorageBlockListSetting;
import net.minecraft.block.entity.BlockEntityType;

import java.util.List;

public class StorageBlockListSettingScreen extends WindowScreen {
    public StorageBlockListSettingScreen(Setting<List<BlockEntityType<?>>> setting) {
        super("Select storage blocks", true);

        for (int i = 0; i < StorageBlockListSetting.STORAGE_BLOCKS.length; i++) {
            BlockEntityType<?> type = StorageBlockListSetting.STORAGE_BLOCKS[i];
            String name = StorageBlockListSetting.STORAGE_BLOCK_NAMES[i];

            add(new WLabel(name));
            WCheckbox checkbox = add(new WCheckbox(setting.get().contains(type))).fillX().right().getWidget();
            checkbox.action = () -> {
                if (checkbox.checked && !setting.get().contains(type)) {
                    setting.get().add(type);
                    setting.changed();
                } else if (!checkbox.checked && setting.get().remove(type)) {
                    setting.changed();
                }
            };

            row();
        }
    }
}
