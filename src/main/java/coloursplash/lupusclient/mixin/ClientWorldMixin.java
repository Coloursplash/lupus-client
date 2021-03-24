/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.mixin;

import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.events.entity.EntityAddedEvent;
import coloursplash.lupusclient.events.entity.EntityRemovedEvent;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.render.Ambience;
import coloursplash.lupusclient.modules.render.Search;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Unique
    private final SkyProperties endSky = new SkyProperties.End();
    @Unique
    private final SkyProperties customSky = new Ambience.Custom();

    @Inject(method = "addEntityPrivate", at = @At("TAIL"))
    private void onAddEntityPrivate(int id, Entity entity, CallbackInfo info) {
        LupusClient.EVENT_BUS.post(EntityAddedEvent.get(entity));
    }

    @Inject(method = "finishRemovingEntity", at = @At("TAIL"))
    private void onFinishRemovingEntity(Entity entity, CallbackInfo info) {
        LupusClient.EVENT_BUS.post(EntityRemovedEvent.get(entity));
    }

    @Inject(method = "setBlockStateWithoutNeighborUpdates", at = @At("TAIL"))
    private void onSetBlockStateWithoutNeighborUpdates(BlockPos blockPos, BlockState blockState, CallbackInfo info) {
        Search search = Modules.get().get(Search.class);
        if (search.isActive()) search.onBlockUpdate(blockPos, blockState);
    }

    /**
     * @author Walaryne
     */
    @Inject(method = "method_23777", at = @At("HEAD"), cancellable = true)
    private void onGetSkyColor(BlockPos blockPos, float tickDelta, CallbackInfoReturnable<Vec3d> info) {
        Ambience ambience = Modules.get().get(Ambience.class);

        if (ambience.isActive() && ambience.changeSkyColor.get()) {
            info.setReturnValue(ambience.skyColor.get().getVec3d());
        }
    }

    /**
     * @author Walaryne
     */
    @Inject(method = "getSkyProperties", at = @At("HEAD"), cancellable = true)
    private void onGetSkyProperties(CallbackInfoReturnable<SkyProperties> info) {
        Ambience ambience = Modules.get().get(Ambience.class);

        if (ambience.enderMode.get()) {
            info.setReturnValue(ambience.enderCustomSkyColor.get() ? customSky : endSky);
        }
    }

    /**
     * @author Walaryne
     */
    @Inject(method = "getCloudsColor", at = @At("HEAD"), cancellable = true)
    private void onGetCloudsColor(float tickDelta, CallbackInfoReturnable<Vec3d> info) {
        Ambience ambience = Modules.get().get(Ambience.class);

        if (ambience.isActive() && ambience.changeCloudColor.get()) {
            info.setReturnValue(ambience.cloudColor.get().getVec3d());
        }
    }
}
