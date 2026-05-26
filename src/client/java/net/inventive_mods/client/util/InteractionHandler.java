package net.inventive_mods.client.util;


import net.inventive_mods.client.InventiveInventoryClient;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;

public class InteractionHandler {
    private static final int LEFT_CLICK = 0;
    private static final int RIGHT_CLICK = 1;

    public static ItemStack getCursorStack() {
        return InventiveInventoryClient.getMenu().getCarried();
    }

    public static boolean isCursorFull() {
        return !getCursorStack().isEmpty();
    }

    public static ItemStack getStackFromSlot(int slot) {
        AbstractContainerMenu menu = InventiveInventoryClient.getMenu();
        return menu.getSlot(slot).getItem();
    }

    public static int getSelectedSlot() {
        AbstractContainerMenu menu = InventiveInventoryClient.getMenu();
        Player player = InventiveInventoryClient.getPlayer();
        return menu.findSlot(player.getInventory(), player.getInventory().getSelectedSlot()).orElse(-1);
    }

    public static void setSelectedSlot(int slot) {
        InventiveInventoryClient.getPlayer().getInventory().setSelectedSlot(slot);
    }

    public static ItemStack getMainHandStack() {
        return InventiveInventoryClient.getPlayer().getMainHandItem();
    }

    public static ItemStack getOffHandStack() {
        return InventiveInventoryClient.getPlayer().getOffhandItem();
    }

    public static ItemStack getAnyHandStack() {
        if (!ItemStack.isSameItem(getMainHandStack(), ItemStack.EMPTY)) return getMainHandStack();
        else if (!ItemStack.isSameItem(getOffHandStack(), ItemStack.EMPTY)) return getOffHandStack();
        else return ItemStack.EMPTY;
    }

    public static void leftClickStack(int slot) {
        MultiPlayerGameMode gameMode = InventiveInventoryClient.getGameMode();
        Player player = InventiveInventoryClient.getPlayer();
        int clickType = (getCursorStack().getItem() instanceof BundleItem && !getStackFromSlot(slot).isEmpty()) || (getStackFromSlot(slot).getItem() instanceof BundleItem && isCursorFull()) ? RIGHT_CLICK : LEFT_CLICK;
        gameMode.handleContainerInput(getSyncId(), slot, clickType, ContainerInput.PICKUP, player);
    }

    public static void rightClickStack(int slot) {
        MultiPlayerGameMode gameMode = InventiveInventoryClient.getGameMode();
        Player player = InventiveInventoryClient.getPlayer();
        int clickType = getCursorStack().getItem() instanceof BundleItem ? LEFT_CLICK : RIGHT_CLICK;
        gameMode.handleContainerInput(getSyncId(), slot, clickType, ContainerInput.PICKUP, player);
    }

    public static void swapStacks(int slot, int target) {
        leftClickStack(slot);
        leftClickStack(target);
        if (isCursorFull()) {
            leftClickStack(slot);
        }
    }

    public static void dropItem(int slot, int times) {
        MultiPlayerGameMode gameMode = InventiveInventoryClient.getGameMode();
        Player player = InventiveInventoryClient.getPlayer();
        if (isCursorFull()) {
            leftClickStack(slot);
            dropCursor(times);
            leftClickStack(slot);
        } else {
            for (; times > 0; times--)
                gameMode.handleContainerInput(getSyncId(), slot, LEFT_CLICK, ContainerInput.THROW, player);
        }
    }

    public static void dropCursor(int times) {
        MultiPlayerGameMode gameMode = InventiveInventoryClient.getGameMode();
        Player player = InventiveInventoryClient.getPlayer();
        for (; times > 0; times--)
            gameMode.handleContainerInput(getSyncId(), AbstractContainerMenu.SLOT_CLICKED_OUTSIDE, RIGHT_CLICK, ContainerInput.PICKUP, player);
    }

    public static void quickMove(int slot) {
        MultiPlayerGameMode gameMode = InventiveInventoryClient.getGameMode();
        Player player = InventiveInventoryClient.getPlayer();
        gameMode.handleContainerInput(getSyncId(), slot, LEFT_CLICK, ContainerInput.QUICK_MOVE, player);
    }

    private static int getSyncId() {
        return InventiveInventoryClient.getMenu().containerId;
    }
}
