/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.utils.files;

import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.systems.Systems;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileUtils {
    private static final File FOLDER = new File(LupusClient.FOLDER, "profiles");

    public static List<String> getProfiles() {
      String[] children = FOLDER.list();
      List<String> profiles = new ArrayList<>(0);

      if (children != null) {
          for (String child : children) {
              File file = new File(child);
              if (!child.contains(".")) profiles.add(file.getName());
          }
      }

      return profiles;
    }

    public static boolean isProfile(String profile) {
        String[] children = FOLDER.list();

        if (children != null) {
            for (String child : children) {
                if (child.equals(profile)) return true;
            }
        }

        return false;
    }

    public static boolean save(String profile) {
        if (profile.isEmpty() || profile.contains(".")) return false;
        File folder = new File(FOLDER, profile);

        Systems.save(folder);
        return true;
    }

    public static void load(String profile) {
        File folder = new File(FOLDER, profile);

        Systems.save();
        Systems.load(folder);
    }

    public static void delete(String profile) {
        try {
            FileUtils.deleteDirectory(new File(FOLDER, profile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
