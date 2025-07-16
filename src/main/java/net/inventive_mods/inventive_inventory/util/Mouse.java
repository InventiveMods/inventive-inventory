package net.inventive_mods.inventive_inventory.util;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.util.slot.SlotRange;
import net.inventive_mods.inventive_inventory.util.slot.SlotType;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class Mouse {
    public static boolean isOverInventory() {
        if (ScreenCheck.isPlayerInventory())
            return true;
        if (InventiveInventory.getScreen() instanceof AbstractContainerScreen<?> containerScreen && containerScreen.hoveredSlot != null)
            return SlotRange.getPlayerSlots().append(SlotType.HOTBAR).contains(containerScreen.hoveredSlot.index);
        return false;
    }
}
