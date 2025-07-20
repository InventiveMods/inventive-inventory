package net.inventive_mods.inventive_inventory.features.locked_slots;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.util.FileHandler;
import net.inventive_mods.inventive_inventory.util.slot.SlotRange;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = InventiveInventory.MOD_ID, value = Dist.CLIENT)
public class LockedSlots {
    private static ArrayList<Integer> lockedSlots = new ArrayList<>();
    private static String worldName;

    public static final int HOVER_COLOR = 0x66FF0000;
    public static final int LOCKED_HOVER_COLOR = 0xFF8B0000;

    @SubscribeEvent
    public static void init(EntityJoinLevelEvent event) {
        if (!event.getEntity().equals(InventiveInventory.getPlayer())) return;
        JsonElement jsonFile = FileHandler.get(LockedSlotsHandler.LOCKED_SLOTS_PATH);
        JsonArray lockedSlotsJson = new JsonArray();
        worldName = InventiveInventory.getWorldName();
        lockedSlots.clear();
        if (jsonFile.isJsonObject() && jsonFile.getAsJsonObject().has(worldName)) {
            lockedSlotsJson = jsonFile.getAsJsonObject().getAsJsonArray(worldName);
        }
        for (JsonElement slot : lockedSlotsJson.getAsJsonArray()) {
            lockedSlots.add(slot.getAsInt());
        }
    }

    @SubscribeEvent
    public static void save(EntityLeaveLevelEvent event) {
        if (!event.getEntity().equals(InventiveInventory.getPlayer())) return;
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
    }

    public static void remove(int slot) {
        lockedSlots = new ArrayList<>(adjust());
        lockedSlots.remove((Integer) slot);
        lockedSlots = new ArrayList<>(unadjust());
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