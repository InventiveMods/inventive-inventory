package net.inventive_mods.inventive_inventory.config.enums.sorting;

import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Translatable;
import net.inventive_mods.inventive_inventory.util.InteractionHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.util.Comparator;
import java.util.stream.Collectors;

public enum SortingMode implements Translatable {
    NAME("name", Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getName().getString())),
    @SuppressWarnings("unused") ITEM_TYPE("item_type", Comparator.comparing(slot -> Item.getRawId(InteractionHandler.getStackFromSlot(slot).getItem())));

    private final String translationKey;
    private final Comparator<Integer> comparator;

    SortingMode(String translationKey, Comparator<Integer> comparator) {
        this.translationKey = "sorting.mode." + translationKey;
        this.comparator = comparator;
    }

    public Comparator<Integer> getComparator() {
        return this.comparator
                .thenComparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount(), Comparator.reverseOrder())
                .thenComparing(slot -> {
                    ItemStack stack = InteractionHandler.getStackFromSlot(slot);
                    ItemEnchantmentsComponent component = stack.get(DataComponentTypes.STORED_ENCHANTMENTS);
                    if (component == null) component = stack.get(DataComponentTypes.ENCHANTMENTS);
                    if (component != null) {
                        ItemEnchantmentsComponent finalComponent = component;
                        return component.getEnchantments()
                                .stream()
                                .map(entry -> {
                                    if (entry.getKey().isPresent()) {
                                        Enchantment enchantment = Registries.ENCHANTMENT.get(entry.getKey().get());
                                        if (enchantment != null) return enchantment.getName(0).getString() + " " + finalComponent.getLevel(enchantment);
                                    } return "";
                                })
                                .collect(Collectors.joining(", "));
                    }
                    return "";
                });
    }

    @Override
    public Text getButtonText() {
        return Text.translatable(ConfigManager.OPTION_TRANSLATION_KEY + "." + this.translationKey);
    }

    @Override
    public String getTranslationKey() {
        return this.translationKey;
    }
}
