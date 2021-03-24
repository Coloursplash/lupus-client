/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.accounts.gui;

import coloursplash.lupusclient.accounts.Account;
import coloursplash.lupusclient.accounts.Accounts;
import coloursplash.lupusclient.gui.WidgetScreen;
import coloursplash.lupusclient.gui.screens.WindowScreen;
import coloursplash.lupusclient.gui.widgets.WButton;
import coloursplash.lupusclient.gui.widgets.WTable;
import coloursplash.lupusclient.utils.network.MeteorExecutor;
import net.minecraft.client.MinecraftClient;

public class AccountsScreen extends WindowScreen {
    public AccountsScreen() {
        super("Accounts", true);
    }

    @Override
    protected void init() {
        clear();
        super.init();
        initWidgets();
    }

    void initWidgets() {
        // Accounts
        if (Accounts.get().size() > 0) {
            WTable t = add(new WTable()).fillX().expandX().getWidget();
            row();

            for (Account<?> account : Accounts.get()) {
                t.add(new WAccount(this, account, () -> {
                    clear();
                    initWidgets();
                })).fillX().expandX();
                t.row();
            }
        }

        // Add account
        WTable t = add(new WTable()).fillX().expandX().getWidget();
        addButton(t, "Cracked", () -> MinecraftClient.getInstance().openScreen(new AddCrackedAccountScreen()));
        addButton(t, "Premium", () -> MinecraftClient.getInstance().openScreen(new AddPremiumAccountScreen()));
        addButton(t, "The Altening", () -> MinecraftClient.getInstance().openScreen(new AddTheAlteningAccountScreen()));
    }

    private void addButton(WTable t, String text, Runnable action) {
        WButton button = t.add(new WButton(text)).fillX().expandX().getWidget();
        button.action = action;
    }

    static void addAccount(WButton add, WidgetScreen screen, Account<?> account) {
        add.setText("...");
        screen.locked = true;

        MeteorExecutor.execute(() -> {
            if (account.fetchInfo() && account.fetchHead()) {
                Accounts.get().add(account);
                screen.locked = false;
                screen.onClose();
            }

            add.setText("Add");
            screen.locked = false;
        });
    }
}
