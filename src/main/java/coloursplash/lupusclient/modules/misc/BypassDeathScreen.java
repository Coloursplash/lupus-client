

package coloursplash.lupusclient.modules.misc;

//Created by squidoodly 27/05/2020

import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;

public class BypassDeathScreen extends Module {
    public boolean shouldBypass = false;

    public BypassDeathScreen(){
        super(Categories.Misc, "bypass-death-screen", "Lets you spy on people after death.");
    }

    @Override
    public void onDeactivate() {
        shouldBypass = false;
        super.onDeactivate();
    }
}
