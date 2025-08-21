package net.inventive_mods.inventive_inventory;

import net.fabricmc.api.ClientModInitializer;
import net.inventive_mods.inventive_inventory.features.item_counter.ItemCounterHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.ScreenHandler;
import net.inventive_mods.inventive_inventory.commands.CommandRegistry;
import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.events.ConnectionEvents;
import net.inventive_mods.inventive_inventory.events.TickEvents;
import net.inventive_mods.inventive_inventory.keys.KeyRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class InventiveInventory implements ClientModInitializer {
    public static final String MOD_ID = "inventive_inventory";
    public static final String MOD_NAME = "Inventive Inventory";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitializeClient() {
        try {
            ConfigManager.init();
            KeyRegistry.register();
            ConnectionEvents.register();
            TickEvents.register();
            CommandRegistry.register();
            ItemCounterHandler.register();
            LOGGER.info(MOD_NAME + " initialized successfully!");
        } catch (IOException e) {
            LOGGER.error("Couldn't create config files", e);
            LOGGER.error(MOD_NAME + " could not be initialized correctly!");
            LOGGER.error("DELETE THE " + InventiveInventory.MOD_ID + " CONFIG DIRECTORY!");
        }
    }

    public static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }

    public static ClientPlayerEntity getPlayer() {
        return getClient().player;
    }

    public static ClientWorld getWorld() {
        return getClient().world;
    }

    public static Screen getScreen() {
        return getClient().currentScreen;
    }

    public static ClientPlayerInteractionManager getInteractionManager() {
        return getClient().interactionManager;
    }

    public static DynamicRegistryManager getRegistryManager() {
        return getWorld().getRegistryManager();
    }

    public static ScreenHandler getScreenHandler() {
        if (getPlayer() == null) return null;
        return getPlayer().currentScreenHandler;
    }

    public static String getWorldName() {
        String worldName = "";
        if (InventiveInventory.getClient().isInSingleplayer() && InventiveInventory.getClient().getServer() != null) {
            worldName = InventiveInventory.getClient().getServer().getSaveProperties().getLevelName();
        } else {
            if (InventiveInventory.getClient().getNetworkHandler() != null) {
                String address = InventiveInventory.getClient().getNetworkHandler().getConnection().getAddress().toString();
                if (address.contains("/")) worldName = address.split("/")[0];
                else worldName = address;
            }
        }
        return worldName;
    }
}
