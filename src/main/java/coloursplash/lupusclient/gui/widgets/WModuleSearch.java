/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.widgets;

import coloursplash.lupusclient.gui.GuiConfig;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.modules.Modules;
import net.minecraft.util.Pair;

import java.util.List;

public class WModuleSearch extends WWindow {
    private WTextBox filter;

    public WModuleSearch() {
        super("Search", GuiConfig.get().getWindowConfig(GuiConfig.WindowType.Search).isExpanded(), true);
        type = GuiConfig.WindowType.Search;

        action = () -> getWindowConfig().setPos(x, y);

        filter = new WTextBox(filter != null ? filter.getText() : "", 140);
        filter.action = () -> {
            clear();
            initWidgets(false);
        };

        initWidgets(true);
    }

    private void initWidgets(boolean first) {
        // Search bar
        add(filter).fillX().expandX().getWidget();
        if (first && isExpanded()) filter.setFocused(true);
        row();

        // Modules
        if (!filter.getText().isEmpty()) {
            // Titles
            List<Pair<Module, Integer>> modules = Modules.get().searchTitles(filter.getText());
            if (modules.size() > 0) {
                WSection section = add(new WSection("Modules", true)).getWidget();
                row();

                for (Pair<Module, Integer> pair : modules) {
                    section.add(new WModule(pair.getLeft())).fillX().expandX().space(0);
                    section.row();
                }
            }

            // Settings
            modules = Modules.get().searchSettingTitles(filter.getText());
            if (modules.size() > 0) {
                WSection section = add(new WSection("Settings", true)).getWidget();
                row();

                for (Pair<Module, Integer> pair : modules) {
                    section.add(new WModule(pair.getLeft())).fillX().expandX().space(0);
                    section.row();
                }
            }
        }
    }
}
