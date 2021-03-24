/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.macros;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.lupus.KeyEvent;
import coloursplash.lupusclient.utils.misc.ISerializable;
import coloursplash.lupusclient.utils.misc.NbtUtils;
import coloursplash.lupusclient.utils.misc.input.KeyAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Macro implements ISerializable<Macro> {
    public String name = "";
    public List<String> messages = new ArrayList<>(1);
    public int key = -1;

    public void addMessage(String command) {
        messages.add(command);
    }

    public void removeMessage(int i) {
        messages.remove(i);
    }

    @EventHandler
    private void onKey(KeyEvent event) {
        if (event.action != KeyAction.Release && event.key == key && MinecraftClient.getInstance().currentScreen == null) {
            for (String command : messages) {
                MinecraftClient.getInstance().player.sendChatMessage(command);
            }
            event.cancel();
        }
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        // General
        tag.putString("name", name);
        tag.putInt("key", key);

        // Messages
        ListTag messagesTag = new ListTag();
        for (String message : messages) messagesTag.add(StringTag.of(message));
        tag.put("messages", messagesTag);

        return tag;
    }

    @Override
    public Macro fromTag(CompoundTag tag) {
        name = tag.getString("name");
        key = tag.getInt("key");
        messages = NbtUtils.listFromTag(tag.getList("messages", 8), Tag::asString);

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Macro macro = (Macro) o;
        return Objects.equals(name, macro.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
