package coloursplash.lupusclient.modules.misc;

import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;

public class Panic extends Module {

    public Panic() {
        super(Categories.Misc, "Panic", "Turns off all modules when activated.");
    }
    
    @Override
    public void onActivate() {
        mc.player.sendChatMessage(".panic");
    }
}
