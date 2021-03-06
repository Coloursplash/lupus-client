/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.utils.player;

import coloursplash.lupusclient.Config;
import coloursplash.lupusclient.mixin.ChatHudAccessor;
import coloursplash.lupusclient.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChatUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    private enum PrefixType {
        Module(Formatting.AQUA),
        Other(Formatting.LIGHT_PURPLE),
        None(Formatting.RESET);

        public Formatting color;

        PrefixType(Formatting color) {
            this.color = color;
        }
    }

    //No prefix

    private static void message(int id,  Formatting color, String msg, Object... args) {
        sendMsg(id, null, PrefixType.None, formatMsg(msg, color, args), color);
    }

    public static void info(int id, String msg, Object... args) {
        message(id, Formatting.GRAY, msg, args);
    }

    public static void info(String msg, Object... args) {
        message(0, Formatting.GRAY, msg, args);
    }

    public static void info(String prefix, Text msg) {
        sendMsg(0, prefix, PrefixType.Other, msg);
    }

    public static void warning(String msg, Object... args) {
        message(0, Formatting.YELLOW, msg, args);
    }

    public static void error(String msg, Object... args) {
        message(0, Formatting.RED, msg, args);
    }

    //Custom Prefix

    private static void prefixMessage(Formatting color, String prefix, String msg, Object... args) {
        sendMsg(0, prefix, PrefixType.Other, formatMsg(msg, color, args), color);
    }

    public static void prefixInfo(String prefix, String msg, Object... args) {
        prefixMessage(Formatting.GRAY, prefix, msg, args);
    }

    public static void prefixWarning(String prefix, String msg, Object... args) {
        prefixMessage(Formatting.YELLOW, prefix, msg, args);
    }

    public static void prefixError(String prefix, String msg, Object... args) {
        prefixMessage(Formatting.RED, prefix, msg, args);
    }

    //Module

    private static void moduleMessage(Formatting color, Module module, String msg, Object... args) {
        sendMsg(0, module.title, PrefixType.Module, formatMsg(msg, color, args), color);
    }

    public static void moduleInfo(Module module, String msg, Object... args) {
        moduleMessage(Formatting.GRAY, module, msg, args);
    }

    public static void moduleWarning(Module module, String msg, Object... args) {
        moduleMessage(Formatting.YELLOW, module, msg, args);
    }

    public static void moduleError(Module module, String msg, Object... args) {
        moduleMessage(Formatting.RED, module, msg, args);
    }

    private static void sendMsg(int id, String prefix, PrefixType type, String msg, Formatting color) {

        String formatted = msg.replaceAll("\\(default\\)", color.toString()).replaceAll("\\(highlight\\)", Formatting.WHITE.toString());

        BaseText message = new LiteralText(formatted);
        message.setStyle(message.getStyle().withFormatting(color));

        sendMsg(id, prefix, type, message);
    }


    private static void sendMsg(int id, String prefix, PrefixType type, Text msg) {
        if (mc.world == null) return;

        BaseText message = new LiteralText("");
        message.append(getPrefix(prefix, type));
        message.append(msg);

        if (!Config.get().deleteChatCommandsInfo) id = 0;

        ((ChatHudAccessor) mc.inGameHud.getChatHud()).add(message, id);
    }

    private static BaseText getPrefix(String title, PrefixType type) {
        BaseText lupus = new LiteralText("Lupus");
        lupus.setStyle(lupus.getStyle().withFormatting(Formatting.BLUE));

        BaseText prefix = new LiteralText("");
        prefix.setStyle(prefix.getStyle().withFormatting(Formatting.GRAY));
        prefix.append("[");
        prefix.append(lupus);
        prefix.append("] ");

        if (type != PrefixType.None) {
            BaseText moduleTitle = new LiteralText(title);
            moduleTitle.setStyle(moduleTitle.getStyle().withFormatting(type.color));
            prefix.append("[");
            prefix.append(moduleTitle);
            prefix.append("] ");
        }

        return prefix;
    }

    private static String formatMsg(String format, Formatting defaultColor, Object... args) {
        String msg = String.format(format, args);
        msg = msg.replaceAll("\\(default\\)", defaultColor.toString());
        msg = msg.replaceAll("\\(highlight\\)", Formatting.WHITE.toString());

        return msg;
    }
}
