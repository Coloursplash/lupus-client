

package coloursplash.lupusclient.modules.render.hud.modules;

import coloursplash.lupusclient.gui.screens.HudElementScreen;
import coloursplash.lupusclient.gui.screens.topbar.TopBarHud;
import coloursplash.lupusclient.modules.render.hud.BoundingBox;
import coloursplash.lupusclient.modules.render.hud.HUD;
import coloursplash.lupusclient.modules.render.hud.HudRenderer;
import coloursplash.lupusclient.settings.Settings;
import coloursplash.lupusclient.utils.Utils;
import coloursplash.lupusclient.utils.misc.ISerializable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;

public abstract class HudElement implements ISerializable<HudElement> {
    public final String name, title;
    public final String description;

    public boolean active = true;

    protected final HUD hud;

    public final Settings settings = new Settings();
    public final BoundingBox box = new BoundingBox();

    protected final MinecraftClient mc;

    public HudElement(HUD hud, String name, String description) {
        this.hud = hud;
        this.name = name;
        this.title = Utils.nameToTitle(name);
        this.description = description;
        this.mc = MinecraftClient.getInstance();
    }

    public void toggle() {
        active = !active;
    }

    public abstract void update(HudRenderer renderer);

    public abstract void render(HudRenderer renderer);

    protected boolean isInEditor() {
        return mc.currentScreen instanceof TopBarHud || mc.currentScreen instanceof HudElementScreen || !Utils.canUpdate();
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        tag.putString("name", name);
        tag.putBoolean("active", active);
        tag.put("settings", settings.toTag());
        tag.put("box", box.toTag());

        return tag;
    }

    @Override
    public HudElement fromTag(CompoundTag tag) {
        active = tag.getBoolean("active");
        if (tag.contains("settings")) settings.fromTag(tag.getCompound("settings"));
        box.fromTag(tag.getCompound("box"));

        return this;
    }
}
