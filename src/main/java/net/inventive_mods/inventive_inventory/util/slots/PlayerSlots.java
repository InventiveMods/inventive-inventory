package net.inventive_mods.inventive_inventory.util.slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.inventive_mods.inventive_inventory.InventiveInventory;

import java.util.List;

public class PlayerSlots {
    public static SlotRange get() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        if (screenHandler == null) return SlotRange.empty();
        List<Slot> playerSlots = screenHandler.slots.stream()
                .filter(slot -> slot.inventory instanceof PlayerInventory)
                .filter(slot -> !PlayerInventory.isValidHotbarIndex(slot.getIndex()))
                .filter(slot -> !(screenHandler instanceof PlayerScreenHandler) || !PlayerScreenHandler.isInHotbar(slot.id))
                .toList();

        if (playerSlots.stream().anyMatch(slot -> slot.getClass().equals(Slot.class))) {
            playerSlots = playerSlots.stream().filter(slot -> slot.getClass().equals(Slot.class)).toList();
        }

        if (playerSlots.isEmpty())
            return SlotRange.empty();

        int start = playerSlots.get(0).id;
        int stop = playerSlots.get(playerSlots.size() - 1).id;
        return new SlotRange(start, stop);
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
