/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.accounts.gui;

import coloursplash.lupusclient.accounts.types.TheAlteningAccount;
import coloursplash.lupusclient.gui.screens.WindowScreen;
import coloursplash.lupusclient.gui.widgets.WButton;
import coloursplash.lupusclient.gui.widgets.WLabel;

public class AddTheAlteningAccountScreen extends WindowScreen {
    public AddTheAlteningAccountScreen() {
        super("Add The Altening Account", true);

        // Token
        add(new WLabel("Token:"));
        WAccountField token = add(new WAccountField("", 400)).getWidget();
        token.setFocused(true);
        row();

        // Add
        WButton add = add(new WButton("Add")).fillX().expandX().getWidget();
        add.action = () -> {
            if (!token.getText().isEmpty()) {
                AccountsScreen.addAccount(add, this, new TheAlteningAccount(token.getText()));
            }
        };
    }
}