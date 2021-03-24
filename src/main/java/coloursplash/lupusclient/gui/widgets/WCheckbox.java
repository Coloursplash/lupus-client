/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.widgets;

import coloursplash.lupusclient.gui.GuiConfig;
import coloursplash.lupusclient.gui.renderer.GuiRenderer;
import coloursplash.lupusclient.gui.renderer.Region;
import coloursplash.lupusclient.utils.Utils;

public class WCheckbox extends WPressable {
    public boolean checked;

    private double animationProgress;

    public WCheckbox(boolean checked) {
        this.checked = checked;
        this.animationProgress = checked ? 1 : 0;
    }

    @Override
    protected void onAction(int button) {
        checked = !checked;
    }

    @Override
    protected void onCalculateSize(GuiRenderer renderer) {
        double h = renderer.textHeight();
        double s = GuiConfig.get().guiScale;
        width = 6 * s + h + 6 * s;
        height = 6 * s + h + 6 * s;
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.background(this, pressed);

        animationProgress += delta * (checked ? 1 : -1);
        animationProgress = Utils.clamp(animationProgress, 0, 1);

        double h = renderer.textHeight();

        if (animationProgress > 0) {
            double s = GuiConfig.get().guiScale;
            renderer.quad(Region.FULL, x + 6 * s + h / 2 * (1 - animationProgress), y + 6 * s + h / 2 * (1 - animationProgress), h * animationProgress, h * animationProgress, pressed ? GuiConfig.get().checkboxPressed : GuiConfig.get().checkbox);
        }
    }
}
