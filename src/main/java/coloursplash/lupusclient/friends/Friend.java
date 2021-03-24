/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.friends;

import coloursplash.lupusclient.utils.entity.FriendType;
import coloursplash.lupusclient.utils.misc.ISerializable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

public class Friend implements ISerializable<Friend> {
    public String name;
    public FriendType type = FriendType.Neutral;

    public Friend(String name) {
        this.name = name;
    }

    public Friend(PlayerEntity player) {
        this(player.getGameProfile().getName());
    }

    public Friend(CompoundTag tag) {
        fromTag(tag);
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", name);
        tag.putString("type", type.name());
        return tag;
    }

    @Override
    public Friend fromTag(CompoundTag tag) {
        name = tag.getString("name");
        if (tag.contains("type")) type = FriendType.valueOf(tag.getString("type"));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend friend = (Friend) o;
        return Objects.equals(name, friend.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
