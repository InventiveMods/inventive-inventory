package net.inventive_mods.inventive_inventory.command.profile;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.feature.profile.Profile;
import net.inventive_mods.inventive_inventory.feature.profile.ProfileHandler;
import net.inventive_mods.inventive_inventory.util.Notifier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

public class ProfilesDeleteCommand {
    private final static String ERROR_TRANSLATION_KEY = "error.profiles.inventive_inventory.";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(InventiveInventory.MOD_ID)
                .then(Commands.literal("profiles")
                        .then(Commands.literal("delete")
                                .then(Commands.argument("profile", StringArgumentType.greedyString())
                                        .suggests(ProfilesDeleteCommand::getProfiles)
                                        .executes(ProfilesDeleteCommand::delete)
                                )
                        )
                )
        );
    }

    private static int delete(CommandContext<CommandSourceStack> context) {
        String profileArg = StringArgumentType.getString(context, "profile");
        for (Profile profile : ProfileHandler.getProfiles()) {
            if (!profileArg.isEmpty() && profile.getName().equals(profileArg)) {
                ProfileHandler.delete(profile);
                return 1;
            }
        }
        Notifier.error(Component.translatable(ERROR_TRANSLATION_KEY + "does_not_exist").getString());
        return -1;
    }

    private static CompletableFuture<Suggestions> getProfiles(CommandContext<CommandSourceStack> ignoredContext, SuggestionsBuilder builder) {
        ProfileHandler.getProfiles().forEach(profile -> {
            if (!profile.getName().isEmpty()) builder.suggest(profile.getName());
        });
        return builder.buildFuture();
    }
}
