package coloursplash.lupusclient.modules.fun;

import meteordevelopment.orbit.EventHandler;
import coloursplash.lupusclient.events.world.TickEvent;
import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;
import coloursplash.lupusclient.settings.IntSetting;
import coloursplash.lupusclient.settings.Setting;
import coloursplash.lupusclient.settings.SettingGroup;

public class Sex extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
            .name("delay")
            .description("The delay between sending messages in seconds.")
            .defaultValue(2)
            .min(0)
            .sliderMax(20)
            .build()
    );

    private int timer;

    public Sex() {
        super(Categories.Fun, "Sex", "Spams sex in chat.");
    }
    
    @Override
    public void onActivate() {
        timer = delay.get() * 20;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (timer <= 0) {
            mc.player.sendChatMessage("sex");
            timer = delay.get() * 20;
        } else {
            timer -= 1;
        }
    }
}
