

package coloursplash.lupusclient.modules.movement;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.DoubleSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ClickTP extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> maxDistance = sgGeneral.add(new DoubleSetting.Builder()
            .name("max-distance")
            .description("The maximum distance you can teleport.")
            .defaultValue(5)
            .build()
    );

    public ClickTP() {
        super(Categories.Movement, "click-tp", "Teleports you to the block you click on.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player.isUsingItem()) return;

        if (mc.options.keyUse.isPressed()) {
            HitResult hitResult = mc.player.raycast(maxDistance.get(), 1f / 20f, false);

            if (hitResult.getType() == HitResult.Type.ENTITY && mc.player.interact(((EntityHitResult) hitResult).getEntity(), Hand.MAIN_HAND) != ActionResult.PASS) return;

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult) hitResult).getBlockPos();
                Direction side = ((BlockHitResult) hitResult).getSide();

                if (mc.world.getBlockState(pos).onUse(mc.world, mc.player, Hand.MAIN_HAND, (BlockHitResult) hitResult) != ActionResult.PASS) return;

                mc.player.updatePosition(pos.getX() + 0.5 + side.getOffsetX(), pos.getY() + side.getOffsetY(), pos.getZ() + 0.5 + side.getOffsetZ());
            }
        }
    }
}
