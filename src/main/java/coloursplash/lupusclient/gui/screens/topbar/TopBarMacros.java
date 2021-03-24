/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens.topbar;

import coloursplash.lupusclient.gui.widgets.WButton;
import coloursplash.lupusclient.gui.widgets.WLabel;
import coloursplash.lupusclient.gui.widgets.WMinus;
import coloursplash.lupusclient.gui.widgets.WTable;
import coloursplash.lupusclient.macros.EditMacroScreen;
import coloursplash.lupusclient.macros.Macro;
import coloursplash.lupusclient.macros.Macros;
import coloursplash.lupusclient.utils.Utils;
import net.minecraft.client.MinecraftClient;

public class TopBarMacros extends TopBarWindowScreen {
    public TopBarMacros() {
        super(TopBarType.Macros);

        refreshWidgetsOnInit = true;
    }

    @Override
    protected void initWidgets() {
        // Macros
        if (Macros.get().getAll().size() > 0) {
            WTable t = add(new WTable()).getWidget();

            for (Macro macro : Macros.get()) {
                t.add(new WLabel(macro.name + " (" + Utils.getKeyName(macro.key) + ")"));

                WButton edit = t.add(new WButton(WButton.ButtonRegion.Edit)).getWidget();
                edit.action = () -> MinecraftClient.getInstance().openScreen(new EditMacroScreen(macro));

                WMinus remove = t.add(new WMinus()).getWidget();
                remove.action = () -> {
                    Macros.get().remove(macro);

                    clear();
                    initWidgets();
                };

                t.row();
            }
            row();
        }

        // Create macro
        WButton create = add(new WButton("Create")).fillX().expandX().getWidget();
        create.action = () -> MinecraftClient.getInstance().openScreen(new EditMacroScreen(null));
    }
}
