/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.accounts.gui;

import coloursplash.lupusclient.accounts.Accounts;
import coloursplash.lupusclient.accounts.types.CrackedAccount;
import coloursplash.lupusclient.gui.screens.WindowScreen;
import coloursplash.lupusclient.gui.widgets.WButton;
import coloursplash.lupusclient.gui.widgets.WLabel;

public class AddCrackedAccountScreen extends WindowScreen {


    public AddCrackedAccountScreen() {
        super("Add Cracked Account", true);

        // Name
        add(new WLabel("Name:"));
        WAccountField name = add(new WAccountField("", 400)).getWidget();
        name.setFocused(true);
        row();

        // Add
        WButton add = add(new WButton("Add")).fillX().expandX().getWidget();
        add.action = () -> {
            CrackedAccount account = new CrackedAccount(name.getText());
            if (!name.getText().trim().isEmpty() && !(Accounts.get().exists(account))) {
                AccountsScreen.addAccount(add, this, account);
            }
        };

    }
}