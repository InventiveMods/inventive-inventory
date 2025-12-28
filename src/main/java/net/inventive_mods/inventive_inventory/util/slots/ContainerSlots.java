package net.inventive_mods.inventive_inventory.util.slots;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.List;

public class ContainerSlots {
    public static SlotRange get() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        List<Slot> containerSlots = screenHandler.slots.stream().filter(slot -> !(slot.inventory instanceof PlayerInventory)).toList();
        if (screenHandler.getClass().getSimpleName().equals("BackpackBlockEntityMenu")) {
            containerSlots = containerSlots.stream().filter(slot -> slot.getClass().getSimpleName().equals("BackpackSlotItemHandler")).toList();
        }
        if (containerSlots.isEmpty()) return SlotRange.empty();
        return new SlotRange(containerSlots.getFirst().id, containerSlots.getLast().id);
    }
}
