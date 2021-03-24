

package coloursplash.lupusclient.modules.render;

import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.ColorSetting;
import coloursplash.lupusclient.settings.ItemListSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;
import coloursplash.lupusclient.utils.render.color.SettingColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemHighlight extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<Item>> items = sgGeneral.add(new ItemListSetting.Builder()
            .name("items")
            .description("Items to highlight.")
            .defaultValue(new ArrayList<>())
            .build()
    );

    private final Setting<SettingColor> color = sgGeneral.add(new ColorSetting.Builder()
            .name("color")
            .description("The color to highlight the items with.")
            .defaultValue(new SettingColor(225, 25, 255, 50))
            .build()
    );

    public ItemHighlight() {
        super(Categories.Render, "item-highlight", "Highlights selected items when in guis");
    }

    public int getColor(ItemStack stack) {
        if (items.get().contains(stack.getItem()) && isActive()) return color.get().getPacked();
        return -1;
    }
}
