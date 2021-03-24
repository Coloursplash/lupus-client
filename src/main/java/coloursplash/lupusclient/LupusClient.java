/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient;

import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.IEventBus;
import coloursplash.lupusclient.events.game.GameLeftEvent;
import coloursplash.lupusclient.events.lupus.ClientInitialisedEvent;
import coloursplash.lupusclient.events.lupus.KeyEvent;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.gui.WidgetScreen;
import coloursplash.lupusclient.gui.screens.topbar.TopBarHud;
import coloursplash.lupusclient.gui.screens.topbar.TopBarModules;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Modules;
import coloursplash.lupusclient.modules.misc.DiscordPresence;
import coloursplash.lupusclient.rendering.Fonts;
import coloursplash.lupusclient.rendering.Matrices;
import coloursplash.lupusclient.rendering.text.CustomTextRenderer;
import coloursplash.lupusclient.systems.Systems;
import coloursplash.lupusclient.utils.Utils;
import coloursplash.lupusclient.utils.entity.EntityUtils;
import coloursplash.lupusclient.utils.misc.FakeClientPlayer;
import coloursplash.lupusclient.utils.misc.MeteorPlayers;
import coloursplash.lupusclient.utils.misc.Names;
import coloursplash.lupusclient.utils.misc.input.KeyAction;
import coloursplash.lupusclient.utils.misc.input.KeyBinds;
import coloursplash.lupusclient.utils.network.Capes;
import coloursplash.lupusclient.utils.network.MeteorExecutor;
import coloursplash.lupusclient.utils.network.OnlinePlayers;
import coloursplash.lupusclient.utils.player.EChestMemory;
import coloursplash.lupusclient.utils.player.Rotations;
import coloursplash.lupusclient.utils.render.color.RainbowColors;
import coloursplash.lupusclient.utils.world.BlockIterator;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LupusClient implements ClientModInitializer {
    public static LupusClient INSTANCE;
    public static final IEventBus EVENT_BUS = new EventBus();
    public static final File FOLDER = new File(FabricLoader.getInstance().getGameDir().toString(), "lupus-client");
    public static final Logger LOG = LogManager.getLogger();

    public static CustomTextRenderer FONT;

    private MinecraftClient mc;

    public Screen screenToOpen;

    @Override
    public void onInitializeClient() {
        if (INSTANCE == null) {
            KeyBinds.Register();

            INSTANCE = this;
            return;
        }

        LOG.info("Initializing Lupus Client");

        List<LupusAddon> addons = new ArrayList<>();
        for (EntrypointContainer<LupusAddon> entrypoint : FabricLoader.getInstance().getEntrypointContainers("lupus", LupusAddon.class)) {
            addons.add(entrypoint.getEntrypoint());
        }

        mc = MinecraftClient.getInstance();
        Utils.mc = mc;
        EntityUtils.mc = mc;

        Systems.addPreLoadTask(() -> {
            if (!Modules.get().getFile().exists()) {
                Modules.get().get(DiscordPresence.class).toggle(false);
            }
        });

        Matrices.begin(new MatrixStack());
        Fonts.init();
        MeteorExecutor.init();
        Capes.init();
        RainbowColors.init();
        BlockIterator.init();
        EChestMemory.init();
        Rotations.init();
        Names.init();
        MeteorPlayers.init();
        FakeClientPlayer.init();

        // Register categories
        Modules.REGISTERING_CATEGORIES = true;
        Categories.register();
        addons.forEach(LupusAddon::onRegisterCategories);
        Modules.REGISTERING_CATEGORIES = false;

        Systems.init();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Systems.save();
            OnlinePlayers.leave();
        }));

        EVENT_BUS.subscribe(this);
        EVENT_BUS.post(new ClientInitialisedEvent()); // TODO: This is there just for compatibility

        // Call onInitialize for addons
        addons.forEach(LupusAddon::onInitialize);

        Modules.get().sortModules();
        Systems.load();
    }

    private void openClickGui() {
        mc.openScreen(new TopBarModules());
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        Systems.save();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        Capes.tick();

        if (screenToOpen != null && mc.currentScreen == null) {
            mc.openScreen(screenToOpen);
            screenToOpen = null;
        }

        if (Utils.canUpdate()) {
            mc.player.getActiveStatusEffects().values().removeIf(statusEffectInstance -> statusEffectInstance.getDuration() <= 0);
        }
    }

    @EventHandler
    private void onKey(KeyEvent event) {
        // Click GUI
        if (event.action == KeyAction.Press && event.key == KeyBindingHelper.getBoundKeyOf(KeyBinds.OPEN_CLICK_GUI).getCode()) {
            if ((!Utils.canUpdate() && !(mc.currentScreen instanceof WidgetScreen) && !(mc.currentScreen instanceof TopBarHud)) || mc.currentScreen == null) openClickGui();
        }

        // Shulker Peek
        KeyBinding shulkerPeek = KeyBinds.SHULKER_PEEK;
        shulkerPeek.setPressed(shulkerPeek.matchesKey(event.key, 0) && event.action != KeyAction.Release);
    }
}
