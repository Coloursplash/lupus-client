/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens.topbar;

import coloursplash.lupusclient.gui.WidgetScreen;
import coloursplash.lupusclient.gui.widgets.WTopBar;

public abstract class TopBarScreen extends WidgetScreen {
    public final TopBarType type;

    public TopBarScreen(TopBarType type) {
        super(type.toString());
        this.type = type;
    }

    protected void addTopBar() {
        super.add(new WTopBar()).centerX();
    }
}
