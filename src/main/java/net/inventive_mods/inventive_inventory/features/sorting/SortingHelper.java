package net.inventive_mods.inventive_inventory.features.sorting;

import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.util.InteractionHandler;
import net.inventive_mods.inventive_inventory.util.ItemStackUtils;
import net.inventive_mods.inventive_inventory.util.slots.SlotRange;
import net.minecraft.item.ItemStack;

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
            if (!stack.isEmpty() && stack.getCount() < stack.getMaxCount()) {
                for (int followingSlot : followingSlots) {
                    ItemStack followingStack = InteractionHandler.getStackFromSlot(followingSlot);
                    if (ItemStackUtils.areEqualWithoutCount(stack, followingStack) && followingStack.getCount() < followingStack.getMaxCount()) {
                        InteractionHandler.swapStacks(followingSlot, slot);
                    } else if (ItemStackUtils.areEqualWithoutCount(stack, InteractionHandler.getCursorStack())) {
                        InteractionHandler.leftClickStack(slot);
                    }
                }
            }
        }
        if (InteractionHandler.isCursorFull()) {
            if (emptySlot != null) {
                InteractionHandler.leftClickStack(emptySlot);
            } else if (ItemStackUtils.areEqualWithoutCount(InteractionHandler.getStackFromSlot(slotRange.get(slotRange.size() - 1)), InteractionHandler.getCursorStack())) {
                InteractionHandler.leftClickStack(slotRange.get(slotRange.size() - 1));
            }
        }
    }

    public static void sortItemStacks(SlotRange inventorySlots) {
        List<Integer> sortedSlots = new ArrayList<>(getSortedSlots(inventorySlots));
        for (int i = 0; i < sortedSlots.size(); i++) {
            if (sortedSlots.get(i).equals(inventorySlots.get(i))) continue;
            if (ItemStackUtils.areEqualWithoutCount(InteractionHandler.getStackFromSlot(sortedSlots.get(i)), InteractionHandler.getCursorStack())) {
                for (int slot : inventorySlots) {
                    if (!ItemStackUtils.areEqualWithoutCount(InteractionHandler.getStackFromSlot(slot), InteractionHandler.getCursorStack()) && slot != sortedSlots.get(i)) {
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
        InteractionHandler.leftClickStack(sameStackSlots.get(0));
        if (InteractionHandler.getCursorStack().getCount() > targetStack.getCount()) {
            while (InteractionHandler.getCursorStack().getCount() > targetStack.getCount()) {
                InteractionHandler.rightClickStack(sameStackSlots.get(0));
            }
        } else if (InteractionHandler.getCursorStack().getCount() < targetStack.getCount()) {
            InteractionHandler.leftClickStack(sameStackSlots.get(0));
            InteractionHandler.leftClickStack(sameStackSlots.get(1));
            while (InteractionHandler.getStackFromSlot(sameStackSlots.get(0)).getCount() < targetStack.getCount()) {
                InteractionHandler.rightClickStack(sameStackSlots.get(0));
            }
            InteractionHandler.leftClickStack(sameStackSlots.get(1));
            InteractionHandler.leftClickStack(sameStackSlots.get(0));
        }
    }

    private static void handleFullCursorStack(SlotRange slotRange, ItemStack targetStack) {
        List<Integer> sameStackSlots = findSameStacks(slotRange, targetStack);
        Collections.reverse(sameStackSlots);
        slotRange.removeAll(sameStackSlots);
        if (slotRange.isEmpty()) return;

        InteractionHandler.leftClickStack(slotRange.get(0));
        InteractionHandler.leftClickStack(sameStackSlots.get(0));

        while (targetStack.getCount() > InteractionHandler.getStackFromSlot(slotRange.get(0)).getCount()) {
            InteractionHandler.rightClickStack(slotRange.get(0));
        }
        InteractionHandler.leftClickStack(sameStackSlots.get(0));
        InteractionHandler.leftClickStack(slotRange.get(0));
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
                .sorted(ConfigManager.SORTING_MODE.getValue().getComparator())
                .toList();
    }

    private static List<Integer> findSameStacks(SlotRange slotRange, ItemStack targetStack) {
        return new ArrayList<>(slotRange.stream()
                .filter(slot -> ItemStackUtils.areEqualWithoutCount(InteractionHandler.getStackFromSlot(slot), targetStack))
                .toList());
    }
}
