/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.commands.commands;

//Created by squidoodly 27/05/2020

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.utils.Utils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.ItemEnchantmentArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class Enchant extends Command {
    private final static SimpleCommandExceptionType NOT_IN_CREATIVE = new SimpleCommandExceptionType(new LiteralText("You must be in creative mode to use this."));

    public Enchant() {
        super("enchant", "Enchants the item in your hand. REQUIRES Creative mode.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("one").then(argument("enchantment", ItemEnchantmentArgumentType.itemEnchantment()).then(argument("level", IntegerArgumentType.integer()).executes(context -> {
            if (!mc.player.isCreative()) throw NOT_IN_CREATIVE.create();

            ItemStack itemStack = getItemStack();
            if (itemStack != null) {
                Enchantment enchantment = context.getArgument("enchantment", Enchantment.class);
                int level = context.getArgument("level", Integer.class);

                Utils.addEnchantment(itemStack, enchantment, level);
            }

            return SINGLE_SUCCESS;
        }))));

        builder.then(literal("all_possible").then(argument("level", IntegerArgumentType.integer()).executes(context -> {
            if (!mc.player.isCreative()) throw NOT_IN_CREATIVE.create();

            ItemStack itemStack = getItemStack();
            if (itemStack != null) {
                int level = context.getArgument("level", Integer.class);

                for (Enchantment enchantment : Registry.ENCHANTMENT) {
                    if (enchantment.isAcceptableItem(itemStack)) {
                        Utils.addEnchantment(itemStack, enchantment, level);
                    }
                }
            }

            return SINGLE_SUCCESS;
        })));

        builder.then(literal("all").then(argument("level", IntegerArgumentType.integer()).executes(context -> {
            if (!mc.player.isCreative()) throw NOT_IN_CREATIVE.create();

            ItemStack itemStack = getItemStack();
            if (itemStack != null) {
                int level = context.getArgument("level", Integer.class);

                for (Enchantment enchantment : Registry.ENCHANTMENT) {
                    Utils.addEnchantment(itemStack, enchantment, level);
                }
            }

            return SINGLE_SUCCESS;
        })));
    }

    private ItemStack getItemStack() {
        ItemStack itemStack = mc.player.getMainHandStack();
        if (itemStack == null) itemStack = mc.player.getOffHandStack();
        return itemStack;
    }
}
