package net.inventive_mods.inventive_inventory.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.features.profiles.SavedSlot;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Converter {
    public static JsonObject itemStackToJson(ItemStack stack) {
        JsonObject stackJson = new JsonObject();
        if (stack == null) return stackJson;
        stackJson.addProperty("id", Item.getRawId(stack.getItem()));

        JsonObject componentsJson = new JsonObject();
        Text customName = stack.get(DataComponentTypes.CUSTOM_NAME);
        if (customName != null) componentsJson.addProperty("custom_name", customName.getString());

        ItemEnchantmentsComponent enchantmentsComponent = stack.get(DataComponentTypes.ENCHANTMENTS);
        if (enchantmentsComponent != null && !enchantmentsComponent.isEmpty()) {
            JsonArray enchantmentsList = new JsonArray();
            Registry<Enchantment> enchantmentRegistry = InventiveInventory.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
            for (RegistryEntry<Enchantment> enchantmentRegistryEntry : enchantmentsComponent.getEnchantments().stream().toList()) {
                JsonObject enchantmentComponent = new JsonObject();
                Enchantment enchantment = enchantmentRegistry.get(new Identifier(enchantmentRegistryEntry.getIdAsString()));
                enchantmentComponent.addProperty("id", enchantmentRegistryEntry.getIdAsString());
                enchantmentComponent.addProperty("lvl", enchantmentsComponent.getLevel(enchantment));
                enchantmentsList.add(enchantmentComponent);
            }
            componentsJson.add("enchantments", enchantmentsList);
        }

        PotionContentsComponent potionComponent = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionComponent != null && potionComponent.potion().isPresent()) {
            componentsJson.addProperty("potion", potionComponent.potion().get().getIdAsString());
        }

        stackJson.add("components", componentsJson);
        return stackJson;
    }

    public static ItemStack jsonToItemStack(JsonObject stackJson) {
        if (stackJson.get("id") == null) return null;
        ItemStack item = new ItemStack(RegistryEntry.of(Item.byRawId(stackJson.get("id").getAsInt())));
        ComponentMap.Builder componentBuilder = ComponentMap.builder();

        if (stackJson.getAsJsonObject("components").has("custom_name")) {
            componentBuilder.add(DataComponentTypes.CUSTOM_NAME, Text.of(stackJson.getAsJsonObject("components").get("custom_name").getAsString()));
        }

        if (stackJson.getAsJsonObject("components").has("enchantments")) {
            ItemEnchantmentsComponent.Builder enchantmentBuilder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
            Registry<Enchantment> enchantmentRegistry = InventiveInventory.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
            for (JsonElement enchantmentElement : stackJson.getAsJsonObject("components").get("enchantments").getAsJsonArray()) {
                JsonObject enchantmentObject = enchantmentElement.getAsJsonObject();
                Enchantment enchantment = enchantmentRegistry.get(new Identifier(enchantmentObject.get("id").getAsString()));
                enchantmentBuilder.add(enchantment, enchantmentObject.get("lvl").getAsInt());
            }
            componentBuilder.add(DataComponentTypes.ENCHANTMENTS, enchantmentBuilder.build());
        }

        if (stackJson.getAsJsonObject("components").has("potion")) {
            Registry<Potion> potionRegistry = InventiveInventory.getRegistryManager().get(RegistryKeys.POTION);
            Potion potion = potionRegistry.get(new Identifier(stackJson.getAsJsonObject("components").get("potion").getAsString()));
            item = PotionContentsComponent.createStack(Items.POTION, potionRegistry.getEntry(potion));
        }

        item.applyComponentsFrom(componentBuilder.build());
        return item;
    }

    public static List<SavedSlot> jsonToSavedSlots(JsonArray savedSlotsJson) {
        List<SavedSlot> savedSlotList = new ArrayList<>();
        for (JsonElement savedSlotElement : savedSlotsJson) {
            JsonObject savedSlotObject = savedSlotElement.getAsJsonObject();
            int slot = savedSlotObject.get("slot").getAsInt();
            ItemStack stack = Converter.jsonToItemStack(savedSlotObject.getAsJsonObject("stack"));
            savedSlotList.add(new SavedSlot(slot, stack));
        }
        return savedSlotList;
    }
}
