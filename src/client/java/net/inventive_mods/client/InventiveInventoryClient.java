package net.inventive_mods.client;

import net.fabricmc.api.ClientModInitializer;
import net.inventive_mods.client.commands.CommandRegistry;
import net.inventive_mods.client.events.ConnectionEvents;
import net.inventive_mods.client.events.TickEvents;
import net.inventive_mods.client.features.item_counter.ItemCounterHandler;
import net.inventive_mods.client.keys.KeyRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventiveInventoryClient implements ClientModInitializer {
	public static final String MOD_ID = "inventive-inventory";
	public static final String MOD_NAME = "Inventive Inventory";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	@Override
	public void onInitializeClient() {
//        try {
//            ConfigManager.init();
        KeyRegistry.register();
        ConnectionEvents.register();
        TickEvents.register();
        CommandRegistry.register();
        ItemCounterHandler.register();
		LOGGER.info(MOD_NAME + " initialized successfully!");
//        } catch (IOException e) {
//            LOGGER.error("Couldn't create config files", e);
//            LOGGER.error(MOD_NAME + " could not be initialized correctly!");
//            LOGGER.error("DELETE THE " + InventiveInventoryClient.MOD_ID + " CONFIG DIRECTORY!");
//        }
	}

    public static Minecraft getClient() {
        return Minecraft.getInstance();
    }

    public static Player getPlayer() {
        return getClient().player;
    }

    public static Screen getScreen() {
        return getClient().screen;
    }

    public static MultiPlayerGameMode getGameMode() {
        return getClient().gameMode;
    }

    public static RegistryAccess getRegistryManager() {
        if (getClient().level == null) return null;
        return getClient().level.registryAccess();
    }

    public static AbstractContainerMenu getMenu() {
        return getPlayer().containerMenu;
    }

    public static String getWorldName() {
        String worldName = "";
        if (InventiveInventoryClient.getClient().isSingleplayer() && InventiveInventoryClient.getClient().getSingleplayerServer() != null) {
            worldName = InventiveInventoryClient.getClient().getSingleplayerServer().name();
        } else {
            if (InventiveInventoryClient.getClient().getCurrentServer() != null) {
                String address = InventiveInventoryClient.getClient().getCurrentServer().ip;
                if (address.contains("/")) worldName = address.split("/")[0];
                else worldName = address;
            }
        }
        return worldName;
    }
}
