package coloursplash.lupusclient.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.commands.arguments.PlayerArgumentType;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class Inventory extends Command {

    public Inventory() {
        super("inventory", "Allows you to see parts of another player's inventory.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("name", PlayerArgumentType.player()).executes(context -> {
            PlayerEntity playerEntity = context.getArgument("name", PlayerEntity.class);
            LupusClient.INSTANCE.screenToOpen = new InventoryScreen(playerEntity);
            return SINGLE_SUCCESS;
        }));

    }

}
