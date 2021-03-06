/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixin;

import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.events.entity.EntityDestroyEvent;
import coloursplash.lupusclient.events.entity.player.PickItemsEvent;
import coloursplash.lupusclient.events.game.GameJoinedEvent;
import coloursplash.lupusclient.events.game.GameLeftEvent;
import coloursplash.lupusclient.events.packets.ContainerSlotUpdateEvent;
import coloursplash.lupusclient.events.packets.PacketEvent;
import coloursplash.lupusclient.events.packets.PlaySoundPacketEvent;
import coloursplash.lupusclient.events.world.ChunkDataEvent;
import coloursplash.lupusclient.mixininterface.IExplosionS2CPacket;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.movement.Velocity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Shadow private MinecraftClient client;

    @Shadow private ClientWorld world;

    private boolean worldNotNull;

    @Inject(at = @At("HEAD"), method = "onGameJoin")
    private void onGameJoinHead(GameJoinS2CPacket packet, CallbackInfo info) {
        worldNotNull = world != null;
    }

    @Inject(at = @At("TAIL"), method = "onGameJoin")
    private void onGameJoinTail(GameJoinS2CPacket packet, CallbackInfo info) {
        if (worldNotNull) {
            LupusClient.EVENT_BUS.post(GameLeftEvent.get());
        }

        LupusClient.EVENT_BUS.post(GameJoinedEvent.get());
    }

    @Inject(at = @At("HEAD"), method = "sendPacket", cancellable = true)
    private void onSendPacketHead(Packet<?> packet, CallbackInfo info) {
        PacketEvent.Send event = LupusClient.EVENT_BUS.post(PacketEvent.Send.get(packet));

        if (event.isCancelled()) info.cancel();
    }

    @Inject(method = "sendPacket", at = @At("TAIL"))
    private void onSendPacketTail(Packet<?> packet, CallbackInfo info) {
        LupusClient.EVENT_BUS.post(PacketEvent.Sent.get(packet));
    }

    @Inject(at = @At("HEAD"), method = "onPlaySound")
    private void onPlaySound(PlaySoundS2CPacket packet, CallbackInfo info) {
        LupusClient.EVENT_BUS.post(PlaySoundPacketEvent.get(packet));
    }

    @Inject(method = "onChunkData", at = @At("TAIL"))
    private void onChunkData(ChunkDataS2CPacket packet, CallbackInfo info) {
        WorldChunk chunk = client.world.getChunk(packet.getX(), packet.getZ());
        LupusClient.EVENT_BUS.post(ChunkDataEvent.get(chunk));
    }

    @Inject(method = "onScreenHandlerSlotUpdate", at = @At("TAIL"))
    private void onContainerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet, CallbackInfo info) {
        LupusClient.EVENT_BUS.post(ContainerSlotUpdateEvent.get(packet));
    }

    @Inject(method = "onEntitiesDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;removeEntity(I)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onEntityDestroy(EntitiesDestroyS2CPacket packet, CallbackInfo info, int i, int j) {
        LupusClient.EVENT_BUS.post(EntityDestroyEvent.get(client.world.getEntityById(j)));
    }

    @Inject(method = "onExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V", shift = At.Shift.AFTER))
    private void onExplosionVelocity(ExplosionS2CPacket packet, CallbackInfo ci) {
        Velocity velocity = Modules.get().get(Velocity.class); //Velocity for explosions
        if (!velocity.explosions.get()) return;

        ((IExplosionS2CPacket) packet).setVelocityX((float) (packet.getPlayerVelocityX() * velocity.getHorizontal()));
        ((IExplosionS2CPacket) packet).setVelocityY((float) (packet.getPlayerVelocityY() * velocity.getVertical()));
        ((IExplosionS2CPacket) packet).setVelocityZ((float) (packet.getPlayerVelocityZ() * velocity.getHorizontal()));
    }

    @Inject(method = "onItemPickupAnimation", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getEntityById(I)Lnet/minecraft/entity/Entity;", ordinal = 0))
    private void onItemPickupAnimation(ItemPickupAnimationS2CPacket packet, CallbackInfo info) {
        Entity itemEntity = client.world.getEntityById(packet.getEntityId());
        Entity entity = client.world.getEntityById(packet.getCollectorEntityId());

        if (itemEntity instanceof ItemEntity && entity == client.player) {
            LupusClient.EVENT_BUS.post(PickItemsEvent.get(((ItemEntity) itemEntity).getStack(), packet.getStackAmount()));
        }
    }
}