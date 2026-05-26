package net.inventive_mods.client.util;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.features.profiles.gui.ProfilesScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.inventory.InventoryMenu;


public class ScreenCheck {
    public static boolean isNone() {
        return InventiveInventoryClient.getScreen() == null;
    }

    public static boolean isSurvivalInventory() {
        return InventiveInventoryClient.getScreen() instanceof InventoryScreen;
    }

    public static boolean isCreativeInventory() {
        return InventiveInventoryClient.getScreen() instanceof CreativeModeInventoryScreen;
    }

    public static boolean isPlayerInventory() {
        return isSurvivalInventory() || isCreativeInventory();
    }

    public static boolean isInventoryMenu() {
        return InventiveInventoryClient.getMenu() instanceof InventoryMenu;
    }

    public static boolean isProfileScreen() {
        return InventiveInventoryClient.getScreen() instanceof ProfilesScreen;
    }

    public static boolean isContainer() {
        AbstractContainerMenu menu = InventiveInventoryClient.getMenu();
        return menu instanceof HopperMenu || menu instanceof DispenserMenu || menu.slots.size() - Inventory.INVENTORY_SIZE > 10;
    }
}
