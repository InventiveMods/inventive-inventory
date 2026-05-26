package net.inventive_mods.client.events;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.inventive_mods.client.features.automatic_refilling.AutomaticRefillingHandler;
import net.inventive_mods.client.features.locked_slots.LockedSlotsHandler;
import net.inventive_mods.client.features.profiles.ProfileHandler;

public class ConnectionEvents {

    public static void register() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            AutomaticRefillingHandler.reset();
            ProfileHandler.init();
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            LockedSlotsHandler.reset();
            LockedSlotsHandler.schedulerStarted = false;
        });
    }
}
