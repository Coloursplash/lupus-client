/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.widgets;

import coloursplash.lupusclient.gui.GuiConfig;
import coloursplash.lupusclient.gui.renderer.GuiRenderer;
import coloursplash.lupusclient.utils.render.color.Color;

public class WTriangle extends WPressable {
    public Color color, colorHovered, colorPressed;

    public double rotation;

    public WTriangle() {
        color = GuiConfig.get().background;
        colorHovered = GuiConfig.get().backgroundHovered;
        colorPressed = GuiConfig.get().backgroundPressed;
    }

    @Override
    protected void onCalculateSize(GuiRenderer renderer) {
        width = height = renderer.textHeight();
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        Color color;
        if (pressed) color = colorPressed;
        else if (mouseOver) color = colorHovered;
        else color = this.color;

        renderer.triangle(x, y + width / 4, width, rotation, color);
    }
}
