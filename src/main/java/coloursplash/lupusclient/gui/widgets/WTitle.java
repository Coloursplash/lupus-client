/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.widgets;

import coloursplash.lupusclient.gui.GuiConfig;
import coloursplash.lupusclient.gui.renderer.GuiRenderer;
import coloursplash.lupusclient.utils.render.color.Color;

public class WTitle extends WWidget {
    public Color color;

    private final String text;

    public WTitle(String text) {
        this.text = text;
        this.color = GuiConfig.get().windowHeaderText;
    }

    @Override
    protected void onCalculateSize(GuiRenderer renderer) {
        width = renderer.titleWidth(text);
        height = renderer.titleHeight();
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.title(text, x, y, color);
    }
}
