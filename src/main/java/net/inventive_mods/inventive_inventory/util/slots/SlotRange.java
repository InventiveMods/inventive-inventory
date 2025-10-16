package net.inventive_mods.inventive_inventory.util.slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.inventive_mods.inventive_inventory.util.ScreenCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SlotRange extends ArrayList<Integer> {

    public SlotRange(int start, int stop) {
        super(IntStream.rangeClosed(start, stop).boxed().toList());
    }

    private SlotRange(List<Integer> list) {
        super(list);
    }

    public static SlotRange of(List<Integer> list) {
        return new SlotRange(list);
    }

    public static SlotRange empty() {
        return new SlotRange(0, -1);
    }

    public SlotRange append(SlotTypes type) {
        if (type == SlotTypes.HOTBAR) {
            ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
            List<Slot> playerSlots = screenHandler.slots.stream()
                    .filter(slot -> slot.inventory instanceof PlayerInventory)
                    .filter(slot -> PlayerInventory.isValidHotbarIndex(slot.getIndex()))
                    .toList();

            if (playerSlots.stream().anyMatch(slot -> slot.getClass().equals(Slot.class))) {
                playerSlots = playerSlots.stream().filter(slot -> slot.getClass().equals(Slot.class)).toList();
            }

            if (playerSlots.isEmpty())
                return this;

            int start = playerSlots.get(0).id;
            int stop = playerSlots.get(playerSlots.size() - 1).id;
            IntStream.rangeClosed(start, stop).forEach(this::add);
        } else if (type == SlotTypes.INVENTORY) {
            this.addAll(PlayerSlots.get());
        } else if (type == SlotTypes.OFFHAND) {
            if (ScreenCheck.isPlayerHandler()) this.add(PlayerScreenHandler.OFFHAND_ID);
        }
        return this;
    }

    public SlotRange exclude(SlotTypes type) {
        if (type == SlotTypes.LOCKED_SLOT) LockedSlotsHandler.getLockedSlots().forEach(this::remove);
        else if (type == SlotTypes.INVENTORY) PlayerSlots.get().forEach(this::remove);
        else if (type == SlotTypes.HOTBAR) PlayerSlots.get(SlotTypes.HOTBAR).forEach(this::remove);
        return this;
    }

    public SlotRange exclude(Integer slot) {
        this.remove(slot);
        return this;
    }

    public SlotRange copy() {
        return (SlotRange) this.clone();
    }
}
