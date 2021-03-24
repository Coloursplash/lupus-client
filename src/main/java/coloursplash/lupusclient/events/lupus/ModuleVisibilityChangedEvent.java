/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.events.lupus;

import coloursplash.lupusclient.modules.Module;

public class ModuleVisibilityChangedEvent {
    private static final ModuleVisibilityChangedEvent INSTANCE = new ModuleVisibilityChangedEvent();

    public Module module;

    public static ModuleVisibilityChangedEvent get(Module module) {
        INSTANCE.module = module;
        return INSTANCE;
    }
}
