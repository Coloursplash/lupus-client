/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens.settings;

import coloursplash.lupusclient.gui.widgets.WItemWithLabel;
import coloursplash.lupusclient.gui.widgets.WWidget;
import coloursplash.lupusclient.mixin.IdentifierAccessor;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.utils.misc.Names;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class BlockListSettingScreen extends LeftRightListSettingScreen<Block> {
    private static final Identifier ID = new Identifier("minecraft", "");

    public BlockListSettingScreen(Setting<List<Block>> setting) {
        super("Select blocks", setting, Registry.BLOCK);
    }

    @Override
    protected boolean includeValue(Block value) {
        return value != Blocks.AIR;
    }

    @Override
    protected WWidget getValueWidget(Block value) {
        return new WItemWithLabel(value.asItem().getDefaultStack(), getValueName(value));
    }

    @Override
    protected String getValueName(Block value) {
        return Names.get(value);
    }

    @Override
    protected boolean skipValue(Block value) {
        return Registry.BLOCK.getId(value).getPath().endsWith("_wall_banner");
    }

    @Override
    protected Block getAdditionalValue(Block value) {
        String path = Registry.BLOCK.getId(value).getPath();
        if (!path.endsWith("_banner")) return null;

        ((IdentifierAccessor) ID).setPath(path.substring(0, path.length() - 6) + "wall_banner");
        return Registry.BLOCK.get(ID);
    }
}
