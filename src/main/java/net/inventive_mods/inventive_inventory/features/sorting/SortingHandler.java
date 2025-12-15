package net.inventive_mods.inventive_inventory.features.sorting;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.config.enums.Status;
import net.inventive_mods.inventive_inventory.config.enums.sorting.CursorStackBehaviour;
import net.inventive_mods.inventive_inventory.context.ContextManager;
import net.inventive_mods.inventive_inventory.context.Contexts;
import net.inventive_mods.inventive_inventory.util.InteractionHandler;
import net.inventive_mods.inventive_inventory.util.ScreenCheck;
import net.inventive_mods.inventive_inventory.util.mouse.MouseLocation;
import net.inventive_mods.inventive_inventory.util.slots.ContainerSlots;
import net.inventive_mods.inventive_inventory.util.slots.PlayerSlots;
import net.inventive_mods.inventive_inventory.util.slots.SlotRange;
import net.inventive_mods.inventive_inventory.util.slots.SlotTypes;
import net.minecraft.item.ItemStack;

public class SortingHandler {

    public static void sort() {
        if (InventiveInventory.getPlayer().isCreative() || ConfigManager.SORTING_STATUS.is(Status.DISABLED)) return;
        ContextManager.setContext(Contexts.SORTING);
        SlotRange slotRange = MouseLocation.isOverInventory() || !ScreenCheck.isContainer() ? PlayerSlots.get().exclude(SlotTypes.LOCKED_SLOT) : ContainerSlots.get();
        ItemStack targetStack = InteractionHandler.getCursorStack().copy();

        SortingHelper.mergeItemStacks(slotRange);
        SortingHelper.sortItemStacks(slotRange);

        if (CursorStackBehaviour.isValid())
            SortingHelper.adjustCursorStack(slotRange, targetStack);
        ContextManager.setContext(Contexts.INIT);
    }
}
