/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.accounts.gui;

import coloursplash.lupusclient.accounts.Accounts;
import coloursplash.lupusclient.accounts.types.PremiumAccount;
import coloursplash.lupusclient.gui.screens.WindowScreen;
import coloursplash.lupusclient.gui.widgets.WButton;
import coloursplash.lupusclient.gui.widgets.WLabel;

public class AddPremiumAccountScreen extends WindowScreen {
    public AddPremiumAccountScreen() {
        super("Add Premium Account", true);

        // Email
        add(new WLabel("Email:"));
        WAccountField email = add(new WAccountField("", 400)).getWidget();
        email.setFocused(true);
        row();

        // Password
        add(new WLabel("Password:"));
        WAccountField password = add(new WAccountField("", 400)).getWidget();
        row();

        // Add
        WButton add = add(new WButton("Add")).fillX().expandX().getWidget();
        add.action = () -> {
            PremiumAccount account = new PremiumAccount(email.getText(), password.getText());
            if (!email.getText().isEmpty() && !password.getText().isEmpty() && email.getText().contains("@") && !Accounts.get().exists(account)) {
                AccountsScreen.addAccount(add, this, account);
            }
        };
    }
}