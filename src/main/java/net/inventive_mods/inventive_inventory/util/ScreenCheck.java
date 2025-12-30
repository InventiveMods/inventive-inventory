package net.inventive_mods.inventive_inventory.util;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.features.profiles.gui.ProfilesScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;

public class ScreenCheck {
    public static boolean isNone() {
        return InventiveInventory.getScreen() == null;
    }

    public static boolean isSurvivalInventory() {
        return InventiveInventory.getScreen() instanceof InventoryScreen;
    }

    public static boolean isCreativeInventory() {
        return InventiveInventory.getScreen() instanceof CreativeInventoryScreen;
    }

    public static boolean isPlayerInventory() {
        return isSurvivalInventory() || isCreativeInventory();
    }

    public static boolean isPlayerHandler() {
        return InventiveInventory.getScreenHandler() instanceof PlayerScreenHandler;
    }

    public static boolean isProfileScreen() {
        return InventiveInventory.getScreen() instanceof ProfilesScreen;
    }

    public static boolean isContainer() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        return screenHandler instanceof GenericContainerScreenHandler || screenHandler instanceof Generic3x3ContainerScreenHandler
                || screenHandler instanceof HopperScreenHandler || screenHandler.slots.size() - PlayerInventory.MAIN_SIZE > 10;
    }
}
