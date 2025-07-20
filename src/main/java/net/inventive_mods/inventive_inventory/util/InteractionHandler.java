package net.inventive_mods.inventive_inventory.util;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class InteractionHandler {
    public static ItemStack getStackFromSlot(int slot) {
        return InventiveInventory.getMenu().getSlot(slot).getItem();
    }

    public static ItemStack getCursorStack() {
        return InventiveInventory.getMenu().getCarried();
    }

    public static ItemStack getMainHandStack() {
        return InventiveInventory.getPlayer().getMainHandItem();
    }

    public static ItemStack getOffHandStack() {
        return InventiveInventory.getPlayer().getOffhandItem();
    }

    public static ItemStack getAnyHandStack() {
        if (!ItemStack.isSameItem(getMainHandStack(), ItemStack.EMPTY)) return getMainHandStack();
        else if (!ItemStack.isSameItem(getOffHandStack(), ItemStack.EMPTY)) return getOffHandStack();
        else return ItemStack.EMPTY;
    }

    public static int getSelectedSlot() {
        AbstractContainerMenu menu = InventiveInventory.getMenu();
        LocalPlayer player = InventiveInventory.getPlayer();
        return menu.findSlot(player.getInventory(), player.getInventory().getSelectedSlot()).orElse(-1);
    }

    public static void setSelectedSlot(int slot) {
        InventiveInventory.getPlayer().getInventory().setSelectedSlot(slot);
    }

    public static boolean isCursorFull() {
        return !getCursorStack().isEmpty();
    }

    public static void leftClickStack(int slot) {
        MultiPlayerGameMode gameMode = InventiveInventory.getGameMode();
        LocalPlayer player = InventiveInventory.getPlayer();
        gameMode.handleInventoryMouseClick(getContainerId(), slot, GLFW.GLFW_MOUSE_BUTTON_LEFT, ClickType.PICKUP, player);
    }

    public static void rightClickStack(int slot) {
        MultiPlayerGameMode gameMode = InventiveInventory.getGameMode();
        LocalPlayer player = InventiveInventory.getPlayer();
        gameMode.handleInventoryMouseClick(getContainerId(), slot, GLFW.GLFW_MOUSE_BUTTON_RIGHT, ClickType.PICKUP, player);
    }

    public static void swapStacks(int slot, int target) {
        MultiPlayerGameMode gameMode = InventiveInventory.getGameMode();
        LocalPlayer player = InventiveInventory.getPlayer();
        gameMode.handleInventoryMouseClick(getContainerId(), slot, GLFW.GLFW_MOUSE_BUTTON_LEFT, ClickType.PICKUP, player);
        gameMode.handleInventoryMouseClick(getContainerId(), target, GLFW.GLFW_MOUSE_BUTTON_LEFT, ClickType.PICKUP, player);
        if (isCursorFull()) {
            gameMode.handleInventoryMouseClick(getContainerId(), slot, GLFW.GLFW_MOUSE_BUTTON_LEFT, ClickType.PICKUP, player);
        }
    }

    public static void dropCursor(int times) {
        MultiPlayerGameMode gameMode = InventiveInventory.getGameMode();
        LocalPlayer player = InventiveInventory.getPlayer();
        for (; times > 0; times--) gameMode.handleInventoryMouseClick(getContainerId(), AbstractContainerMenu.SLOT_CLICKED_OUTSIDE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, ClickType.PICKUP, player);
    }

    public static void dropItem(int slot, int times) {
        MultiPlayerGameMode gameMode = InventiveInventory.getGameMode();
        LocalPlayer player = InventiveInventory.getPlayer();
        if (isCursorFull()) {
            leftClickStack(slot);
            dropCursor(times);
            leftClickStack(slot);
        } else for (; times > 0; times--) gameMode.handleInventoryMouseClick(getContainerId(), slot, GLFW.GLFW_MOUSE_BUTTON_LEFT, ClickType.THROW, player);
    }

    private static int getContainerId() {
        return InventiveInventory.getMenu().containerId;
    }
}
