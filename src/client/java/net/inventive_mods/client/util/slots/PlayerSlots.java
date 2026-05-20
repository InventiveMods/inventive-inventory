package net.inventive_mods.client.util.slots;

import net.inventive_mods.client.InventiveInventoryClient;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import java.util.List;

public class PlayerSlots {
    public static SlotRange get() {
        AbstractContainerMenu menu = InventiveInventoryClient.getMenu();
        if (menu == null) return SlotRange.empty();
        List<Slot> playerSlots = menu.slots.stream()
                .filter(slot -> slot.container instanceof Inventory)
                .filter(slot -> !Inventory.isHotbarSlot(slot.getContainerSlot()))
                .toList();

        if (playerSlots.stream().anyMatch(slot -> slot.getClass().equals(Slot.class))) {
            playerSlots = playerSlots.stream().filter(slot -> slot.getClass().equals(Slot.class)).toList();
        }

        if (playerSlots.isEmpty()) return SlotRange.empty();
        return new SlotRange(playerSlots.getFirst().index, playerSlots.getLast().index);
    }

    public static SlotRange get(SlotTypes... types) {
        SlotRange slotRange = SlotRange.empty();
        for (SlotTypes type : types) {
            if (type != SlotTypes.LOCKED_SLOT) slotRange.append(type);
            else throw new IllegalArgumentException("This SlotType is not valid in this function");
        }
        return slotRange;
    }
}
