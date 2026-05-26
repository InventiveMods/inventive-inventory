package net.inventive_mods.client.commands.profiles;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.features.profiles.ProfileHandler;
import net.inventive_mods.client.util.Notifier;
import net.minecraft.client.KeyMapping;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

public class ProfilesCreateCommand {
    private final static String ERROR_TRANSLATION_KEY = "error.profiles." + InventiveInventoryClient.MOD_ID + ".";

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext ignored) {
        dispatcher.register(ClientCommands.literal(InventiveInventoryClient.MOD_ID)
                .then(ClientCommands.literal("profiles")
                        .then(ClientCommands.literal("create")
                                .then(ClientCommands.argument("name", StringArgumentType.string())
                                        .executes(ProfilesCreateCommand::create)
                                        .then(ClientCommands.argument("keybinding", StringArgumentType.word())
                                                .suggests(ProfilesCreateCommand::getKeyBinds)
                                                .executes(ProfilesCreateCommand::createWithKeyBinding)
                                        )
                                )
                        )
                )
        );
    }

    private static int create(CommandContext<FabricClientCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        if (!name.isEmpty() && ProfileHandler.isNoProfile(name)) {
            ProfileHandler.create(name, "");
            return 1;
        }
        Notifier.error(Component.translatable(ERROR_TRANSLATION_KEY + "exclusive_name").getString());
        return -1;
    }

    private static int createWithKeyBinding(CommandContext<FabricClientCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        String keyBinding = StringArgumentType.getString(context, "keybinding");
        if (!name.isEmpty() && ProfileHandler.isNoProfile(name)) {
            for (KeyMapping key : ProfileHandler.getAvailableProfileKeys()) {
                if (key.getTranslatedKeyMessage().getString().equals(keyBinding)) {
                    ProfileHandler.create(name, key.getName());
                    return 1;
                }
            }
            Notifier.error(Component.translatable(ERROR_TRANSLATION_KEY + "key").getString());
            return -1;
        }
        Notifier.error(Component.translatable(ERROR_TRANSLATION_KEY + "exclusive_name").getString());
        return -1;
    }

    private static CompletableFuture<Suggestions> getKeyBinds(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        ProfileHandler.getAvailableProfileKeys().forEach(keyBinding -> builder.suggest(keyBinding.getTranslatedKeyMessage().getString()));
        return builder.buildFuture();
    }
}
