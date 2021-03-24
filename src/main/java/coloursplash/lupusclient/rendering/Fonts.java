/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.rendering;

import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.rendering.text.CustomTextRenderer;

import java.io.*;

public class Fonts {
    public static void reset() {
        File[] files = LupusClient.FOLDER.exists() ? LupusClient.FOLDER.listFiles() : new File[0];
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".ttf") || file.getName().endsWith(".TTF")) {
                    file.delete();
                }
            }
        }
    }

    public static void init() {
        File[] files = LupusClient.FOLDER.exists() ? LupusClient.FOLDER.listFiles() : new File[0];
        File fontFile = null;
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".ttf") || file.getName().endsWith(".TTF")) {
                    fontFile = file;
                    break;
                }
            }
        }

        if (fontFile == null) {
            try {
                fontFile = new File(LupusClient.FOLDER, "JetBrainsMono-Regular.ttf");
                fontFile.getParentFile().mkdirs();

                InputStream in = LupusClient.class.getResourceAsStream("/assets/lupus-client/JetBrainsMono-Regular.ttf");
                OutputStream out = new FileOutputStream(fontFile);

                byte[] bytes = new byte[255];
                int read;
                while ((read = in.read(bytes)) > 0) out.write(bytes, 0, read);

                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LupusClient.FONT = new CustomTextRenderer(fontFile);
    }
}
