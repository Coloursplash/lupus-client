/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.widgets;

import coloursplash.lupusclient.gui.GuiConfig;
import coloursplash.lupusclient.modules.Category;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.modules.Modules;

public class WModuleCategory extends WWindow {
    public WModuleCategory(Category category) {
        super(category.toString(), category.windowConfig.isExpanded(), true);
        this.type = GuiConfig.WindowType.Category;
        this.category = category;

        action = () -> getWindowConfig().setPos(x, y);

        pad(0);
        getDefaultCell().space(0);

        for (Module module : Modules.get().getGroup(category)) {
            add(new WModule(module)).fillX().expandX();
            row();
        }
    }
}
