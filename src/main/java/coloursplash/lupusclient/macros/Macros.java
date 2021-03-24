/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.macros;

import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.systems.System;
import coloursplash.lupusclient.systems.Systems;
import coloursplash.lupusclient.utils.misc.NbtUtils;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Macros extends System<Macros> implements Iterable<Macro> {
    private List<Macro> macros = new ArrayList<>();

    public Macros() {
        super("macros");
    }

    public static Macros get() {
        return Systems.get(Macros.class);
    }

    @Override
    public void init() {}

    public void add(Macro macro) {
        macros.add(macro);
        LupusClient.EVENT_BUS.subscribe(macro);
        save();
    }

    public List<Macro> getAll() {
        return macros;
    }

    public void remove(Macro macro) {
        if (macros.remove(macro)) {
            LupusClient.EVENT_BUS.unsubscribe(macro);
            save();
        }
    }

    @Override
    public Iterator<Macro> iterator() {
        return macros.iterator();
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("macros", NbtUtils.listToTag(macros));
        return tag;
    }

    @Override
    public Macros fromTag(CompoundTag tag) {
        for (Macro macro : macros) LupusClient.EVENT_BUS.unsubscribe(macro);

        macros = NbtUtils.listFromTag(tag.getList("macros", 10), tag1 -> new Macro().fromTag((CompoundTag) tag1));

        for (Macro macro : macros) LupusClient.EVENT_BUS.subscribe(macro);
        return this;
    }
}
