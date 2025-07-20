package net.inventive_mods.inventive_inventory.command;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.command.config.ConfigCommand;
import net.inventive_mods.inventive_inventory.command.profile.ProfileCreateCommand;
import net.inventive_mods.inventive_inventory.command.profile.ProfilesDeleteCommand;
import net.inventive_mods.inventive_inventory.command.profile.ProfilesLoadCommand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = InventiveInventory.MOD_ID, value = Dist.CLIENT)
public class CommandHandler {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        ConfigCommand.register(event.getDispatcher());
        ProfileCreateCommand.register(event.getDispatcher());
        ProfilesLoadCommand.register(event.getDispatcher());
        ProfilesDeleteCommand.register(event.getDispatcher());
    }
}
