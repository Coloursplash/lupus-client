

package coloursplash.lupusclient.modules.misc;

import coloursplash.lupusclient.modules.Categories;
import coloursplash.lupusclient.modules.Module;

public class AntiPacketKick extends Module {
    public AntiPacketKick() {
        super(Categories.Misc, "anti-packet-kick", "Attempts to prevent you from being disconnected by large packets.");
    }
}
