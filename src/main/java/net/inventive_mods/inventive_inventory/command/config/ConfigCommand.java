package net.inventive_mods.inventive_inventory.command.config;

import com.mojang.brigadier.CommandDispatcher;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.gui.ConfigScreen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ConfigCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(InventiveInventory.MOD_ID)
                .then(Commands.literal("config")
                        .executes(context -> {
                            InventiveInventory.getMinecraft().setScreen(new ConfigScreen(null));
                            return 1;
                        })
                )
        );
    }
}
