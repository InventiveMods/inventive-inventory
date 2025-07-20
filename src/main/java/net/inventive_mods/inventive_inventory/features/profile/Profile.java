package net.inventive_mods.inventive_inventory.features.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.inventive_mods.inventive_inventory.util.Converter;
import net.inventive_mods.inventive_inventory.util.InteractionHandler;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class Profile {
    private int id;
    private final ItemStack displayStack;
    private final List<SavedSlot> savedSlots;
    private String name;
    private String key;

    public Profile(int id, String name, String key, List<SavedSlot> savedSlots) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.savedSlots = savedSlots;
        this.displayStack = InteractionHandler.getAnyHandStack().copy();
    }

    public Profile(int id, String name, String key, List<SavedSlot> savedSlots, ItemStack displayStack) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.savedSlots = savedSlots;
        this.displayStack = displayStack.copy();
    }

    public Profile(int id, String name, String key, JsonObject displayStack, JsonArray savedSlots) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.displayStack = Converter.jsonToItemStack(displayStack);
        this.savedSlots = Converter.jsonToSavedSlots(savedSlots);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<SavedSlot> getSavedSlots() {
        return this.savedSlots;
    }

    public ItemStack getDisplayStack() {
        return this.displayStack;
    }

    public JsonObject getAsJsonObject() {
        JsonObject jsonProfile = new JsonObject();
        jsonProfile.addProperty("id", this.id);
        jsonProfile.addProperty("name", this.name);
        jsonProfile.addProperty("key", this.key);
        jsonProfile.add("display_stack", Converter.itemStackToJson(this.displayStack));

        JsonArray jsonArray = new JsonArray();
        for (SavedSlot savedSlot : this.savedSlots) {
            JsonObject savedSlotMap = new JsonObject();
            savedSlotMap.addProperty("slot", savedSlot.slot());
            savedSlotMap.add("stack", Converter.itemStackToJson(savedSlot.stack()));
            jsonArray.add(savedSlotMap);
        }
        jsonProfile.add("saved_slots", jsonArray);
        return jsonProfile;
    }
}
