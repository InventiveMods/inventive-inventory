package net.inventive_mods.inventive_inventory.feature.locked_slots;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.key.AdvancedOperationHandler;
import net.inventive_mods.inventive_inventory.util.InteractionHandler;
import net.inventive_mods.inventive_inventory.util.slot.SlotRange;
import net.inventive_mods.inventive_inventory.util.slot.SlotType;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@EventBusSubscriber(modid = InventiveInventory.MOD_ID, value = Dist.CLIENT)
public class LockedSlotsHandler {
    private static final String LOCKED_SLOTS_FILE = "locked_slots.json";
    public static final Path LOCKED_SLOTS_PATH = Config.CONFIG_PATH.resolve(LOCKED_SLOTS_FILE);

    private static ItemStack pickUpStack = ItemStack.EMPTY;
    private static final List<ItemStack> savedInventory = new ArrayList<>();
    private static boolean onlyAdd = false;

    @SubscribeEvent
    public static void onSlotClick(ScreenEvent.MouseButtonPressed.Pre event) {
        if (!(event.getScreen() instanceof AbstractContainerScreen<?>) || event.getButton() != GLFW.GLFW_MOUSE_BUTTON_LEFT || !AdvancedOperationHandler.isPressed() || InventiveInventory.getPlayer().isCreative())
            return;
        Slot slot = ((AbstractContainerScreen<?>) event.getScreen()).getSlotUnderMouse();
        if (slot != null && SlotRange.getPlayerSlots().append(SlotType.HOTBAR).contains(slot.index)) {
            List<Integer> lockedSlots = LockedSlots.get();
            if (lockedSlots.contains(slot.index)) {
                LockedSlots.remove(slot.index);
                onlyAdd = false;
            } else {
                LockedSlots.add(slot.index);
                onlyAdd = true;
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onDragEvent(ScreenEvent.MouseDragged.Pre event) {
        if (!(event.getScreen() instanceof AbstractContainerScreen<?>) || !AdvancedOperationHandler.isPressed() || InventiveInventory.getPlayer().isCreative())
            return;
        Slot slot = ((AbstractContainerScreen<?>) event.getScreen()).getSlotUnderMouse();
        if (slot != null && SlotRange.getPlayerSlots().append(SlotType.HOTBAR).contains(slot.index)) {
            List<Integer> lockedSlots = LockedSlots.get();
            if (lockedSlots.contains(slot.index) && !onlyAdd) {
                LockedSlots.remove(slot.index);
            } else if (!lockedSlots.contains(slot.index) && onlyAdd) {
                LockedSlots.add(slot.index);
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void beforeItemPickup(ItemEntityPickupEvent.Pre event) {
        if (!event.getPlayer().equals(InventiveInventory.getPlayer()) || InventiveInventory.getPlayer().isCreative())
            return;
        pickUpStack = event.getItemEntity().getItem();
        savedInventory.clear();
        getMainSlots(InventiveInventory.getPlayer().inventoryMenu).forEach(slot -> savedInventory.add(slot.getItem().copy()));
    }

    @SubscribeEvent
    public static void onTick(ClientTickEvent.Post event) {
        if (InventiveInventory.getPlayer() == null || InventiveInventory.getPlayer().isCreative() || Config.PICKUP_INTO_LOCKED_SLOTS.is(true))
            return;
        if (!pickUpStack.isEmpty() && !savedInventory.isEmpty()) {
            List<ItemStack> inventory = new ArrayList<>();
            getMainSlots(InventiveInventory.getPlayer().inventoryMenu).forEach(slot -> inventory.add(slot.getItem().copy()));
            for (int i = 0; i < savedInventory.size(); i++) {
                ItemStack savedStack = savedInventory.get(i);
                ItemStack inventoryStack = inventory.get(i);
                if (!ItemStack.matches(savedStack, inventoryStack)) {
                    List<Integer> suitableSlots = SlotRange.getPlayerSlots(SlotType.HOTBAR, SlotType.INVENTORY).exclude(SlotType.LOCKED_SLOT).stream()
                            .filter(slot -> {
                                ItemStack stack = InteractionHandler.getStackFromSlot(slot);
                                return stack.isEmpty() || ItemStack.isSameItem(stack, pickUpStack) && stack.getCount() < stack.getMaxStackSize();
                            })
                            .sorted(Comparator.comparingInt(slot -> InteractionHandler.getStackFromSlot(slot).getCount()))
                            .toList();
                    Integer inventorySlot = SlotRange.getPlayerSlots().append(SlotType.HOTBAR).get(i);
                    if (!suitableSlots.isEmpty()) {
                        InteractionHandler.leftClickStack(inventorySlot);
                        for (int slot : suitableSlots) {
                            ItemStack stack = InteractionHandler.getStackFromSlot(slot);
                            while (InteractionHandler.getCursorStack().getCount() > savedStack.getCount()) {
                                if (stack.getCount() < stack.getMaxStackSize())
                                    InteractionHandler.rightClickStack(slot);
                                else
                                    break;
                            }
                        }
                        InteractionHandler.leftClickStack(inventorySlot);
                    } else {
                        int times = inventoryStack.getCount() - savedStack.getCount();
                        InteractionHandler.dropItem(inventorySlot, times);
                    }
                }
            }
            pickUpStack = ItemStack.EMPTY;
        }
    }

    private static List<Slot> getMainSlots(InventoryMenu inventoryMenu) {
        return inventoryMenu.slots.stream()
                .filter(slot -> !inventoryMenu.getResultSlot().equals(slot))
                .filter(slot -> !inventoryMenu.getInputGridSlots().contains(slot))
                .filter(slot -> slot.getClass().equals(Slot.class))
                .toList();
    }
}
