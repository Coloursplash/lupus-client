/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.systems;

import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.utils.files.StreamUtils;
import coloursplash.lupusclient.utils.misc.ISerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

import java.io.File;
import java.io.IOException;

public abstract class System<T> implements ISerializable<T> {
    private File file;

    public System(String name) {
        if (name != null) {
            this.file = new File(LupusClient.FOLDER, name + ".nbt");
        }
    }

    public void init() {}

    public void save(File folder) {
        File file = getFile();
        if (file == null) return;

        CompoundTag tag = toTag();
        if (tag == null) return;

        try {
            File tempFile = File.createTempFile("lupus-client", file.getName());
            NbtIo.write(tag, tempFile);

            if (folder != null) file = new File(folder, file.getName());

            file.getParentFile().mkdirs();
            StreamUtils.copy(tempFile, file);
            tempFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void save() {
        save(null);
    }

    public void load(File folder) {
        File file = getFile();
        if (file == null) return;

        try {
            if (folder != null) file = new File(folder, file.getName());

            if (file.exists()) {
                fromTag(NbtIo.read(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void load() {
        load(null);
    }

    public File getFile() {
        return file;
    }


    @Override
    public CompoundTag toTag() {
        return null;
    }

    @Override
    public T fromTag(CompoundTag tag) {
        return null;
    }
}
