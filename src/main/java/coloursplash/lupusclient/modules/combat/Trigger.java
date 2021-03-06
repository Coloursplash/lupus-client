

package coloursplash.lupusclient.modules.combat;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class Trigger extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    
    private final Setting<Boolean> whenHoldingLeftClick = sgGeneral.add(new BoolSetting.Builder()
            .name("when-holding-left-click")
            .description("Attacks only when you are holding left click.")
            .defaultValue(false)
            .build()
    );

    public Trigger() {
        super(Categories.Combat, "trigger", "Automatically swings when you look at entities.");
    }

    private Entity target;

    @EventHandler
    private void onTick(TickEvent.Post event) {
        target = null;

        if (mc.player.getHealth() <= 0 || mc.player.getAttackCooldownProgress(0.5f) < 1) return;
        if (!(mc.targetedEntity instanceof LivingEntity)) return;
        if (((LivingEntity) mc.targetedEntity).getHealth() <= 0) return;

        target = mc.targetedEntity;

        if (whenHoldingLeftClick.get()) {
            if (mc.options.keyAttack.isPressed()) attack(target);
        } else {
            attack(target);
        }
    }

    private void attack(Entity entity) {
        mc.interactionManager.attackEntity(mc.player, entity);
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    @Override
    public String getInfoString() {
        if (target != null && target instanceof PlayerEntity) return target.getEntityName();
        if (target != null) return target.getType().getName().getString();
        return null;
    }
}
