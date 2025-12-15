package net.inventive_mods.inventive_inventory.util;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class InteractionHandler {
    private static final int LEFT_CLICK = 0;
    private static final int RIGHT_CLICK = 1;

    public static ItemStack getCursorStack() {
        return InventiveInventory.getScreenHandler().getCursorStack();
    }

    public static boolean isCursorFull() {
        return !getCursorStack().isEmpty();
    }

    public static ItemStack getStackFromSlot(int slot) {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        return screenHandler.getSlot(slot).getStack();
    }

    public static int getSelectedSlot() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        return screenHandler.getSlotIndex(player.getInventory(), player.getInventory().getSelectedSlot()).orElse(-1);
    }

    public static void setSelectedSlot(int slot) {
        InventiveInventory.getPlayer().getInventory().setSelectedSlot(slot);
    }

    public static ItemStack getMainHandStack() {
        return InventiveInventory.getPlayer().getMainHandStack();
    }

    public static ItemStack getOffHandStack() {
        return InventiveInventory.getPlayer().getOffHandStack();
    }

    public static ItemStack getAnyHandStack() {
        if (!ItemStack.areItemsEqual(getMainHandStack(), ItemStack.EMPTY)) return getMainHandStack();
        else if (!ItemStack.areItemsEqual(getOffHandStack(), ItemStack.EMPTY)) return getOffHandStack();
        else return ItemStack.EMPTY;
    }

    public static void leftClickStack(int slot) {
        ClientPlayerInteractionManager manager = InventiveInventory.getInteractionManager();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        int clickType = (getCursorStack().getItem() instanceof BundleItem && !getStackFromSlot(slot).isEmpty()) || (getStackFromSlot(slot).getItem() instanceof BundleItem && isCursorFull()) ? RIGHT_CLICK : LEFT_CLICK;
        manager.clickSlot(getSyncId(), slot, clickType, SlotActionType.PICKUP, player);
    }

    public static void rightClickStack(int slot) {
        ClientPlayerInteractionManager manager = InventiveInventory.getInteractionManager();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        int clickType = getCursorStack().getItem() instanceof BundleItem ? LEFT_CLICK : RIGHT_CLICK;
        manager.clickSlot(getSyncId(), slot, clickType, SlotActionType.PICKUP, player);
    }

    public static void swapStacks(int slot, int target) {
        leftClickStack(slot);
        leftClickStack(target);
        if (isCursorFull()) {
            leftClickStack(slot);
        }
    }

    public static void dropItem(int slot, int times) {
        ClientPlayerInteractionManager manager = InventiveInventory.getInteractionManager();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        if (isCursorFull()) {
            leftClickStack(slot);
            dropCursor(times);
            leftClickStack(slot);
        } else
            for (; times > 0; times--) manager.clickSlot(getSyncId(), slot, LEFT_CLICK, SlotActionType.THROW, player);
    }

    public static void dropCursor(int times) {
        ClientPlayerInteractionManager manager = InventiveInventory.getInteractionManager();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        for (; times > 0; times--)
            manager.clickSlot(getSyncId(), ScreenHandler.EMPTY_SPACE_SLOT_INDEX, RIGHT_CLICK, SlotActionType.PICKUP, player);
    }

    public static void quickMove(int slot) {
        ClientPlayerInteractionManager manager = InventiveInventory.getInteractionManager();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        manager.clickSlot(getSyncId(), slot, LEFT_CLICK, SlotActionType.QUICK_MOVE, player);
    }

    private static int getSyncId() {
        return InventiveInventory.getScreenHandler().syncId;
    }
}
