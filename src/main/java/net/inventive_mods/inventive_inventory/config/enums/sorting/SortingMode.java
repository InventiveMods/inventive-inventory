package net.inventive_mods.inventive_inventory.config.enums.sorting;

import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Translatable;
import net.inventive_mods.inventive_inventory.util.InteractionHandler;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Comparator;
import java.util.stream.Collectors;

public enum SortingMode implements Translatable {
    NAME("name", Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getHoverName().getString())),
    @SuppressWarnings("unused") ITEM_TYPE("item_type", Comparator.comparing(slot -> Item.getId(InteractionHandler.getStackFromSlot(slot).getItem())));

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
                    ItemEnchantments enchantments = stack.get(DataComponents.STORED_ENCHANTMENTS);
                    if (enchantments == null)
                        enchantments = stack.get(DataComponents.ENCHANTMENTS);
                    if (enchantments != null) {
                        ItemEnchantments finalEnchantments = enchantments;
                        return enchantments.keySet()
                                .stream()
                                .map(entry -> Enchantment.getFullname(entry, 0).getString() + " " + finalEnchantments.getLevel(entry))
                                .collect(Collectors.joining(", "));
                    }
                    return "";
                });
    }



    @Override
    public Component getButtonText() {
        return Component.translatable(Config.OPTION_TRANSLATION_KEY + "." + this.translationKey);
    }

    @Override
    public String getTranslationKey() {
        return this.translationKey;
    }
}
