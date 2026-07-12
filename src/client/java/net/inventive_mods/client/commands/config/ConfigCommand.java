package net.inventive_mods.client.commands.config;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.screens.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;

public class ConfigCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext ignored) {
        dispatcher.register(ClientCommands.literal(InventiveInventoryClient.MOD_ID)
                .then(ClientCommands.literal("config")
                        .executes(context -> {
                            Minecraft client = context.getSource().getClient();
                            client.schedule(() -> client.gui.setScreen(new ConfigScreen(null)));
                            return 1;
                        }))
        );
    }
}
