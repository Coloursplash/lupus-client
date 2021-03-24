/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.widgets;

import coloursplash.lupusclient.gui.GuiConfig;
import coloursplash.lupusclient.gui.renderer.GuiRenderer;
import coloursplash.lupusclient.gui.renderer.Region;
import coloursplash.lupusclient.utils.render.color.Color;

public class WQuad extends WWidget {
    public Color color;

    public WQuad(Color color) {
        this.color = color;
    }

    @Override
    protected void onCalculateSize(GuiRenderer renderer) {
        double s = GuiConfig.get().guiScale;
        width = 6 * s + renderer.textHeight() + 6 * s;
        height = 6 * s + renderer.textHeight() + 6 * s;
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.quad(Region.FULL, x, y, width, height, color);
    }
}
