package net.inventive_mods.inventive_inventory.util.slot;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.features.locked_slots.LockedSlots;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SlotRange extends ArrayList<Integer> {

    private SlotRange(int start, int end) {
        super(IntStream.rangeClosed(start, end).boxed().toList());
    }

    private SlotRange(ArrayList<Integer> list) {
        super(list);
    }

    public static SlotRange range(int start, int end) {
        return new SlotRange(start, end);
    }

    public static SlotRange of(ArrayList<Integer> list) {
        return new SlotRange(list);
    }

    public static SlotRange empty() {
        return new SlotRange(0, -1);
    }

    public SlotRange append(SlotType type) {
        switch (type) {
            case HOTBAR -> {
                AbstractContainerMenu menu = InventiveInventory.getMenu();
                if (menu == null) break;

                List<Slot> hotbarSlots = menu.slots.stream()
                        .filter(slot -> slot.container instanceof Inventory)
                        .filter(slot -> Inventory.isHotbarSlot(slot.getSlotIndex()))
                        .toList();

                if (hotbarSlots.stream().anyMatch(slot -> slot.getClass().equals(Slot.class))) {
                    hotbarSlots = hotbarSlots.stream().filter(slot -> slot.getClass().equals(Slot.class)).toList();
                }
                IntStream.rangeClosed(hotbarSlots.getFirst().index, hotbarSlots.getLast().index).forEach(this::add);
            }
            case INVENTORY -> this.addAll(getPlayerSlots());
            case OFFHAND -> {
                if (InventiveInventory.getMenu() instanceof InventoryMenu) {
                    this.add(InventoryMenu.SHIELD_SLOT);
                }
            }
        }
        return this;
    }

    public SlotRange exclude(SlotType type) {
        switch (type) {
            case LOCKED_SLOT -> LockedSlots.get().forEach(this::remove);
            case INVENTORY -> SlotRange.getPlayerSlots().forEach(this::remove);
            case HOTBAR -> SlotRange.getPlayerSlots(SlotType.HOTBAR).forEach(this::remove);
        }
        return this;
    }

    public SlotRange exclude(Integer slot) {
        this.remove(slot);
        return this;
    }

    public static SlotRange getPlayerSlots() {
        AbstractContainerMenu menu = InventiveInventory.getMenu();
        if (menu == null) return empty();

        List<Slot> playerSlots = menu.slots.stream()
                .filter(slot -> slot.container instanceof Inventory)
                .filter(slot -> !Inventory.isHotbarSlot(slot.getSlotIndex()))
                .filter(slot -> !(menu instanceof InventoryMenu) || !InventoryMenu.isHotbarSlot(slot.index))
                .toList();

        if (playerSlots.stream().anyMatch(slot -> slot.getClass().equals(Slot.class))) {
            playerSlots = playerSlots.stream().filter(slot -> slot.getClass().equals(Slot.class)).toList();
        }
        if (playerSlots.isEmpty()) return empty();
        return range(playerSlots.getFirst().index, playerSlots.getLast().index);
    }

    public static SlotRange getPlayerSlots(SlotType... types) {
        SlotRange slotRange = empty();
        for (SlotType type : types) {
            if (type != SlotType.LOCKED_SLOT) {
                slotRange.append(type);
            } else {
                throw new IllegalArgumentException("This SlotType is not valid in this function");
            }
        }
        return slotRange;
    }

    public static SlotRange getContainerSlots() {
        AbstractContainerMenu menu = InventiveInventory.getMenu();
        if (menu == null) return empty();

        List<Slot> containerSlots = menu.slots.stream()
                .filter(slot -> !(slot.container instanceof Inventory))
                .toList();

        if (containerSlots.isEmpty()) return empty();
        return range(containerSlots.getFirst().index, containerSlots.getLast().index);
    }

    public SlotRange copy() {
        return (SlotRange) this.clone();
    }
}
