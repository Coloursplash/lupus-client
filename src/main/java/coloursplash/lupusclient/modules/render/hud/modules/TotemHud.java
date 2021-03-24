

package coloursplash.lupusclient.modules.render.hud.modules;

import coloursplash.lupusclient.modules.render.hud.HUD;
import coloursplash.lupusclient.modules.render.hud.HudRenderer;
import coloursplash.lupusclient.settings.DoubleSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.utils.player.InvUtils;
import coloursplash.lupusclient.utils.render.RenderUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class TotemHud extends HudElement {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> scale = sgGeneral.add(new DoubleSetting.Builder()
            .name("scale")
            .description("Scale of totem counter.")
            .defaultValue(2)
            .min(1)
            .sliderMin(1)
            .sliderMax(4)
            .build()
    );

    public TotemHud(HUD hud) {
        super(hud, "totems", "Displays the amount of totems in your inventory.");
    }

    @Override
    public void update(HudRenderer renderer) {
        box.setSize(16 * scale.get(), 16 * scale.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        double x = box.getX() / scale.get();
        double y = box.getY() / scale.get();

        if (isInEditor()) {
            RenderUtils.drawItem(Items.TOTEM_OF_UNDYING.getDefaultStack(), (int) x, (int) y, scale.get(), true);
        } else if (InvUtils.findItemWithCount(Items.TOTEM_OF_UNDYING).count > 0) {
            RenderUtils.drawItem(new ItemStack(Items.TOTEM_OF_UNDYING, InvUtils.findItemWithCount(Items.TOTEM_OF_UNDYING).count), (int) x, (int) y, scale.get(), true);
        }
    }
}
