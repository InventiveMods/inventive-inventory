package net.inventive_mods.inventive_inventory.feature.locked_slots;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.util.FileHandler;
import net.inventive_mods.inventive_inventory.util.slot.SlotRange;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@EventBusSubscriber(modid = InventiveInventory.MOD_ID, value = Dist.CLIENT)
public class LockedSlots {
    private static ArrayList<Integer> lockedSlots = new ArrayList<>();
    private static String worldName;
    private static boolean schedulerStarted = false;
    private static boolean ready = false;

    public static final int HOVER_COLOR = 0x66FF0000;
    public static final int LOCKED_HOVER_COLOR = 0xFF8B0000;

    @SubscribeEvent
    public static void setWorldName(EntityJoinLevelEvent event) {
        if (!event.getEntity().equals(InventiveInventory.getPlayer())) return;
        worldName = InventiveInventory.getWorldName();
        schedulerStarted = false;
        ready = false;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onStartTick(ClientTickEvent.Pre event) {
        if (InventiveInventory.getPlayer() == null)
            return;
        if (!schedulerStarted) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            Runnable task = new Runnable() {
                private int iteration = 0;

                @Override
                public void run() {
                    if (iteration > 10) {
                        ready = true;
                        scheduler.shutdown();
                    }
                    iteration++;
                }
            };
            scheduler.scheduleAtFixedRate(task, 0, 50, TimeUnit.MILLISECONDS);
            schedulerStarted = true;
        } else if (ready) {
            init();
        }
    }

    private static void init() {
        JsonElement jsonFile = FileHandler.get(LockedSlotsHandler.LOCKED_SLOTS_PATH);
        JsonArray lockedSlotsJson = new JsonArray();
        lockedSlots.clear();
        if (jsonFile.isJsonObject() && jsonFile.getAsJsonObject().has(worldName)) {
            lockedSlotsJson = jsonFile.getAsJsonObject().getAsJsonArray(worldName);
        }
        for (JsonElement slot : lockedSlotsJson.getAsJsonArray()) {
            lockedSlots.add(slot.getAsInt());
        }
    }

    public static boolean isReady() {
        return schedulerStarted && ready;
    }

    public static void save() {
        JsonArray lockedSlotsJson = new JsonArray();
        for (int lockedSlot : lockedSlots) lockedSlotsJson.add(lockedSlot);
        JsonObject jsonObject = FileHandler.get(LockedSlotsHandler.LOCKED_SLOTS_PATH).isJsonObject() ? FileHandler.get(LockedSlotsHandler.LOCKED_SLOTS_PATH).getAsJsonObject() : new JsonObject();
        jsonObject.remove(worldName);
        jsonObject.add(worldName, lockedSlotsJson);
        FileHandler.write(LockedSlotsHandler.LOCKED_SLOTS_PATH, jsonObject);
    }

    public static List<Integer> get() {
        return adjust();
    }

    public static void add(int slot) {
        lockedSlots = new ArrayList<>(adjust());
        if (!lockedSlots.contains(slot)) {
            lockedSlots.add(slot);
        }
        lockedSlots = new ArrayList<>(unadjust());
        save();
    }

    public static void remove(int slot) {
        lockedSlots = new ArrayList<>(adjust());
        lockedSlots.remove((Integer) slot);
        lockedSlots = new ArrayList<>(unadjust());
        save();
    }

    private static List<Integer> adjust() {
        int start = SlotRange.getPlayerSlots().getFirst();
        return lockedSlots.stream().map(slot -> slot + start).toList();
    }

    private static List<Integer> unadjust() {
        int start = SlotRange.getPlayerSlots().getFirst();
        return lockedSlots.stream().map(slot -> slot - start).toList();
    }
}