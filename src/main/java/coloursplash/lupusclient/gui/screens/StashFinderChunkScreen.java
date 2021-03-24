/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens;

import coloursplash.lupusclient.gui.widgets.WHorizontalSeparator;
import coloursplash.lupusclient.gui.widgets.WLabel;
import coloursplash.lupusclient.modules.world.StashFinder;

public class StashFinderChunkScreen extends WindowScreen {
    public StashFinderChunkScreen(StashFinder.Chunk chunk) {
        super("Chunk at " + chunk.x + ", " + chunk.z, true);

        // Total
        add(new WLabel("Total:"));
        add(new WLabel(chunk.getTotal() + ""));
        row();

        add(new WHorizontalSeparator());
        row();

        // Separate
        add(new WLabel("Chests:"));
        add(new WLabel(chunk.chests + ""));
        row();

        add(new WLabel("Barrels:"));
        add(new WLabel(chunk.barrels + ""));
        row();

        add(new WLabel("Shulkers:"));
        add(new WLabel(chunk.shulkers + ""));
        row();

        add(new WLabel("Ender Chests:"));
        add(new WLabel(chunk.enderChests + ""));
        row();

        add(new WLabel("Furnaces:"));
        add(new WLabel(chunk.furnaces + ""));
        row();

        add(new WLabel("Dispensers and droppers:"));
        add(new WLabel(chunk.dispensersDroppers + ""));
        row();

        add(new WLabel("Hoppers:"));
        add(new WLabel(chunk.hoppers + ""));
    }
}
