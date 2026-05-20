package net.inventive_mods.client.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.features.profiles.SavedSlot;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Converter {
    public static JsonObject itemStackToJson(ItemStack stack) {
        JsonObject stackJson = new JsonObject();
        if (stack == null) return stackJson;
        stackJson.addProperty("id", Item.getId(stack.getItem()));

        JsonObject componentsJson = new JsonObject();
        Component customName = stack.get(DataComponents.CUSTOM_NAME);
        if (customName != null) componentsJson.addProperty("custom_name", customName.getString());

        ItemEnchantments enchantmentsComponent = stack.get(DataComponents.ENCHANTMENTS);
        if (enchantmentsComponent != null && !enchantmentsComponent.isEmpty()) {
            JsonArray enchantmentsList = new JsonArray();
            for (Holder<Enchantment> enchantmentRegistryEntry : enchantmentsComponent.keySet()) {
                JsonObject enchantmentComponent = new JsonObject();
                enchantmentComponent.addProperty("id", enchantmentRegistryEntry.getRegisteredName());
                enchantmentComponent.addProperty("lvl", enchantmentsComponent.getLevel(enchantmentRegistryEntry));
                enchantmentsList.add(enchantmentComponent);
            }
            componentsJson.add("enchantments", enchantmentsList);
        }

        PotionContents potionComponent = stack.get(DataComponents.POTION_CONTENTS);
        if (potionComponent != null && potionComponent.potion().isPresent()) {
            componentsJson.addProperty("potion", potionComponent.potion().get().getRegisteredName());
        }

        stackJson.add("components", componentsJson);
        return stackJson;
    }

    public static ItemStack jsonToItemStack(JsonObject stackJson) {
        if (stackJson.get("id") == null) return null;
        ItemStack item = new ItemStack(Item.byId(stackJson.get("id").getAsInt()));
        DataComponentMap.Builder componentBuilder = DataComponentMap.builder();

        if (stackJson.getAsJsonObject("components").has("custom_name")) {
            componentBuilder.set(DataComponents.CUSTOM_NAME, Component.literal(stackJson.getAsJsonObject("components").get("custom_name").getAsString()));
        }

        if (stackJson.getAsJsonObject("components").has("enchantments")) {
            ItemEnchantments.Mutable enchantmentBuilder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            Optional<Registry<Enchantment>> opt = InventiveInventoryClient.getRegistryManager().lookup(Registries.ENCHANTMENT);
            if (opt.isPresent()) {
                Registry<Enchantment> enchantmentRegistry = opt.get();
                for (JsonElement enchantmentElement : stackJson.getAsJsonObject("components").get("enchantments").getAsJsonArray()) {
                    JsonObject enchantmentObject = enchantmentElement.getAsJsonObject();
                    Enchantment enchantment = enchantmentRegistry.getValue(Identifier.withDefaultNamespace(enchantmentObject.get("id").getAsString()));
                    if (enchantment != null) {
                        enchantmentBuilder.set(enchantmentRegistry.wrapAsHolder(enchantment), enchantmentObject.get("lvl").getAsInt());
                    }
                }
                componentBuilder.set(DataComponents.ENCHANTMENTS, enchantmentBuilder.toImmutable());
            }
        }

        if (stackJson.getAsJsonObject("components").has("potion")) {
            Optional<Registry<Potion>> opt = InventiveInventoryClient.getRegistryManager().lookup(Registries.POTION);
            if (opt.isPresent()) {
                Registry<Potion> potionRegistry = opt.get();
                Potion potion = potionRegistry.getValue(Identifier.withDefaultNamespace(stackJson.getAsJsonObject("components").get("potion").getAsString()));
                if (potion != null) {
                    item = PotionContents.createItemStack(Items.POTION, potionRegistry.wrapAsHolder(potion));
                }
            }
        }

        item.applyComponents(componentBuilder.build());
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
