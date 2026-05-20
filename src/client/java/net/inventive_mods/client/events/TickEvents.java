package net.inventive_mods.client.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.enums.Status;
import net.inventive_mods.client.config.enums.automatic_refilling.AutomaticRefillingMode;
import net.inventive_mods.client.context.ContextManager;
import net.inventive_mods.client.context.Contexts;
import net.inventive_mods.client.features.automatic_refilling.AutomaticRefillingHandler;
import net.inventive_mods.client.features.locked_slots.LockedSlotsHandler;
import net.inventive_mods.client.features.profiles.Profile;
import net.inventive_mods.client.features.profiles.ProfileHandler;
import net.inventive_mods.client.features.profiles.gui.ProfilesScreen;
import net.inventive_mods.client.keys.KeyRegistry;
import net.inventive_mods.client.keys.handler.AdvancedOperationHandler;
import net.inventive_mods.client.util.InteractionHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import java.util.List;

public class TickEvents {

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            TickEvents.playerHandling(client);
            TickEvents.checkKeys(client);
            TickEvents.adjustInventory(client);
            TickEvents.automaticRefilling(client);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            TickEvents.automaticRefilling(client);
            TickEvents.captureInventory(client);
            TickEvents.loadProfile(client);
        });
    }

    private static void playerHandling(Minecraft client) {
        if (client.player == null) {
            LockedSlotsHandler.reset();
            LockedSlotsHandler.schedulerStarted = false;
            return;
        }
        if (!LockedSlotsHandler.schedulerStarted) LockedSlotsHandler.startScheduler();
        else if (LockedSlotsHandler.shouldInit) LockedSlotsHandler.init();
    }


    private static void checkKeys(Minecraft client) {
        if (client.player == null || client.player.hasInfiniteMaterials()) return;
        if (client.screen == null) {
            AdvancedOperationHandler.setPressed(KeyRegistry.advancedOperationKey.isDown());
        }
        if (AutomaticRefillingHandler.getSelectedSlot() != InteractionHandler.getSelectedSlot()) {
            AutomaticRefillingHandler.reset();
        }
        if (KeyRegistry.openProfilesScreenKey.isDown() && ConfigManager.PROFILES_STATUS.is(Status.ENABLED)) {
            client.setScreen(new ProfilesScreen());
        }
    }

    private static void adjustInventory(Minecraft client) {
        if (client.player == null || client.player.hasInfiniteMaterials()) return;
        if (ContextManager.isInit()) LockedSlotsHandler.adjustInventory();
    }

    private static void automaticRefilling(Minecraft client) {
        if (client.player == null || client.player.hasInfiniteMaterials()) return;
        if (AutomaticRefillingMode.isValid() && ConfigManager.AUTOMATIC_REFILLING_STATUS.is(Status.ENABLED) && ContextManager.isInit() && AutomaticRefillingHandler.shouldRun()) {
            ContextManager.setContext(Contexts.AUTOMATIC_REFILLING);
            AutomaticRefillingHandler.runMainHand();
            ContextManager.setContext(Contexts.INIT);
        }
        if (AutomaticRefillingMode.isValid() && ConfigManager.AUTOMATIC_REFILLING_STATUS.is(Status.ENABLED) && ContextManager.isInit() && AutomaticRefillingHandler.shouldRunOffHand()) {
            ContextManager.setContext(Contexts.AUTOMATIC_REFILLING);
            AutomaticRefillingHandler.runOffHand();
            ContextManager.setContext(Contexts.INIT);
        }
    }

    private static void captureInventory(Minecraft client) {
        if (client.player == null || client.player.hasInfiniteMaterials()) return;
        LockedSlotsHandler.setSavedInventory();
        LockedSlotsHandler.setSavedHandlerInventory();
        AutomaticRefillingHandler.setMainHandStack(client.player.getMainHandItem());
        AutomaticRefillingHandler.setOffHandStack(client.player.getOffhandItem());
        AutomaticRefillingHandler.setSelectedSlot(InteractionHandler.getSelectedSlot());
    }

    private static void loadProfile(Minecraft client) {
        if (client.player == null || client.player.hasInfiniteMaterials()) return;
        for (KeyMapping profileKey : KeyRegistry.profileKeys) {
            if (profileKey.isDown()) {
                boolean validMode = ConfigManager.FAST_LOAD.is(true) || (ConfigManager.FAST_LOAD.is(false) && KeyRegistry.loadProfileKey.isDown());
                if (validMode && ContextManager.isInit()) {
                    ContextManager.setContext(Contexts.PROFILES);
                    List<Profile> profiles = ProfileHandler.getProfiles();
                    profiles.forEach(profile -> {
                        if (profileKey.getName().equals(profile.getKey())) ProfileHandler.load(profile);
                    });
                    ContextManager.setContext(Contexts.INIT);
                }
            }
        }
    }
}
