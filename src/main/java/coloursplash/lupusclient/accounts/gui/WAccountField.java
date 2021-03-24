/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.accounts.gui;

import coloursplash.lupusclient.gui.widgets.WTextBox;


public class WAccountField extends WTextBox {
    public WAccountField(String text, double width) {
        super(text, width);
    }

    @Override
    protected boolean addChar(char c) {
        if(c != ' ') {
            return super.addChar(c);
        }
        return false;
    }
}