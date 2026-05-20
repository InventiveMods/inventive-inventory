package net.inventive_mods.client.commands.profiles;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.features.profiles.Profile;
import net.inventive_mods.client.features.profiles.ProfileHandler;
import net.inventive_mods.client.util.Notifier;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.chat.Component;
import java.util.concurrent.CompletableFuture;

public class ProfilesLoadCommand {
    private final static String ERROR_TRANSLATION_KEY = "error.profiles." + InventiveInventoryClient.MOD_ID + ".";

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext ignored) {
        dispatcher.register(ClientCommands.literal(InventiveInventoryClient.MOD_ID)
                .then(ClientCommands.literal("profiles")
                        .then(ClientCommands.literal("load")
                                .then(ClientCommands.argument("profile", StringArgumentType.greedyString())
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
        Notifier.error(Component.translatable(ERROR_TRANSLATION_KEY + "does_not_exist").getString());
        return -1;
    }

    private static CompletableFuture<Suggestions> getProfiles(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        ProfileHandler.getProfiles().forEach(profile -> {
            if (!profile.getName().isEmpty()) builder.suggest(profile.getName());
        });
        return builder.buildFuture();
    }
}
