package net.inventive_mods.inventive_inventory.features.locked_slots;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.context.ContextManager;
import net.inventive_mods.inventive_inventory.context.Contexts;
import net.inventive_mods.inventive_inventory.util.FileHandler;
import net.inventive_mods.inventive_inventory.util.InteractionHandler;
import net.inventive_mods.inventive_inventory.util.ItemStackUtils;
import net.inventive_mods.inventive_inventory.util.ScreenCheck;
import net.inventive_mods.inventive_inventory.util.slots.PlayerSlots;
import net.inventive_mods.inventive_inventory.util.slots.SlotTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class LockedSlotsHandler {
    private static final String LOCKED_SLOTS_FILE = "locked_slots.json";
    public static final Path LOCKED_SLOTS_PATH = ConfigManager.CONFIG_PATH.resolve(LOCKED_SLOTS_FILE);
    private static final List<ItemStack> savedInventory = new ArrayList<>();
    private static final List<ItemStack> savedHandlerInventory = new ArrayList<>();
    public static boolean shouldAdd;
    public static boolean shouldInit;
    public static boolean schedulerStarted;
    private static LockedSlots lockedSlots = LockedSlots.empty();

    public static final int HOVER_COLOR = 0x66FF0000;
    public static final int LOCKED_HOVER_COLOR = 0xFF8B0000;

    public static void toggle(int slot) {
        ContextManager.setContext(Contexts.LOCKED_SLOTS);
        if (PlayerSlots.get().append(SlotTypes.HOTBAR).contains(slot)) {
            lockedSlots = getLockedSlots();
            if (lockedSlots.contains(slot)) {
                lockedSlots.remove(((Integer) slot));
                shouldAdd = false;
            } else {
                lockedSlots.add(slot);
                shouldAdd = true;
            }
            save();
        }
    }

    public static void dragToggle(int slot) {
        if (PlayerSlots.get().append(SlotTypes.HOTBAR).contains(slot)) {
            lockedSlots = getLockedSlots();
            if (shouldAdd) {
                if (!lockedSlots.contains(slot)) lockedSlots.add(slot);
            } else lockedSlots.remove(Integer.valueOf(slot));
            save();
        }
    }

    public static void init() {
        reset();
        shouldInit = false;
        JsonElement jsonFile = FileHandler.get(LOCKED_SLOTS_PATH);
        JsonArray lockedSlotsJson = new JsonArray();
        if (jsonFile.isJsonObject() && jsonFile.getAsJsonObject().has(InventiveInventory.getWorldName())) {
            lockedSlotsJson = jsonFile.getAsJsonObject().getAsJsonArray(InventiveInventory.getWorldName());
        }
        for (JsonElement slot : lockedSlotsJson.getAsJsonArray()) {
            lockedSlots.add(slot.getAsInt());
        }
    }

    public static void reset() {
        lockedSlots.clear();
        savedInventory.clear();
        savedHandlerInventory.clear();
    }

    public static LockedSlots getLockedSlots() {
        return lockedSlots.adjust();
    }

    public static void adjustInventory() {
        List<ItemStack> currentInventory = new ArrayList<>(InventiveInventory.getPlayer().getInventory().getMainStacks());
        if (savedInventory.isEmpty()) return;
        boolean itemTaken = isItemTaken(currentInventory);
        boolean itemMoved = isItemMoved(itemTaken);

        ContextManager.setContext(Contexts.LOCKED_SLOTS);
        if (ConfigManager.PICKUP_INTO_LOCKED_SLOTS.is(false) && itemTaken && !itemMoved) {
            rearrange(currentInventory, InteractionHandler::dropItem);
        } else if (ConfigManager.QUICK_MOVE_INTO_LOCKED_SLOTS.is(false) && itemMoved) {
            rearrange(currentInventory, (slot, times) -> InteractionHandler.quickMove(slot));
        }
        ContextManager.setContext(Contexts.INIT);
    }

    public static void setSavedInventory() {
        savedInventory.clear();
        for (ItemStack stack : InventiveInventory.getPlayer().getInventory().getMainStacks())
            savedInventory.add(stack.copy());
        savedInventory.add(InteractionHandler.getCursorStack().copy());
    }

    public static void setSavedHandlerInventory() {
        savedHandlerInventory.clear();
        if (!ScreenCheck.isNone()) {
            for (int i = 0; i < InventiveInventory.getScreenHandler().slots.size(); i++) {
                savedHandlerInventory.add(InteractionHandler.getStackFromSlot(i).copy());
            }
        }
    }

    private static Map<Item, Integer> getItemCountMap(List<ItemStack> inventory) {
        return inventory.stream().collect(Collectors.toMap(ItemStack::getItem, ItemStack::getCount, Integer::sum));
    }

    private static boolean isItemTaken(List<ItemStack> currentInventory) {
        Map<Item, Integer> currentCountMap = getItemCountMap(currentInventory);
        Map<Item, Integer> savedCountMap = getItemCountMap(savedInventory);
        return currentCountMap.entrySet().stream()
                .anyMatch(entry -> entry.getValue() > savedCountMap.getOrDefault(entry.getKey(), 0));
    }

    private static boolean isItemMoved(boolean tookItem) {
        List<ItemStack> currentHandlerInventory = new ArrayList<>();
        for (int i = 0; i < InventiveInventory.getScreenHandler().slots.size(); i++) {
            currentHandlerInventory.add(InteractionHandler.getStackFromSlot(i).copy());
        }
        Map<Item, Integer> currentCountMap = getItemCountMap(currentHandlerInventory);
        Map<Item, Integer> savedCountMap = getItemCountMap(savedHandlerInventory);
        return tookItem && currentCountMap.entrySet().stream()
                .allMatch(entry -> entry.getValue().equals(savedCountMap.getOrDefault(entry.getKey(), 0)));
    }

    private static void rearrange(List<ItemStack> currentInventory, BiConsumer<Integer, Integer> func) {
        if (savedInventory.isEmpty()) return;
        LockedSlots lockedSlots = LockedSlotsHandler.getLockedSlots();
        int i = 0;
        for (int invSlot : PlayerSlots.get(SlotTypes.HOTBAR).append(SlotTypes.INVENTORY)) {
            ItemStack currentStack = currentInventory.get(i);
            ItemStack savedStack = savedInventory.get(i);
            i++;
            if (!lockedSlots.contains(invSlot) || ItemStack.areEqual(currentStack, savedStack)) continue;
            List<Integer> suitableSlots = PlayerSlots.get(SlotTypes.HOTBAR).append(SlotTypes.INVENTORY).exclude(SlotTypes.LOCKED_SLOT).stream()
                    .filter(slot -> {
                        ItemStack stack = InteractionHandler.getStackFromSlot(slot);
                        return stack.isEmpty() || ItemStackUtils.areEqualWithoutCount(stack, currentStack) && stack.getCount() < stack.getMaxCount();
                    })
                    .sorted(Comparator.comparing((Integer slot) -> InteractionHandler.getStackFromSlot(slot).getCount(), Comparator.reverseOrder()))
                    .toList();
            if (!suitableSlots.isEmpty()) {
                InteractionHandler.leftClickStack(invSlot);
                for (int slot : suitableSlots) {
                    ItemStack stack = InteractionHandler.getStackFromSlot(slot);
                    while (InteractionHandler.getCursorStack().getCount() > savedStack.getCount()) {
                        if (stack.getCount() < stack.getMaxCount()) InteractionHandler.rightClickStack(slot);
                        else break;
                    }
                }
                InteractionHandler.leftClickStack(invSlot);
            }
            if (InteractionHandler.getStackFromSlot(invSlot).getCount() > savedStack.getCount()) {
                func.accept(invSlot, InteractionHandler.getStackFromSlot(invSlot).getCount() - savedStack.getCount());
            }
        }
    }

    private static void save() {
        lockedSlots = lockedSlots.unadjust();
        JsonArray lockedSlotsJson = new JsonArray();
        for (int lockedSlot : lockedSlots) lockedSlotsJson.add(lockedSlot);
        JsonObject jsonObject = FileHandler.get(LOCKED_SLOTS_PATH).isJsonObject() ? FileHandler.get(LOCKED_SLOTS_PATH).getAsJsonObject() : new JsonObject();
        jsonObject.remove(InventiveInventory.getWorldName());
        jsonObject.add(InventiveInventory.getWorldName(), lockedSlotsJson);
        FileHandler.write(LOCKED_SLOTS_PATH, jsonObject);
    }

    public static void startScheduler() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = new Runnable() {
            private int iteration = 0;

            @Override
            public void run() {
                if (iteration > 10) {
                    LockedSlotsHandler.shouldInit = true;
                    scheduler.shutdown();
                }
                iteration++;
            }
        };
        scheduler.scheduleAtFixedRate(task, 0, 50, TimeUnit.MILLISECONDS);
        schedulerStarted = true;
    }
}
