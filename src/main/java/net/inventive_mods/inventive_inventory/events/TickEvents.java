package net.inventive_mods.inventive_inventory.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.config.enums.Status;
import net.inventive_mods.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingMode;
import net.inventive_mods.inventive_inventory.context.ContextManager;
import net.inventive_mods.inventive_inventory.context.Contexts;
import net.inventive_mods.inventive_inventory.features.automatic_refilling.AutomaticRefillingHandler;
import net.inventive_mods.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.inventive_mods.inventive_inventory.features.profiles.Profile;
import net.inventive_mods.inventive_inventory.features.profiles.ProfileHandler;
import net.inventive_mods.inventive_inventory.features.profiles.gui.ProfilesScreen;
import net.inventive_mods.inventive_inventory.keys.KeyRegistry;
import net.inventive_mods.inventive_inventory.keys.handler.AdvancedOperationHandler;
import net.inventive_mods.inventive_inventory.util.InteractionHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

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

    private static void playerHandling(MinecraftClient client) {
        if (client.player == null) {
            LockedSlotsHandler.reset();
            LockedSlotsHandler.schedulerStarted = false;
            return;
        }
        if (!LockedSlotsHandler.schedulerStarted) LockedSlotsHandler.startScheduler();
        else if (LockedSlotsHandler.shouldInit) LockedSlotsHandler.init();
    }


    private static void checkKeys(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        if (client.currentScreen == null) {
            AdvancedOperationHandler.setPressed(KeyRegistry.advancedOperationKey.isPressed());
        }
        if (AutomaticRefillingHandler.getSelectedSlot() != InteractionHandler.getSelectedSlot()) {
            AutomaticRefillingHandler.reset();
        }
        if (KeyRegistry.openProfilesScreenKey.isPressed() && ConfigManager.PROFILES_STATUS.is(Status.ENABLED)) {
            client.setScreen(new ProfilesScreen());
        }
    }

    private static void adjustInventory(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        if (ContextManager.isInit()) LockedSlotsHandler.adjustInventory();
    }

    private static void automaticRefilling(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
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

    private static void captureInventory(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        LockedSlotsHandler.setSavedInventory();
        LockedSlotsHandler.setSavedHandlerInventory();
        AutomaticRefillingHandler.setMainHandStack(client.player.getMainHandStack());
        AutomaticRefillingHandler.setOffHandStack(client.player.getOffHandStack());
        AutomaticRefillingHandler.setSelectedSlot(InteractionHandler.getSelectedSlot());
    }

    private static void loadProfile(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        for (KeyBinding profileKey : KeyRegistry.profileKeys) {
            if (profileKey.isPressed()) {
                boolean validMode = ConfigManager.FAST_LOAD.is(true) || (ConfigManager.FAST_LOAD.is(false) && KeyRegistry.loadProfileKey.isPressed());
                if (validMode && ContextManager.isInit()) {
                    ContextManager.setContext(Contexts.PROFILES);
                    List<Profile> profiles = ProfileHandler.getProfiles();
                    profiles.forEach(profile -> {
                        if (profileKey.getTranslationKey().equals(profile.getKey())) ProfileHandler.load(profile);
                    });
                    ContextManager.setContext(Contexts.INIT);
                }
            }
        }
    }
}
