

package coloursplash.lupusclient.modules.render;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.ParticleEffectListSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import net.minecraft.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;

public class Trail extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<ParticleEffect>> particles = sgGeneral.add(new ParticleEffectListSetting.Builder()
            .name("particles")
            .description("Particles to draw.")
            .defaultValue(new ArrayList<>(0))
            .build()
    );

    private final Setting<Boolean> pause = sgGeneral.add(new BoolSetting.Builder()
            .name("pause-when-stationary")
            .description("Whether or not to add particles when you are not moving.")
            .defaultValue(true)
            .build()
    );


    public Trail() {
        super(Categories.Render, "trail", "Renders a customizable trail behind your player.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (pause.get() && mc.player.input.movementForward == 0f && mc.player.input.movementSideways == 0f && !mc.options.keyJump.isPressed()) return;
        for (ParticleEffect particleEffect : particles.get()) {
            mc.world.addParticle(particleEffect, mc.player.getX(), mc.player.getY(), mc.player.getZ(), 0, 0, 0);
        }
    }
}
