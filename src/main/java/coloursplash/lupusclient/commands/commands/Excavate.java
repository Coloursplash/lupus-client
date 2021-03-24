package coloursplash.lupusclient.commands.commands;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import coloursplash.lupusclient.commands.Command;
import coloursplash.lupusclient.utils.player.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandSource;

public class Excavate extends Command {

    public Excavate() {
        super("excavate", "Uses baritone to dig out a given selection");
    }

    double x1;
    double y1;
    double z1;
    double x2;
    double y2;
    double z2;

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("x1", DoubleArgumentType.doubleArg())
            .then(argument("y1", DoubleArgumentType.doubleArg())
                .then(argument("z1", DoubleArgumentType.doubleArg())
                    .then(argument("x2", DoubleArgumentType.doubleArg())
                        .then(argument("y2", DoubleArgumentType.doubleArg())
                            .then(argument("z2", DoubleArgumentType.doubleArg()).executes(context -> {
                                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                assert player != null;
                                
                                x1 = context.getArgument("x1", Double.class);
                                y1 = context.getArgument("y1", Double.class);
                                z1 = context.getArgument("z1", Double.class);
                                x2 = context.getArgument("x2", Double.class);
                                y2 = context.getArgument("y2", Double.class);
                                z2 = context.getArgument("z2", Double.class);
                                
                                player.updatePosition(x1, y1, z1);
                                player.sendChatMessage("#sel  1");
                                player.updatePosition(x2, y2, z2);
                                player.sendChatMessage("#sel  2");
                                player.sendChatMessage("#sel cleararea");

                                return SINGLE_SUCCESS;
                            })))))));
    }
}