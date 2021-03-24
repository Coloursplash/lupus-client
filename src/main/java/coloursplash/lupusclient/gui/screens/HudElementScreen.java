/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens;

import coloursplash.lupusclient.events.render.Render2DEvent;
import coloursplash.lupusclient.gui.widgets.WCheckbox;
import coloursplash.lupusclient.gui.widgets.WHorizontalSeparator;
import coloursplash.lupusclient.gui.widgets.WLabel;
import coloursplash.lupusclient.gui.widgets.WTable;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.render.hud.HUD;
import coloursplash.lupusclient.modules.render.hud.modules.HudElement;

public class HudElementScreen extends WindowScreen {
    private final HudElement element;

    public HudElementScreen(HudElement element) {
        super(element.title, true);
        this.element = element;

        initModules();
    }

    private void initModules() {
        // Description
        add(new WLabel(element.description));
        row();

        // Settings
        if (element.settings.sizeGroups() > 0) {
            add(element.settings.createTable(false)).fillX().expandX().getWidget();
            row();

            add(new WHorizontalSeparator());
            row();
        }

        // Bottom
        WTable bottomTable = add(new WTable()).fillX().expandX().getWidget();

        //   Active
        bottomTable.add(new WLabel("Active:"));
        WCheckbox active = bottomTable.add(new WCheckbox(element.active)).getWidget();
        active.action = () -> {
            if (element.active != active.checked) element.toggle();
        };
    }

    @Override
    protected void onRenderBefore(float delta) {
        Modules.get().get(HUD.class).onRender(Render2DEvent.get(0, 0, delta));
    }
}
