package net.inventive_mods.inventive_inventory.commands.profiles;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.features.profiles.Profile;
import net.inventive_mods.inventive_inventory.features.profiles.ProfileHandler;
import net.inventive_mods.inventive_inventory.util.Notifier;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;

public class ProfilesLoadCommand {
    private final static String ERROR_TRANSLATION_KEY = "error.profiles.inventive_inventory.";

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
        dispatcher.register(ClientCommandManager.literal(InventiveInventory.MOD_ID)
                .then(ClientCommandManager.literal("profiles")
                        .then(ClientCommandManager.literal("load")
                                .then(ClientCommandManager.argument("profile", StringArgumentType.greedyString())
                                        .suggests(ProfilesLoadCommand::getProfiles)
                                        .executes(ProfilesLoadCommand::load)
                                )
                        )
                )
        );
    }

    private static int load(CommandContext<FabricClientCommandSource> context) {
        String profileArg = StringArgumentType.getString(context, "profile");
        for (Profile profile : ProfileHandler.getProfiles()) {
            if (!profileArg.isEmpty() && profile.getName().equals(profileArg)) {
                ProfileHandler.load(profile);
                return 1;
            }
        }
        Notifier.error(Text.translatable(ERROR_TRANSLATION_KEY + "does_not_exist").getString());
        return -1;
    }

    private static CompletableFuture<Suggestions> getProfiles(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        ProfileHandler.getProfiles().forEach(profile -> {
            if (!profile.getName().isEmpty()) builder.suggest(profile.getName());
        });
        return builder.buildFuture();
    }
}
