/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.waypoints;

import coloursplash.lupusclient.rendering.DrawMode;
import coloursplash.lupusclient.rendering.MeshBuilder;
import coloursplash.lupusclient.utils.misc.ISerializable;
import coloursplash.lupusclient.utils.render.color.SettingColor;
import coloursplash.lupusclient.utils.world.Dimension;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.nbt.CompoundTag;

public class Waypoint implements ISerializable<Waypoint> {
    private static final MeshBuilder MB;

    static {
        MB = new MeshBuilder(128);
        MB.texture = true;
    }

    public String name = "Lupus on Crack!";
    public String icon = "Square";
    public SettingColor color = new SettingColor(225, 25, 25);

    public int x, y, z;

    public boolean visible = true;
    public int maxVisibleDistance = 1000;
    public double scale = 1;

    public boolean overworld, nether, end;

    public Dimension actualDimension;

    public void renderIcon(double x, double y, double z, double a, double size) {
        MB.begin(null, DrawMode.Triangles, VertexFormats.POSITION_TEXTURE_COLOR);

        int preA = color.a;
        color.a *= a;

        MB.pos(x, y, z).texture(0, 0).color(color).endVertex();
        MB.pos(x + size, y, z).texture(1, 0).color(color).endVertex();
        MB.pos(x + size, y + size, z).texture(1, 1).color(color).endVertex();

        MB.pos(x, y, z).texture(0, 0).color(color).endVertex();
        MB.pos(x + size, y + size, z).texture(1, 1).color(color).endVertex();
        MB.pos(x, y + size, z).texture(0, 1).color(color).endVertex();

        Waypoints.get().icons.get(icon).bindTexture();
        MB.end();

        color.a = preA;
    }

    private int findIconIndex() {
        int i = 0;
        for (String icon : Waypoints.get().icons.keySet()) {
            if (this.icon.equals(icon)) return i;
            i++;
        }

        return -1;
    }

    private int correctIconIndex(int i) {
        if (i < 0) return Waypoints.get().icons.size() + i;
        else if (i >= Waypoints.get().icons.size()) return i - Waypoints.get().icons.size();
        return i;
    }

    private String getIcon(int i) {
        i = correctIconIndex(i);

        int _i = 0;
        for (String icon : Waypoints.get().icons.keySet()) {
            if (_i == i) return icon;
            _i++;
        }

        return "Square";
    }

    public void prevIcon() {
        icon = getIcon(findIconIndex() - 1);
    }

    public void nextIcon() {
        icon = getIcon(findIconIndex() + 1);
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        tag.putString("name", name);
        tag.putString("icon", icon);
        tag.put("color", color.toTag());

        tag.putInt("x", x);
        tag.putInt("y", y);
        tag.putInt("z", z);

        tag.putBoolean("visible", visible);
        tag.putInt("maxVisibleDistance", maxVisibleDistance);
        tag.putDouble("scale", scale);

        tag.putString("dimension", actualDimension.name());

        tag.putBoolean("overworld", overworld);
        tag.putBoolean("nether", nether);
        tag.putBoolean("end", end);

        return tag;
    }

    @Override
    public Waypoint fromTag(CompoundTag tag) {
        name = tag.getString("name");
        icon = tag.getString("icon");
        color.fromTag(tag.getCompound("color"));

        x = tag.getInt("x");
        y = tag.getInt("y");
        z = tag.getInt("z");

        visible = tag.getBoolean("visible");
        maxVisibleDistance = tag.getInt("maxVisibleDistance");
        scale = tag.getDouble("scale");

        actualDimension = Dimension.valueOf(tag.getString("dimension"));

        overworld = tag.getBoolean("overworld");
        nether = tag.getBoolean("nether");
        end = tag.getBoolean("end");

        if (!Waypoints.get().icons.containsKey(icon)) icon = "Square";

        return this;
    }
}
