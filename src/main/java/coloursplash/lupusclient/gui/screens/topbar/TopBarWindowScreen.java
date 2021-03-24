/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens.topbar;

import coloursplash.lupusclient.gui.widgets.Cell;
import coloursplash.lupusclient.gui.widgets.WWidget;
import coloursplash.lupusclient.gui.widgets.WWindow;

public abstract class TopBarWindowScreen extends TopBarScreen {
    private final WWindow window;

    protected boolean refreshWidgetsOnInit;

    public TopBarWindowScreen(TopBarType type) {
        super(type);

        window = super.add(new WWindow(type.toString(), true)).centerXY().getWidget();

        addTopBar();
    }

    @Override
    protected void init() {
        boolean wasFirstInit = firstInit;
        if (refreshWidgetsOnInit) clear();
        super.init();
        if (refreshWidgetsOnInit || wasFirstInit) initWidgets();
    }

    protected abstract void initWidgets();

    @Override
    public <T extends WWidget> Cell<T> add(T widget) {
        return window.add(widget);
    }

    protected void row() {
        window.row();
    }

    @Override
    public void clear() {
        window.clear();
    }
}
