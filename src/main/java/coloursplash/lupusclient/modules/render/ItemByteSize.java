

package coloursplash.lupusclient.modules.render;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.game.GetTooltipEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.settings.BoolSetting;
import coloursplash.lupusclient.settings.EnumSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.utils.misc.ByteCountDataOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class ItemByteSize extends Module {
    public enum Mode {
        Standard,
        True
    }

    private final SettingGroup sgUseKbIfBigEnough = settings.createGroup("Use KB if big enough");

    private final Setting<Boolean> useKbIfBigEnoughEnabled = sgUseKbIfBigEnough.add(new BoolSetting.Builder()
            .name("use-kb-if-big-enough-enabled")
            .description("Uses KB instead of bytes if your item's size is larger or equal to 1KB.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Mode> mode = sgUseKbIfBigEnough.add(new EnumSetting.Builder<Mode>()
            .name("mode")
            .description("Uses the standard mode (1KB to 1000b) OR true mode (1KB to 1024b).")
            .defaultValue(Mode.True)
            .build()
    );

    public ItemByteSize() {
        super(Categories.Render, "item-byte-size", "Displays an item's size in bytes in the tooltip.");
    }

    @EventHandler
    private void onGetTooltip(GetTooltipEvent event) {
        try {
            event.itemStack.toTag(new CompoundTag()).write(ByteCountDataOutput.INSTANCE);
            int byteCount = ByteCountDataOutput.INSTANCE.getCount();
            ByteCountDataOutput.INSTANCE.reset();

            event.list.add(new LiteralText(Formatting.GRAY + Modules.get().get(ItemByteSize.class).bytesToString(byteCount)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getKbSize() {
        return mode.get() == Mode.True ? 1024 : 1000;
    }

    public String bytesToString(int count) {
        if (useKbIfBigEnoughEnabled.get() && count >= getKbSize()) return String.format("%.2f kb", count / (float) getKbSize());
        return String.format("%d bytes", count);
    }
}
