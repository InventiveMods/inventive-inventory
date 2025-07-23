package net.inventive_mods.inventive_inventory.feature.sorting;

import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.util.InteractionHandler;
import net.inventive_mods.inventive_inventory.util.slot.SlotRange;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class SortingHelper {

    public static void mergeItemStacks(SlotRange slotRange) {
        Integer emptySlot = null;
        SlotRange followingSlots = slotRange.copy();
        for (int slot : slotRange) {
            followingSlots.remove((Integer) slot);
            ItemStack stack = InteractionHandler.getStackFromSlot(slot);
            if (stack.isEmpty() && emptySlot == null) emptySlot = slot;
            if (!stack.isEmpty() && stack.getCount() < stack.getMaxStackSize()) {
                for (int followingSlot : followingSlots) {
                    ItemStack followingStack = InteractionHandler.getStackFromSlot(followingSlot);
                    if (ItemStack.isSameItem(stack, followingStack) && followingStack.getCount() < followingStack.getMaxStackSize()) {
                        InteractionHandler.swapStacks(followingSlot, slot);
                    } else if (ItemStack.isSameItem(stack, InteractionHandler.getCursorStack())) {
                        InteractionHandler.leftClickStack(slot);
                    }
                }
            }
        }
        if (InteractionHandler.isCursorFull()) {
            if (emptySlot != null) {
                InteractionHandler.leftClickStack(emptySlot);
            } else if (ItemStack.isSameItem(InteractionHandler.getStackFromSlot(slotRange.getLast()), InteractionHandler.getCursorStack())) {
                InteractionHandler.leftClickStack(slotRange.getLast());
            }
        }
    }

    public static void sortItemStacks(SlotRange inventorySlots) {
        List<Integer> sortedSlots = new ArrayList<>(getSortedSlots(inventorySlots));
        for (int i = 0; i < sortedSlots.size(); i++) {
            if (sortedSlots.get(i).equals(inventorySlots.get(i))) continue;
            if (ItemStack.isSameItem(InteractionHandler.getStackFromSlot(sortedSlots.get(i)), InteractionHandler.getCursorStack())) {
                for (int slot : inventorySlots) {
                    if (!ItemStack.isSameItem(InteractionHandler.getStackFromSlot(slot), InteractionHandler.getCursorStack()) && slot != sortedSlots.get(i)) {
                        InteractionHandler.leftClickStack(slot);
                        InteractionHandler.swapStacks(sortedSlots.get(i), inventorySlots.get(i));
                        InteractionHandler.leftClickStack(slot);
                        break;
                    }
                }
            } else {
                InteractionHandler.swapStacks(sortedSlots.get(i), inventorySlots.get(i));
            }
            if (sortedSlots.contains(inventorySlots.get(i))) {
                sortedSlots.set(sortedSlots.indexOf(inventorySlots.get(i)), sortedSlots.get(i));
            }
            sortedSlots.set(i, inventorySlots.get(i));
        }
    }

    public static void adjustCursorStack(SlotRange slotRange, ItemStack targetStack) {
        if (targetStack.getCount() > InteractionHandler.getCursorStack().getCount()) {
            if (InteractionHandler.getCursorStack().isEmpty()) handleEmptyCursorStack(slotRange, targetStack);
            else handleFullCursorStack(slotRange, targetStack);
            rearrangeSlots(slotRange);
        }
    }

    private static void handleEmptyCursorStack(SlotRange slotRange, ItemStack targetStack) {
        List<Integer> sameStackSlots = findSameStacks(slotRange, targetStack);
        Collections.reverse(sameStackSlots);
        InteractionHandler.leftClickStack(sameStackSlots.getFirst());
        if (InteractionHandler.getCursorStack().getCount() > targetStack.getCount()) {
            while (InteractionHandler.getCursorStack().getCount() > targetStack.getCount()) {
                InteractionHandler.rightClickStack(sameStackSlots.getFirst());
            }
        } else if (InteractionHandler.getCursorStack().getCount() < targetStack.getCount()) {
            InteractionHandler.leftClickStack(sameStackSlots.getFirst());
            InteractionHandler.leftClickStack(sameStackSlots.get(1));
            while (InteractionHandler.getStackFromSlot(sameStackSlots.getFirst()).getCount() < targetStack.getCount()) {
                InteractionHandler.rightClickStack(sameStackSlots.getFirst());
            }
            InteractionHandler.leftClickStack(sameStackSlots.get(1));
            InteractionHandler.leftClickStack(sameStackSlots.getFirst());
        }
    }

    private static void handleFullCursorStack(SlotRange slotRange, ItemStack targetStack) {
        List<Integer> sameStackSlots = findSameStacks(slotRange, targetStack);
        Collections.reverse(sameStackSlots);
        slotRange.removeAll(sameStackSlots);
        if (slotRange.isEmpty()) return;

        InteractionHandler.leftClickStack(slotRange.getFirst());
        InteractionHandler.leftClickStack(sameStackSlots.getFirst());

        while (targetStack.getCount() > InteractionHandler.getStackFromSlot(slotRange.getFirst()).getCount()) {
            InteractionHandler.rightClickStack(slotRange.getFirst());
        }
        InteractionHandler.leftClickStack(sameStackSlots.getFirst());
        InteractionHandler.leftClickStack(slotRange.getFirst());
    }

    private static void rearrangeSlots(SlotRange slotRange) {
        SlotRange followingSlots = slotRange.copy();
        for (int slot : slotRange) {
            followingSlots.remove((Integer) slot);
            if (InteractionHandler.getStackFromSlot(slot).isEmpty()) {
                for (int followingSlot : followingSlots) {
                    InteractionHandler.swapStacks(followingSlot, slot);
                    InteractionHandler.leftClickStack(followingSlot);
                    slot = followingSlot;
                }
                return;
            }
        }
    }

    private static List<Integer> getSortedSlots(SlotRange slotRange) {
        return slotRange.stream()
                .filter(slot -> !InteractionHandler.getStackFromSlot(slot).isEmpty())
                .sorted(Config.SORTING_MODE.getValue().getComparator())
                .toList();
    }

    private static List<Integer> findSameStacks(SlotRange slotRange, ItemStack targetStack) {
        return new ArrayList<>(slotRange.stream()
                .filter(slot -> ItemStack.isSameItem(InteractionHandler.getStackFromSlot(slot), targetStack))
                .toList());
    }
}
