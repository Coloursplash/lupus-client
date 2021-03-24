/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens.settings;

import coloursplash.lupusclient.gui.widgets.WItemWithLabel;
import coloursplash.lupusclient.gui.widgets.WWidget;
import coloursplash.lupusclient.settings.ItemListSetting;
import coloursplash.lupusclient.utils.misc.Names;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

import java.util.function.Predicate;

public class ItemListSettingScreen extends LeftRightListSettingScreen<Item> {
    public ItemListSettingScreen(ItemListSetting setting) {
        super("Select items", setting, Registry.ITEM);
    }

    @Override
    protected boolean includeValue(Item value) {
        Predicate<Item> filter = ((ItemListSetting) setting).filter;
        if (filter != null && !filter.test(value)) return false;

        return value != Items.AIR;
    }

    @Override
    protected WWidget getValueWidget(Item value) {
        return new WItemWithLabel(value.getDefaultStack());
    }

    @Override
    protected String getValueName(Item value) {
        return Names.get(value);
    }
}
