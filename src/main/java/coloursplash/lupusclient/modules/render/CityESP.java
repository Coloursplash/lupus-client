

package coloursplash.lupusclient.modules.render;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.render.RenderEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.rendering.Renderer;
import coloursplash.lupusclient.rendering.ShapeMode;
import coloursplash.lupusclient.settings.*;
import coloursplash.lupusclient.utils.player.CityUtils;
import coloursplash.lupusclient.utils.render.color.SettingColor;
import net.minecraft.util.math.BlockPos;

public class CityESP extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    // General

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
            .name("range")
            .description("The maximum range a city-able block will be found.")
            .defaultValue(5)
            .min(0)
            .sliderMax(20)
            .build()
    );

    // Render

    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
            .name("shape-mode")
            .description("How the shapes are rendered.")
            .defaultValue(ShapeMode.Both)
            .build()
    );

    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder()
            .name("fill-color")
            .description("The fill color the city block will render as.")
            .defaultValue(new SettingColor(225, 0, 0, 75))
            .build()
    );

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
            .name("outline-color")
            .description("The line color the city block will render as.")
            .defaultValue(new SettingColor(225, 0, 0, 255))
            .build()
    );

    public CityESP() {
        super(Categories.Render, "city-esp", "Displays blocks that can be broken in order to city another player.");
    }

    @EventHandler
    private void onRender(RenderEvent event) {
        BlockPos targetBlock = CityUtils.getTargetBlock(CityUtils.getPlayerTarget(range.get()));

        if (targetBlock == null) return;

        Renderer.boxWithLines(Renderer.NORMAL, Renderer.LINES, targetBlock, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
    }
}
