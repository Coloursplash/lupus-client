/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixin;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.platform.GlStateManager;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.render.HandView;
import coloursplash.lupusclient.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @ModifyVariable(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "STORE", ordinal = 0), index = 6)
    private float modifySwing(float swingProgress) {
        HandView module = Modules.get().get(HandView.class);
        MinecraftClient mc = Utils.mc;
        Hand hand = MoreObjects.firstNonNull(mc.player.preferredHand, Hand.MAIN_HAND);

        if (module.isActive()) {
            if (hand == Hand.OFF_HAND && !mc.player.getOffHandStack().isEmpty()) {
                if (!module.noSwing.get()) {
                    if (module.offSwing.get() != 1 && module.offSwing.get() != 0) return module.offSwing.get().floatValue();
                } else return module.offSwing.get().floatValue();
            }
            if (hand == Hand.MAIN_HAND && !mc.player.getMainHandStack().isEmpty()) {
                if (!module.noSwing.get()) {
                    if (module.mainSwing.get() != 1 && module.mainSwing.get() != 0) return module.mainSwing.get().floatValue();
                } else return module.mainSwing.get().floatValue();
            }
        }
        return swingProgress;
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "TAIL", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void sex(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        HandView module = Modules.get().get(HandView.class);
        if (!module.isActive()) return;
        GlStateManager.scaled(module.scaleX.get(), module.scaleY.get(), module.scaleZ.get());
        GlStateManager.translated(module.posX.get(), module.posY.get(), module.posZ.get());
        GlStateManager.rotatef((module.rotationY.get().floatValue() * 360.0f), 1.0f, 0.0f, 0.0f);
        GlStateManager.rotatef(-(module.rotationX.get().floatValue() * 360.0f), 0.0f, 1.0f, 0.0f);
        GlStateManager.rotatef((module.rotationZ.get().floatValue() * 360.0f), 0.0f, 0.0f, 1.0f);
    }
}
