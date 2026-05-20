package net.inventive_mods.client.util.slots;

import net.inventive_mods.client.InventiveInventoryClient;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

import java.util.List;

public class ContainerSlots {
    public static SlotRange get() {
        AbstractContainerMenu menu = InventiveInventoryClient.getMenu();
        List<Slot> containerSlots = menu.slots.stream().filter(slot -> !(slot.container instanceof Inventory)).toList();
        if (menu.getClass().getSimpleName().equals("BackpackBlockEntityMenu")) {
            containerSlots = containerSlots.stream().filter(slot -> slot.getClass().getSimpleName().equals("BackpackSlotItemHandler")).toList();
        }
        if (containerSlots.isEmpty()) return SlotRange.empty();
        return new SlotRange(containerSlots.getFirst().index, containerSlots.getLast().index);
    }
}
