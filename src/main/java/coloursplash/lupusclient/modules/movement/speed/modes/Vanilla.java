

package coloursplash.lupusclient.modules.movement.speed.modes;

import coloursplash.lupusclient.events.entity.player.PlayerMoveEvent;
import coloursplash.lupusclient.mixininterface.IVec3d;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.movement.Anchor;
import coloursplash.lupusclient.modules.movement.AutoJump;
import coloursplash.lupusclient.modules.movement.speed.SpeedMode;
import coloursplash.lupusclient.modules.movement.speed.SpeedModes;
import coloursplash.lupusclient.utils.player.PlayerUtils;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;

public class Vanilla extends SpeedMode {
    public Vanilla() {
        super(SpeedModes.Vanilla);
    }

    @Override
    public void onMove(PlayerMoveEvent event) {
        Vec3d vel = PlayerUtils.getHorizontalVelocity(settings.speed.get());
        double velX = vel.getX();
        double velZ = vel.getZ();

        if (settings.applySpeedPotions.get() && mc.player.hasStatusEffect(StatusEffects.SPEED)) {
            double value = (mc.player.getStatusEffect(StatusEffects.SPEED).getAmplifier() + 1) * 0.205;
            velX += velX * value;
            velZ += velZ * value;
        }

        Anchor anchor = Modules.get().get(Anchor.class);
        if (anchor.isActive() && anchor.controlMovement) {
            velX = anchor.deltaX;
            velZ = anchor.deltaZ;
        }

        ((IVec3d) event.movement).set(velX, event.movement.y, velZ);
    }

    @Override
    public void onTick() {
        if (settings.jump.get()) {
            if (!mc.player.isOnGround() || mc.player.isSneaking() || !jump()) return;
            if (settings.jumpMode.get() == AutoJump.Mode.Jump) mc.player.jump();
            else ((IVec3d) mc.player.getVelocity()).setY(settings.hopHeight.get());
        }
    }

    private boolean jump() {
        switch (settings.jumpIf.get()) {
            case Sprinting: return PlayerUtils.isSprinting();
            case Walking:   return PlayerUtils.isMoving();
            case Always:    return true;
            default:        return false;
        }
    }
}
