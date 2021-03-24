/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.lupus.ModuleBindChangedEvent;
import coloursplash.lupusclient.gui.widgets.*;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.utils.Utils;

public class ModuleScreen extends WindowScreen {
    private final Module module;

    private WKeybind keybind;

    public ModuleScreen(Module module) {
        super(module.title, true);
        this.module = module;

        initWidgets();
    }

    private void initWidgets() {
        // Description
        add(new WLabel(module.description));
        row();

        // Settings
        if (module.settings.sizeGroups() > 0) {
            add(module.settings.createTable(false)).fillX().expandX().getWidget();
        }
        else {
            add(new WHorizontalSeparator());
        }

        // Custom widget
        WWidget customWidget = module.getWidget();
        if (customWidget != null) {
            if (module.settings.sizeGroups() > 0) {
                row();
                add(new WHorizontalSeparator());
            }

            Cell<WWidget> cell = add(customWidget);
            if (customWidget instanceof WTable) cell.fillX().expandX();
            row();
        }

        if (customWidget != null || module.settings.sizeGroups() > 0) {
            row();
            add(new WHorizontalSeparator());
        }

        // Bind
        keybind = add(new WKeybind(module.getKey())).getWidget();
        keybind.actionOnSet = () -> Modules.get().setModuleToBind(module);
        keybind.action = () -> module.setKey(keybind.get());
        row();

        // Toggle on key release
        WTable tokrTable = add(new WTable()).fillX().expandX().getWidget();
        tokrTable.add(new WLabel("Toggle on key release:"));
        WCheckbox toggleOnKeyRelease = tokrTable.add(new WCheckbox(module.toggleOnKeyRelease)).getWidget();
        toggleOnKeyRelease.action = () -> {
            module.toggleOnKeyRelease = toggleOnKeyRelease.checked;
            Modules.get().save();
        };
        row();

        add(new WHorizontalSeparator());

        // Bottom
        WTable bottomTable = add(new WTable()).fillX().expandX().getWidget();

        //   Active
        bottomTable.add(new WLabel("Active:"));
        WCheckbox active = bottomTable.add(new WCheckbox(module.isActive())).getWidget();
        active.action = () -> {
            if (module.isActive() != active.checked) module.toggle(Utils.canUpdate());
        };

        //   Visible
        bottomTable.add(new WLabel("Visible: ")).fillX().right().getWidget().tooltip = "Shows the module in the array list.";
        WCheckbox visibleCheckbox = bottomTable.add(new WCheckbox(module.isVisible())).getWidget();
        visibleCheckbox.tooltip = "Shows the module in the array list.";
        visibleCheckbox.action = () -> {
            if (module.isVisible() != visibleCheckbox.checked) module.setVisible(visibleCheckbox.checked);
        };
    }

    @EventHandler
    private void onModuleBindChanged(ModuleBindChangedEvent event) {
        if (event.module == module) {
            keybind.set(event.module.getKey());
        }
    }
}
