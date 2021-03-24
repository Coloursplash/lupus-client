

package coloursplash.lupusclient.modules.player;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.mixin.StatusEffectInstanceAccessor;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.settings.StatusEffectSetting;
import coloursplash.lupusclient.utils.Utils;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

public class PotionSpoof extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Object2IntMap<StatusEffect>> potions = sgGeneral.add(new StatusEffectSetting.Builder()
            .name("potions")
            .description("Potions to add.")
            .defaultValue(Utils.createStatusEffectMap())
            .build()
    );

    public PotionSpoof() {
        super(Categories.Player, "potion-spoof", "Spoofs specified potion effects for you. SOME effects DO NOT work.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        for (StatusEffect statusEffect : potions.get().keySet()) {
            int level = potions.get().getInt(statusEffect);
            if (level <= 0) continue;

            if (mc.player.hasStatusEffect(statusEffect)) {
                StatusEffectInstance instance = mc.player.getStatusEffect(statusEffect);
                ((StatusEffectInstanceAccessor) instance).setAmplifier(level - 1);
                if (instance.getDuration() < 20) ((StatusEffectInstanceAccessor) instance).setDuration(20);
            } else {
                mc.player.addStatusEffect(new StatusEffectInstance(statusEffect, 20, level - 1));
            }
        }
    }
}
