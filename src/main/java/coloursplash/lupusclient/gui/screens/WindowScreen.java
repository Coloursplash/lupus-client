/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens;

import coloursplash.lupusclient.gui.WidgetScreen;
import coloursplash.lupusclient.gui.widgets.Cell;
import coloursplash.lupusclient.gui.widgets.WWidget;
import coloursplash.lupusclient.gui.widgets.WWindow;

public abstract class WindowScreen extends WidgetScreen {
    private final WWindow window;

    public WindowScreen(String title, boolean expanded) {
        super(title);

        window = super.add(new WWindow(title, expanded)).centerXY().getWidget();
    }

    @Override
    public <T extends WWidget> Cell<T> add(T widget) {
        return window.add(widget);
    }

    public void row() {
        window.row();
    }

    @Override
    public void clear() {
        window.clear();
    }
}
