

package coloursplash.lupusclient.modules.player;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.render.RenderEvent;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.rendering.Renderer;
import coloursplash.lupusclient.rendering.ShapeMode;
import coloursplash.lupusclient.settings.*;
import coloursplash.lupusclient.utils.render.color.SettingColor;
import coloursplash.lupusclient.utils.world.BlockUtils;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class AirPlace extends Module {
    public enum Place {
        OnClick,
        Always
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    // General

    private final Setting<Place> placeWhen = sgGeneral.add(new EnumSetting.Builder<Place>()
            .name("place-when")
            .description("Decides when it should place.")
            .defaultValue(Place.OnClick)
            .build()
    );

    // Render
    private final Setting<Boolean> render = sgRender.add(new BoolSetting.Builder()
            .name("render")
            .description("Renders a block overlay where the obsidian will be placed.")
            .defaultValue(true)
            .build()
    );

    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
            .name("shape-mode")
            .description("How the shapes are rendered.")
            .defaultValue(ShapeMode.Both)
            .build()
    );

    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder()
            .name("side-color")
            .description("The color of the sides of the blocks being rendered.")
            .defaultValue(new SettingColor(204, 0, 0, 10))
            .build()
    );

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
            .name("line-color")
            .description("The color of the lines of the blocks being rendered.")
            .defaultValue(new SettingColor(204, 0, 0, 255))
            .build()
    );

    private BlockPos target;

    public AirPlace() {
        super(Categories.Player, "air-place", "Places a block where your crosshair is pointing at.");
    }

    @Override
    public void onActivate() {
        target = mc.player.getBlockPos().add(4, 2, 0); //lol funni
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!(mc.crosshairTarget instanceof BlockHitResult) || !(mc.player.getMainHandStack().getItem() instanceof BlockItem)) return;

        target = ((BlockHitResult) mc.crosshairTarget).getBlockPos();

        if (!mc.world.getBlockState(target).isAir()) return;

        if (placeWhen.get() == Place.Always || placeWhen.get() == Place.OnClick && (mc.options.keyUse.wasPressed() || mc.options.keyUse.isPressed())) {
            BlockUtils.place(target, Hand.MAIN_HAND, 0, false, 0, true, true, false, false);
        }
    }

    @EventHandler
    private void onRender(RenderEvent event) {
        if (!(mc.crosshairTarget instanceof BlockHitResult)
                || !mc.world.getBlockState(target).isAir()
                || !(mc.player.getMainHandStack().getItem() instanceof BlockItem)
                || !render.get()) return;

        Renderer.boxWithLines(Renderer.NORMAL, Renderer.LINES, target, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
    }
}
