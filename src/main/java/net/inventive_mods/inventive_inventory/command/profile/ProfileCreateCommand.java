package net.inventive_mods.inventive_inventory.command.profile;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.features.profile.ProfileHandler;
import net.inventive_mods.inventive_inventory.util.Notifier;
import net.minecraft.client.KeyMapping;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

public class ProfileCreateCommand {
    private final static String ERROR_TRANSLATION_KEY = "error.profiles.inventive_inventory.";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(InventiveInventory.MOD_ID)
                .then(Commands.literal("profiles")
                        .then(Commands.literal("create")
                                .then(Commands.argument("name", StringArgumentType.string())
                                        .executes(ProfileCreateCommand::create)
                                        .then(Commands.argument("keybinding", StringArgumentType.word())
                                                .suggests(ProfileCreateCommand::getKeyBinds)
                                                .executes(ProfileCreateCommand::createWithKeyBinding)
                                        )
                                )
                        )
                )
        );
    }

    private static int create(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        if (!name.isEmpty() && ProfileHandler.isNoProfile(name)) {
            ProfileHandler.create(name, "");
            return 1;
        }
        Notifier.error(Component.translatable(ERROR_TRANSLATION_KEY + "exclusive_name").getString());
        return -1;
    }

    private static int createWithKeyBinding(CommandContext<CommandSourceStack> context) {
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

    private static CompletableFuture<Suggestions> getKeyBinds(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ProfileHandler.getAvailableProfileKeys().forEach(keyBinding -> {
            String keyString = keyBinding.getTranslatedKeyMessage().getString();
            builder.suggest(keyString);
        });
        return builder.buildFuture();
    }
}
