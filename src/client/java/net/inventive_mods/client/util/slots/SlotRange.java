package net.inventive_mods.client.util.slots;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.features.locked_slots.LockedSlotsHandler;
import net.inventive_mods.client.util.ScreenCheck;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;

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
            AbstractContainerMenu menu = InventiveInventoryClient.getMenu();
            List<Slot> playerSlots = menu.slots.stream()
                    .filter(slot -> slot.container instanceof Inventory)
                    .filter(slot -> Inventory.isHotbarSlot(slot.getContainerSlot()))
                    .toList();

            if (playerSlots.stream().anyMatch(slot -> slot.getClass().equals(Slot.class))) {
                playerSlots = playerSlots.stream().filter(slot -> slot.getClass().equals(Slot.class)).toList();
            }

            if (playerSlots.isEmpty())
                return this;

            int start = playerSlots.getFirst().index;
            int stop = playerSlots.getLast().index;
            IntStream.rangeClosed(start, stop).forEach(this::add);
        } else if (type == SlotTypes.INVENTORY) {
            this.addAll(PlayerSlots.get());
        } else if (type == SlotTypes.OFFHAND) {
            if (ScreenCheck.isInventoryMenu()) this.add(InventoryMenu.SHIELD_SLOT);
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
