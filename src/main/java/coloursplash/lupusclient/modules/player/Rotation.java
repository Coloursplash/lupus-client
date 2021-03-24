

package coloursplash.lupusclient.modules.player;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.DoubleSetting;
import coloursplash.lupusclient.settings.EnumSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;

public class Rotation extends Module {
    public enum LockMode {
        Smart,
        Simple,
        None
    }

    private final SettingGroup sgYaw = settings.createGroup("Yaw");
    private final SettingGroup sgPitch = settings.createGroup("Pitch");

    // Yaw

    private final Setting<LockMode> yawLockMode = sgYaw.add(new EnumSetting.Builder<LockMode>()
            .name("yaw-lock-mode")
            .description("The way in which your yaw is locked.")
            .defaultValue(LockMode.Simple)
            .build()
    );

    private final Setting<Double> yawAngle = sgYaw.add(new DoubleSetting.Builder()
            .name("yaw-angle")
            .description("Yaw angle in degrees.")
            .defaultValue(0)
            .sliderMax(360)
            .max(360)
            .build()
    );

    // Pitch

    private final Setting<LockMode> pitchLockMode = sgPitch.add(new EnumSetting.Builder<LockMode>()
            .name("pitch-lock-mode")
            .description("The way in which your pitch is locked.")
            .defaultValue(LockMode.Simple)
            .build()
    );

    private final Setting<Double> pitchAngle = sgPitch.add(new DoubleSetting.Builder()
            .name("pitch-angle")
            .description("Pitch angle in degrees.")
            .defaultValue(0)
            .min(-90)
            .max(90)
            .sliderMin(-90)
            .sliderMax(90)
            .build()
    );

    public Rotation() {
        super(Categories.Player, "rotation", "Changes/locks your yaw and pitch.");
    }

    @Override
    public void onActivate() {
        onTick(null);
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        switch (yawLockMode.get()) {
            case Simple:
                setYawAngle(yawAngle.get().floatValue());
                break;
            case Smart:
                setYawAngle(getSmartYawDirection());
                break;
        }

        switch (pitchLockMode.get()) {
            case Simple:
                mc.player.pitch = pitchAngle.get().floatValue();
                break;
            case Smart:
                mc.player.pitch = getSmartPitchDirection();
                break;
        }
    }

    private float getSmartYawDirection() {
        return Math.round((mc.player.yaw + 1f) / 45f) * 45f;
    }

    private float getSmartPitchDirection() {
        return Math.round((mc.player.pitch + 1f) / 30f) * 30f;
    }

    private void setYawAngle(float yawAngle) {
        mc.player.yaw = yawAngle;
        mc.player.headYaw = yawAngle;
        mc.player.bodyYaw = yawAngle;
    }
}
