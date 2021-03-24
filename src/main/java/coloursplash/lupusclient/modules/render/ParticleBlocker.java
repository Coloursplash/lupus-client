

package coloursplash.lupusclient.modules.render;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.world.ParticleEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.ParticleEffectListSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import net.minecraft.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;

public class ParticleBlocker extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<ParticleEffect>> particles = sgGeneral.add(new ParticleEffectListSetting.Builder()
            .name("particles")
            .description("Particles to block.")
            .defaultValue(new ArrayList<>(0))
            .build()
    );

    public ParticleBlocker() {
        super(Categories.Render, "particle-blocker", "Stops specified particles from rendering.");
    }

    @EventHandler
    private void onRenderParticle(ParticleEvent event) {
        if (event.particle != null && particles.get().contains(event.particle)) event.cancel();
    }
}
