package net.inventive_mods.inventive_inventory.util;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.HopperMenu;

public class ScreenCheck {
    public static boolean isPlayerInventory() {
        return InventiveInventory.getScreen() instanceof InventoryScreen;
    }

    public static boolean isContainerScreen() {
        AbstractContainerMenu menu = InventiveInventory.getMenu();
        return menu instanceof ChestMenu || menu instanceof DispenserMenu
                || menu instanceof HopperMenu || menu.slots.size() - Inventory.INVENTORY_SIZE > 10;
    }
}
