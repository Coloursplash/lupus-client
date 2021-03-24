

package coloursplash.lupusclient.modules.render;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.render.RenderEvent;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.rendering.Renderer;
import coloursplash.lupusclient.rendering.ShapeMode;
import coloursplash.lupusclient.settings.*;
import coloursplash.lupusclient.utils.Utils;
import coloursplash.lupusclient.utils.render.color.SettingColor;
import coloursplash.lupusclient.utils.world.Dimension;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class VoidESP extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    // General

    private final Setting<Boolean> airOnly = sgGeneral.add(new BoolSetting.Builder()
            .name("air-only")
            .description("Checks bedrock only for air blocks.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Integer> horizontalRadius = sgGeneral.add(new IntSetting.Builder()
            .name("horizontal-radius")
            .description("Horizontal radius in which to search for holes.")
            .defaultValue(64)
            .min(0)
            .sliderMax(256)
            .build()
    );

    private final Setting<Integer> holeHeight = sgGeneral.add(new IntSetting.Builder()
            .name("hole-height")
            .description("The minimum hole height to be rendered.")
            .defaultValue(1)  // If we already have one hole in the bedrock layer, there is already something interesting.
            .min(1)
            .sliderMax(5)     // There is no sense to check more than 5.
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
            .description("The color that fills holes in the void.")
            .defaultValue(new SettingColor(225, 25, 25))
            .build()
    );

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
            .name("line-color")
            .description("The color to draw lines of holes to the void.")
            .defaultValue(new SettingColor(225, 25, 255))
            .build()
    );

    private final List<BlockPos> voidHoles = new ArrayList<>();

    public VoidESP() {
        super(Categories.Render, "void-esp", "Renders holes in bedrock layers that lead to the void.");
    }

    private void getHoles(int searchRange, int holeHeight) {
        voidHoles.clear();
        if (Utils.getDimension() == Dimension.End) return;

        BlockPos playerPos = mc.player.getBlockPos();
        int playerY = playerPos.getY();

        for (int x = -searchRange; x < searchRange; ++x) {
            for (int z = -searchRange; z < searchRange; ++z) {
                BlockPos bottomBlockPos = playerPos.add(x, -playerY, z);

                int blocksFromBottom = 0;
                for (int i = 0; i < holeHeight; ++i)
                    if (isBlockMatching(mc.world.getBlockState(bottomBlockPos.add(0, i, 0)).getBlock()))
                        ++blocksFromBottom;

                if (blocksFromBottom >= holeHeight) voidHoles.add(bottomBlockPos);

                // checking nether roof
                if (Utils.getDimension() == Dimension.Nether) {
                    BlockPos topBlockPos = playerPos.add(x, 127 - playerY, z);

                    int blocksFromTop = 0;
                    for (int i = 0; i < holeHeight; ++i)
                        if (isBlockMatching(mc.world.getBlockState(bottomBlockPos.add(0, 127 - i, 0)).getBlock()))
                            ++blocksFromTop;

                    if (blocksFromTop >= holeHeight) voidHoles.add(topBlockPos);
                }
            }
        }
    }

    private boolean isBlockMatching(Block block) {
        if (airOnly.get()) return block == Blocks.AIR;
        return block != Blocks.BEDROCK;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        getHoles(horizontalRadius.get(), holeHeight.get());
    }

    @EventHandler
    private void onRender(RenderEvent event) {
        for (BlockPos voidHole : voidHoles) {
            Renderer.boxWithLines(Renderer.NORMAL, Renderer.LINES, voidHole, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
        }
    }
}
