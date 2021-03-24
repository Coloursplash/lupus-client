package coloursplash.lupusclient.commands.commands;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import coloursplash.lupusclient.commands.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandSource;

public class Teleport extends Command {

    public Teleport() {
        super("tp", "Teleports the player, even in survival");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("x", DoubleArgumentType.doubleArg())
            .then(argument("y", DoubleArgumentType.doubleArg())
                .then(argument("z", DoubleArgumentType.doubleArg()).executes(context -> {
                    ClientPlayerEntity player = MinecraftClient.getInstance().player;
                    assert player != null;

                    double x = context.getArgument("x", Double.class);
                    double y = context.getArgument("y", Double.class);
                    double z = context.getArgument("z", Double.class);
                    player.updatePosition(x, y, z);

                    return SINGLE_SUCCESS;
                }))));

        builder.then(argument("x", DoubleArgumentType.doubleArg())
            .then(argument("z", DoubleArgumentType.doubleArg()).executes(context -> {
                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                assert player != null;

                double x = context.getArgument("x", Double.class);
                double z = context.getArgument("z", Double.class);
                player.updatePosition(x, player.getY(), z);

                return SINGLE_SUCCESS;
            })));
    }
    
}