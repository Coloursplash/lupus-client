/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.rendering.text;

import coloursplash.lupusclient.Config;
import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.utils.render.color.Color;

public interface TextRenderer {
    static TextRenderer get() {
        return Config.get().customFont ? LupusClient.FONT : VanillaTextRenderer.INSTANCE;
    }

    void begin(double scale, boolean scaleOnly, boolean big);
    default void begin(double scale) { begin(scale, false, false); }
    default void begin() { begin(1, false, false); }

    default void beginBig() { begin(1, false, true); }

    double getWidth(String text, int length);
    default double getWidth(String text) { return getWidth(text, text.length()); }

    double getHeight();

    double render(String text, double x, double y, Color color, boolean shadow);
    default double render(String text, double x, double y, Color color) { return render(text, x, y, color, false); }

    boolean isBuilding();

    void end();
}
