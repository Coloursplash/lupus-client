

package coloursplash.lupusclient.modules.player;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.mixin.MinecraftClientAccessor;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.EnumSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;

public class FastUse extends Module {
    public enum Mode {
        All,
        Some
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
            .name("mode")
            .description("Which items to fast use.")
            .defaultValue(Mode.All)
            .build()
    );

    private final Setting<Boolean> exp = sgGeneral.add(new BoolSetting.Builder()
            .name("xP")
            .description("Fast-throws XP bottles if the mode is \"Some\".")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> blocks = sgGeneral.add(new BoolSetting.Builder()
            .name("blocks")
            .description("Fast-places blocks if the mode is \"Some\".")
            .defaultValue(false)
            .build()
    );

    public FastUse() {
        super(Categories.Player, "fast-use", "Allows you to use items at very high speeds.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        switch (mode.get()) {
            case All:
                ((MinecraftClientAccessor) mc).setItemUseCooldown(0);
                break;
            case Some:
                if ((exp.get() && (mc.player.getMainHandStack().getItem() == Items.EXPERIENCE_BOTTLE || mc.player.getOffHandStack().getItem() == Items.EXPERIENCE_BOTTLE))
                        || (blocks.get() && mc.player.getMainHandStack().getItem() instanceof BlockItem || mc.player.getOffHandStack().getItem() instanceof BlockItem))
                    ((MinecraftClientAccessor) mc).setItemUseCooldown(0);
                break;
        }
    }
}
