

package coloursplash.lupusclient.modules.render;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.render.RenderEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.rendering.Renderer;
import coloursplash.lupusclient.rendering.ShapeMode;
import coloursplash.lupusclient.settings.*;
import coloursplash.lupusclient.utils.render.color.SettingColor;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

public class BlockSelection extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> advanced = sgGeneral.add(new BoolSetting.Builder()
            .name("advanced")
            .description("Shows a more advanced outline on different types of shape blocks.")
            .defaultValue(true)
            .build()
    );

    private final Setting<ShapeMode> shapeMode = sgGeneral.add(new EnumSetting.Builder<ShapeMode>()
            .name("shape-mode")
            .description("How the shapes are rendered.")
            .defaultValue(ShapeMode.Lines)
            .build()
    );

    private final Setting<SettingColor> sideColor = sgGeneral.add(new ColorSetting.Builder()
            .name("side-color")
            .description("The side color.")
            .defaultValue(new SettingColor(255, 255, 255, 50))
            .build()
    );

    private final Setting<SettingColor> lineColor = sgGeneral.add(new ColorSetting.Builder()
            .name("line-color")
            .description("The line color.")
            .defaultValue(new SettingColor(255, 255, 255, 255))
            .build()
    );

    public BlockSelection() {
        super(Categories.Render, "block-selection", "Modifies how your block selection is rendered.");
    }

    @EventHandler
    private void onRender(RenderEvent event) {
        if (mc.crosshairTarget == null || !(mc.crosshairTarget instanceof BlockHitResult)) return;

        BlockPos pos = ((BlockHitResult) mc.crosshairTarget).getBlockPos();
        BlockState state = mc.world.getBlockState(pos);
        VoxelShape shape = state.getOutlineShape(mc.world, pos);

        if (shape.isEmpty()) return;
        Box box = shape.getBoundingBox();

        if (advanced.get()) {
            for (Box b : shape.getBoundingBoxes()) {
                render(pos, b);
            }
        } else {
            render(pos, box);
        }
    }

    private void render(BlockPos pos, Box box) {
        Renderer.boxWithLines(Renderer.NORMAL, Renderer.LINES, pos.getX() + box.minX, pos.getY() + box.minY, pos.getZ() + box.minZ, pos.getX() + box.maxX, pos.getY() + box.maxY, pos.getZ() + box.maxZ, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
    }
}
