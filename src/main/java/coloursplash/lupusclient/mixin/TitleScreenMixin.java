/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixin;

import com.g00fy2.versioncompare.Version;
import coloursplash.lupusclient.Config;
import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.gui.screens.NewUpdateScreen;
import coloursplash.lupusclient.utils.Utils;
import coloursplash.lupusclient.utils.network.HttpUtils;
import coloursplash.lupusclient.utils.network.MeteorExecutor;
import coloursplash.lupusclient.utils.render.color.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    private final int WHITE = Color.fromRGBA(255, 255, 255, 255);
    private final int GRAY = Color.fromRGBA(175, 175, 175, 255);
    private final int BLUE = Color.fromRGBA(30, 144, 255, 255);

    private String text1;
    private int text1Length;

    private String text2;
    private int text2Length;

    private int fullLength;
    private int prevWidth;

    public TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo info) {

        text1 = "Lupus Client by ";
        text2 = "Coloursplash";

        text1Length = textRenderer.getWidth(text1);
        text2Length = textRenderer.getWidth(text2);

        fullLength = text1Length + text2Length;
        prevWidth = 0;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawStringWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V", ordinal = 0))
    private void onRenderIdkDude(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if (Utils.firstTimeTitleScreen) {
            Utils.firstTimeTitleScreen = false;
            LupusClient.LOG.info("Checking latest version of Lupus Client");

            // This code is commented out so that it in the future I can reuse it
            //MeteorExecutor.execute(() -> HttpUtils.getLines("http://meteorclient.com/api/version", s -> {
            //   Version latestVer = new Version(s);
            //    if (latestVer.isHigherThan(Config.get().version)) MinecraftClient.getInstance().openScreen(new NewUpdateScreen(latestVer));
            //}));
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
        prevWidth = 0;
        textRenderer.drawWithShadow(matrices, text1, width - fullLength - 3, 3, WHITE);
        prevWidth += text1Length;
        textRenderer.drawWithShadow(matrices, text2, width - fullLength + prevWidth - 3, 3, BLUE);
    }
}
