/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.accounts;

import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.utils.misc.ISerializable;
import coloursplash.lupusclient.utils.misc.NbtException;
import coloursplash.lupusclient.utils.render.ByteTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

public class AccountCache implements ISerializable<AccountCache> {

    public String username = "";
    public String uuid = "";

    private ByteTexture headTexture;

    public ByteTexture getHeadTexture() {
        return headTexture;
    }

    public boolean makeHead(String skinUrl) {
        try {
            BufferedImage skin;
            byte[] head = new byte[8 * 8 * 3];
            int[] pixel = new int[4];

            if (skinUrl.equals("steve"))
                skin = ImageIO.read(MinecraftClient.getInstance().getResourceManager().getResource(new Identifier("lupus-client", "steve.png")).getInputStream());
            else skin = ImageIO.read(new URL(skinUrl));

            // Whole picture
            // TODO: Find a better way to do it
            int i = 0;
            for (int x = 0; x < 4 + 4; x++) {
                for (int y = 0; y < 4 + 4; y++) {
                    skin.getData().getPixel(x, y, pixel);

                    for (int j = 0; j < 3; j++) {
                        head[i] = (byte) pixel[j];
                        i++;
                    }
                }
            }

            headTexture = new ByteTexture(8, 8, head, false);
            return true;
        } catch (Exception e) {
            LupusClient.LOG.error("Failed to read skin url. (" + skinUrl + ")");
            return false;
        }
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        tag.putString("username", username);
        tag.putString("uuid", uuid);

        return tag;
    }

    @Override
    public AccountCache fromTag(CompoundTag tag) {
        if (!tag.contains("username") || !tag.contains("uuid")) throw new NbtException();

        username = tag.getString("username");
        uuid = tag.getString("uuid");

        return this;
    }
}