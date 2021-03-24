/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens.settings;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import coloursplash.lupusclient.gui.screens.WindowScreen;
import coloursplash.lupusclient.gui.widgets.WIntTextBox;
import coloursplash.lupusclient.gui.widgets.WLabel;
import coloursplash.lupusclient.gui.widgets.WTable;
import coloursplash.lupusclient.gui.widgets.WTextBox;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.utils.misc.Names;
import net.minecraft.entity.effect.StatusEffect;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StatusEffectSettingScreen extends WindowScreen {
    private final Setting<Object2IntMap<StatusEffect>> setting;
    private final WTextBox filter;

    private String filterText = "";

    public StatusEffectSettingScreen(Setting<Object2IntMap<StatusEffect>> setting) {
        super("Select Potions", true);

        this.setting = setting;

        // Filter
        filter = new WTextBox("", 200);
        filter.setFocused(true);
        filter.action = () -> {
            filterText = filter.getText().trim();

            clear();
            initWidgets();
        };

        initWidgets();
    }

    private void initWidgets() {
        add(filter).fillX().expandX();
        row();

        List<StatusEffect> statusEffects = new ArrayList<>(setting.get().keySet());
        statusEffects.sort(Comparator.comparing(Names::get));

        WTable table = add(new WTable()).expandX().fillX().getWidget();

        for (StatusEffect statusEffect : statusEffects) {
            String name = Names.get(statusEffect);
            if (!StringUtils.containsIgnoreCase(name, filterText)) continue;

            table.add(new WLabel(name));
            WIntTextBox level = table.add(new WIntTextBox(setting.get().getInt(statusEffect), 50)).fillX().right().getWidget();
            level.action = () -> {
                setting.get().put(statusEffect, level.getValue());
                setting.changed();
            };

            table.row();
        }
    }
}
