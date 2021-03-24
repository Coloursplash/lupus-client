package coloursplash.lupusclient;

import com.g00fy2.versioncompare.Version;

import coloursplash.lupusclient.gui.GuiConfig;
import coloursplash.lupusclient.rendering.Fonts;
import coloursplash.lupusclient.systems.System;
import coloursplash.lupusclient.systems.Systems;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.CompoundTag;

public class Config extends System<Config> {
    public final Version version = new Version("0.0.1");
    public String devBuild;
    private String prefix = ".";
    public GuiConfig guiConfig = new GuiConfig();

    public boolean customFont = true;

    public boolean chatCommandsInfo = true;
    public boolean deleteChatCommandsInfo = true;

    public boolean sendDataToApi = true;

    public int rotationHoldTicks = 9;

    public Config() {
        super("config");

        devBuild = FabricLoader.getInstance().getModContainer("lupus-client").get().getMetadata().getCustomValue("lupus-client:devbuild").getAsString();
    }

    public static Config get() {
        return Systems.get(Config.class);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        save();
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        tag.putString("version", version.getOriginalString());
        tag.putString("prefix", prefix);
        tag.putBoolean("customFont", customFont);
        tag.put("guiConfig", guiConfig.toTag());
        tag.putBoolean("chatCommandsInfo", chatCommandsInfo);
        tag.putBoolean("deleteChatCommandsInfo", deleteChatCommandsInfo);
        tag.putBoolean("sendDataToApi", sendDataToApi);

        return tag;
    }

    @Override
    public Config fromTag(CompoundTag tag) {
        prefix = tag.getString("prefix");
        guiConfig.fromTag(tag.getCompound("guiConfig"));
        if (tag.contains("customFont")) customFont = tag.getBoolean("customFont");
        chatCommandsInfo = !tag.contains("chatCommandsInfo") || tag.getBoolean("chatCommandsInfo");
        deleteChatCommandsInfo = !tag.contains("deleteChatCommandsInfo") || tag.getBoolean("deleteChatCommandsInfo");
        sendDataToApi = !tag.contains("sendDataToApi") || tag.getBoolean("sendDataToApi");

        // In 0.2.9 the default font was changed, detect when people load up 0.2.9 for the first time
        Version lastVer = new Version(tag.getString("version"));
        Version v029 = new Version("0.2.9");

        if (lastVer.isLowerThan(v029) && version.isAtLeast(v029)) {
            Fonts.reset();
        }

        return this;
    }
}
