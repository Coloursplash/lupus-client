

package coloursplash.lupusclient.modules.render;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import net.minecraft.client.MinecraftClient;

public class Fullbright extends Module {
    public Fullbright() {
        super(Categories.Render, "fullbright", "Lights up your world!");

        LupusClient.EVENT_BUS.subscribe(StaticListener.class);
    }

    @Override
    public void onActivate() {
        enable();
    }

    @Override
    public void onDeactivate() {
        disable();
    }

    public static void enable() {
        StaticListener.timesEnabled++;
    }

    public static void disable() {
        StaticListener.timesEnabled--;
    }

    private static class StaticListener {
        private static final MinecraftClient mc = MinecraftClient.getInstance();

        private static int timesEnabled;
        private static int lastTimesEnabled;

        private static double prevGamma;

        @EventHandler
        private static void onTick(TickEvent.Post event) {
            if (timesEnabled > 0 && lastTimesEnabled == 0) {
                prevGamma = mc.options.gamma;
            }
            else if (timesEnabled == 0 && lastTimesEnabled > 0) {
                mc.options.gamma = prevGamma;
            }

            if (timesEnabled > 0) {
                mc.options.gamma = 16;
            }

            lastTimesEnabled = timesEnabled;
        }
    }
}
