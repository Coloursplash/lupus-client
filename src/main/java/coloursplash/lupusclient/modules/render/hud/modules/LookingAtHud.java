

package coloursplash.lupusclient.modules.render.hud.modules;

import coloursplash.lupusclient.modules.render.hud.HUD;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class LookingAtHud extends DoubleTextHudElement {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> blockPosition = sgGeneral.add(new BoolSetting.Builder()
            .name("block-position")
            .description("Displays block's position.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> entityPosition = sgGeneral.add(new BoolSetting.Builder()
            .name("entity-position")
            .description("Displays entity's position.")
            .defaultValue(true)
            .build()
    );

    public LookingAtHud(HUD hud) {
        super(hud, "looking-at", "Displays what entity or block you are looking at.", "Looking At: ");
    }

    @Override
    protected String getRight() {
        if (isInEditor()) return blockPosition.get() ? "Obsidian [0, 0, 0]" : "Obsidian";

        if (mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            String name = mc.world.getBlockState(((BlockHitResult) mc.crosshairTarget).getBlockPos()).getBlock().getName().getString();
            Vec3d pos = mc.crosshairTarget.getPos();

            return blockPosition.get() ? String.format("%s [%d, %d, %d]", name, (int) pos.x, (int) pos.y, (int) pos.z) : name;
        }
        else if (mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            String name = ((EntityHitResult) mc.crosshairTarget).getEntity().getDisplayName().getString();
            Vec3d pos = mc.crosshairTarget.getPos();

            return entityPosition.get() ? String.format("%s [%d, %d, %d]", name, (int) pos.x, (int) pos.y, (int) pos.z) : name;
        }

        return "";
    }
}
