package net.inventive_mods.client.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.inventive_mods.client.commands.config.ConfigCommand;
import net.inventive_mods.client.commands.profiles.ProfilesCreateCommand;
import net.inventive_mods.client.commands.profiles.ProfilesDeleteCommand;
import net.inventive_mods.client.commands.profiles.ProfilesLoadCommand;

public class CommandRegistry {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register(ConfigCommand::register);
        registerProfileCommands();
    }

    private static void registerProfileCommands() {
        ClientCommandRegistrationCallback.EVENT.register(ProfilesCreateCommand::register);
        ClientCommandRegistrationCallback.EVENT.register(ProfilesLoadCommand::register);
        ClientCommandRegistrationCallback.EVENT.register(ProfilesDeleteCommand::register);
    }
}
