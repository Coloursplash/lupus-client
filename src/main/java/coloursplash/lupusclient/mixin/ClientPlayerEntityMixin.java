/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixin;

import baritone.api.BaritoneAPI;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import coloursplash.lupusclient.Config;
import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.commands.Commands;
import coloursplash.lupusclient.events.entity.player.SendMessageEvent;
import coloursplash.lupusclient.events.entity.player.SendMovementPacketsEvent;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.movement.NoSlow;
import coloursplash.lupusclient.modules.movement.Scaffold;
import coloursplash.lupusclient.modules.player.Portals;
import coloursplash.lupusclient.utils.player.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    @Shadow @Final public ClientPlayNetworkHandler networkHandler;

    @Shadow public abstract void sendChatMessage(String string);

    private boolean ignoreChatMessage;

    @Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
    private void onSendChatMessage(String msg, CallbackInfo info) {
        if (ignoreChatMessage) return;

        if (!msg.startsWith(Config.get().getPrefix()) && !msg.startsWith("/") && !msg.startsWith(BaritoneAPI.getSettings().prefix.value)) {
            SendMessageEvent event = LupusClient.EVENT_BUS.post(SendMessageEvent.get(msg));

            if (!event.isCancelled()) {
                ignoreChatMessage = true;
                sendChatMessage(event.msg);
                ignoreChatMessage = false;
            }

            info.cancel();
            return;
        }

        if (msg.startsWith(Config.get().getPrefix())) {
            try {
                Commands.get().dispatch(msg.substring(Config.get().getPrefix().length()));
            } catch (CommandSyntaxException e) {
                ChatUtils.error(e.getMessage());
            }
            info.cancel();
        }
    }

    @Redirect(method = "updateNausea", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;"))
    private Screen updateNauseaGetCurrentScreenProxy(MinecraftClient client) {
        if (Modules.get().isActive(Portals.class)) return null;
        return client.currentScreen;
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean proxy_tickMovement_isUsingItem(ClientPlayerEntity player) {
        if (Modules.get().get(NoSlow.class).items()) return false;
        return player.isUsingItem();
    }

    @Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
    private void onIsSneaking(CallbackInfoReturnable<Boolean> info) {
        if (Modules.get().isActive(Scaffold.class)) info.setReturnValue(false);
    }

    // Rotations

    @Inject(method = "sendMovementPackets", at = @At("HEAD"))
    private void onSendMovementPacketsHead(CallbackInfo info) {
        LupusClient.EVENT_BUS.post(SendMovementPacketsEvent.Pre.get());
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 0))
    private void onTickHasVehicleBeforeSendPackets(CallbackInfo info) {
        LupusClient.EVENT_BUS.post(SendMovementPacketsEvent.Pre.get());
    }

    @Inject(method = "sendMovementPackets", at = @At("TAIL"))
    private void onSendMovementPacketsTail(CallbackInfo info) {
        LupusClient.EVENT_BUS.post(SendMovementPacketsEvent.Post.get());
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void onTickHasVehicleAfterSendPackets(CallbackInfo info) {
        LupusClient.EVENT_BUS.post(SendMovementPacketsEvent.Post.get());
    }
}
