package net.inventive_mods.inventive_inventory.feature.locked_slots;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.key.AdvancedOperationHandler;
import net.inventive_mods.inventive_inventory.util.InteractionHandler;
import net.inventive_mods.inventive_inventory.util.slot.SlotRange;
import net.inventive_mods.inventive_inventory.util.slot.SlotType;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.apache.logging.log4j.util.TriConsumer;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = InventiveInventory.MOD_ID, value = Dist.CLIENT)
public class LockedSlotsHandler {
    private static final String LOCKED_SLOTS_FILE = "locked_slots.json";
    public static final Path LOCKED_SLOTS_PATH = Config.CONFIG_PATH.resolve(LOCKED_SLOTS_FILE);

    private static List<ItemStack> savedInventory = new ArrayList<>();
    private static List<ItemStack> savedMenuInventory = new ArrayList<>();
    private static boolean slotClicked = false;
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
    public static void onMouseReleased(ScreenEvent.MouseButtonReleased.Pre event) {
        if (event.getScreen() instanceof AbstractContainerScreen<?> && (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT || event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT))
            slotClicked = true;
    }

    @SubscribeEvent
    public static void onStartTickEvent(ClientTickEvent.Pre event) {
        if (InventiveInventory.getPlayer() == null || InventiveInventory.getPlayer().isCreative() || savedInventory.isEmpty() || !LockedSlots.isReady())
            return;
        List<ItemStack> currentInventory = InventiveInventory.getPlayer().getInventory().getNonEquipmentItems();
        boolean itemAddedToInventory = isItemAddedToInventory(currentInventory);
        boolean itemQuickMoved = isItemQuickMoved(itemAddedToInventory);

        if (InventiveInventory.getMinecraft().screen == null && itemAddedToInventory && Config.PICKUP_INTO_LOCKED_SLOTS.is(false)) {
            rearrange(currentInventory, (inventorySlot, savedStack, currentStack) -> dropItems(inventorySlot, savedStack));
        } else if (InventiveInventory.getMinecraft().screen != null) {
            if (itemAddedToInventory && !itemQuickMoved && !slotClicked && Config.PICKUP_INTO_LOCKED_SLOTS.is(false)) {
                rearrange(currentInventory, (inventorySlot, savedStack, currentStack) -> dropItems(inventorySlot, savedStack));
            } else if (itemAddedToInventory && itemQuickMoved && Config.QUICK_MOVE_INTO_LOCKED_SLOTS.is(false)) {
                rearrange(currentInventory, (inventorySlot, savedStack, currentStack) -> handleSuitableSlots(SlotRange.getContainerSlots(), currentStack, inventorySlot, savedStack));
            }
        }
        slotClicked = false;
    }

    @SubscribeEvent
    public static void onEndTickEvent(ClientTickEvent.Post event) {
        if (InventiveInventory.getPlayer() == null || InventiveInventory.getPlayer().isCreative() && !LockedSlots.isReady())
            return;
        savedInventory = InventiveInventory.getPlayer().getInventory().getNonEquipmentItems().stream().map(ItemStack::copy).toList();
        savedMenuInventory = new ArrayList<>();
        if (InventiveInventory.getMinecraft().screen != null) {
            savedMenuInventory = InventiveInventory.getMenu().getItems().stream().map(ItemStack::copy).toList();
        }
    }

    private static boolean isItemAddedToInventory(List<ItemStack> currentInventory) {
        Map<Item, Integer> currentCountMap = getItemCountMap(currentInventory);
        Map<Item, Integer> savedCountMap = getItemCountMap(savedInventory);
        return currentCountMap.entrySet().stream().anyMatch(entry -> entry.getValue() > savedCountMap.getOrDefault(entry.getKey(), 0));
    }

    private static boolean isItemQuickMoved(boolean itemAddedToInventory) {
        List<ItemStack> currentMenuInventory = InventiveInventory.getMinecraft().screen != null ? InventiveInventory.getMenu().getItems().stream().map(ItemStack::copy).toList() : new ArrayList<>();
        Map<Item, Integer> currentCountMap = getItemCountMap(currentMenuInventory);
        Map<Item, Integer> savedCountMap = getItemCountMap(savedMenuInventory);
        return itemAddedToInventory && currentCountMap.entrySet().stream().allMatch(entry -> entry.getValue().equals(savedCountMap.getOrDefault(entry.getKey(), 0)));
    }

    private static Map<Item, Integer> getItemCountMap(List<ItemStack> inventory) {
        return inventory.stream().collect(Collectors.toMap(ItemStack::getItem, ItemStack::getCount, Integer::sum));
    }

    private static void dropItems(Integer inventorySlot, ItemStack savedStack) {
        InteractionHandler.dropItem(inventorySlot, InteractionHandler.getStackFromSlot(inventorySlot).getCount() - savedStack.getCount());
    }

    private static void handleSuitableSlots(SlotRange slotRange, ItemStack currentStack, Integer inventorySlot, ItemStack savedStack) {
        List<Integer> suitableSlots = slotRange.stream()
                .filter(slot -> {
                    ItemStack stack = InteractionHandler.getStackFromSlot(slot);
                    return stack.isEmpty() || ItemStack.isSameItem(stack, currentStack) && stack.getCount() < stack.getMaxStackSize();
                })
                .sorted(Comparator.comparing((Integer slot) -> InteractionHandler.getStackFromSlot(slot).getCount(), Comparator.reverseOrder()))
                .toList();
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
        }
    }

    private static void rearrange(List<ItemStack> currentInventory, TriConsumer<Integer, ItemStack, ItemStack> func) {
        List<Integer> lockedSlots = LockedSlots.get();
        int i = 0;
        for (int inventorySlot : SlotRange.getPlayerSlots(SlotType.HOTBAR, SlotType.INVENTORY)) {
            ItemStack currentStack = currentInventory.get(i);
            ItemStack savedStack = savedInventory.get(i);
            i++;
            if (!lockedSlots.contains(inventorySlot) || ItemStack.matches(currentStack, savedStack))
                continue;
            handleSuitableSlots(SlotRange.getPlayerSlots(SlotType.HOTBAR, SlotType.INVENTORY).exclude(SlotType.LOCKED_SLOT), currentStack, inventorySlot, savedStack);
            if (InteractionHandler.getStackFromSlot(inventorySlot).getCount() > savedStack.getCount())
                func.accept(inventorySlot, savedStack, currentStack);
        }
    }
}
