/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixin;

import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.render.NoRender;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(SignBlockEntityRenderer.class)
public class SignBlockEntityRendererMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/SignBlockEntity;getTextBeingEditedOnRow(ILjava/util/function/Function;)Lnet/minecraft/text/OrderedText;"))
    private OrderedText onRenderSignBlockEntityGetTextBeingEditedOnRowProxy(SignBlockEntity sign, int row, Function<Text, OrderedText> function) {
        if (Modules.get().get(NoRender.class).noSignText()) return null;
        return sign.getTextBeingEditedOnRow(row, function);
    }
}
