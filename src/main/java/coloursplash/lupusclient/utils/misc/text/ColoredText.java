/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.utils.misc.text;

import coloursplash.lupusclient.utils.render.color.Color;

import java.util.Objects;

/**
 * Encapsulates a string and the color it should have. See {@link TextUtils}
 */
public class ColoredText {
    private final String text;
    private final Color color;

    public ColoredText(String text, Color color)
    {
        this.text = text;
        this.color = color;
    }

    public String getText()
    {
        return text;
    }

    public Color getColor()
    {
        return color;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColoredText that = (ColoredText)o;
        return text.equals(that.text) && color.equals(that.color);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(text, color);
    }
}