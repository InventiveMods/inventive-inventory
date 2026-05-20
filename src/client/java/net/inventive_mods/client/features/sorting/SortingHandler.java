package net.inventive_mods.client.features.sorting;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.enums.Status;
import net.inventive_mods.client.config.enums.sorting.CursorStackBehaviour;
import net.inventive_mods.client.context.ContextManager;
import net.inventive_mods.client.context.Contexts;
import net.inventive_mods.client.util.InteractionHandler;
import net.inventive_mods.client.util.ScreenCheck;
import net.inventive_mods.client.util.mouse.MouseLocation;
import net.inventive_mods.client.util.slots.ContainerSlots;
import net.inventive_mods.client.util.slots.PlayerSlots;
import net.inventive_mods.client.util.slots.SlotRange;
import net.inventive_mods.client.util.slots.SlotTypes;
import net.minecraft.world.item.ItemStack;

public class SortingHandler {

    public static void sort() {
        if (InventiveInventoryClient.getPlayer().isCreative() || ConfigManager.SORTING_STATUS.is(Status.DISABLED)) return;
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
