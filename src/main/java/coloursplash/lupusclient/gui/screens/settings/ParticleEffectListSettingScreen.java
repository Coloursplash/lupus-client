/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.gui.screens.settings;

import coloursplash.lupusclient.gui.screens.WindowScreen;
import coloursplash.lupusclient.gui.widgets.WCheckbox;
import coloursplash.lupusclient.gui.widgets.WLabel;
import coloursplash.lupusclient.gui.widgets.WTextBox;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.utils.misc.Names;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ParticleEffectListSettingScreen extends WindowScreen {

    private final Setting<List<ParticleEffect>> setting;
    private final WTextBox filter;
    private String filterText = "";

    public ParticleEffectListSettingScreen(Setting<List<ParticleEffect>> setting) {
        super("Particle Effects", true);
        this.setting = setting;

        filter = new WTextBox("", 400);
        filter.setFocused(true);
        filter.action = () -> {
            filterText = filter.getText().trim();

            clear();
            initWidgets();
        };

        initWidgets();
    }

    private void initWidgets() {

        add(filter).fillX().expandX().getWidget();
        row();

        for (ParticleType<?> particleType : Registry.PARTICLE_TYPE) {
            if (!(particleType instanceof ParticleEffect)) continue;

            ParticleEffect effect = (ParticleEffect) particleType;
            String name = Names.get(effect);

            if (!filterText.isEmpty()) if (!StringUtils.containsIgnoreCase(name, filterText)) continue;

            add(new WLabel(name));
            WCheckbox checkbox = add(new WCheckbox(setting.get().contains(effect))).fillX().right().getWidget();
            checkbox.action = () -> {
                if (checkbox.checked && !setting.get().contains(effect)) {
                    setting.get().add(effect);
                    setting.changed();
                } else if (!checkbox.checked && setting.get().remove(effect)) {
                    setting.changed();
                }
            };

            row();
        }
    }
}