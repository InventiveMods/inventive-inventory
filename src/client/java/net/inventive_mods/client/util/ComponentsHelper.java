package net.inventive_mods.client.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.Objects;

public class ComponentsHelper {

    public static boolean arePotionsEqual(ItemStack stack, ItemStack otherStack) {
        PotionContents potionContentsComponent = stack.get(DataComponents.POTION_CONTENTS);
        PotionContents otherPotionContentsComponent = otherStack.get(DataComponents.POTION_CONTENTS);
        if (potionContentsComponent == null && otherPotionContentsComponent == null) return true;
        if (potionContentsComponent == null || otherPotionContentsComponent == null) return false;
        if (potionContentsComponent.potion().isEmpty() || otherPotionContentsComponent.potion().isEmpty()) return false;
        return potionContentsComponent.potion().get().getRegisteredName().equals(otherPotionContentsComponent.potion().get().getRegisteredName());
    }

    public static boolean areCustomNamesEqual(ItemStack stack, ItemStack otherStack) {
        var customName = stack.getCustomName();
        return Objects.equals(customName, otherStack.getCustomName());
    }

    public static boolean areEnchantmentsEqual(ItemStack stack, ItemStack otherStack) {
        return stack.getEnchantments().equals(otherStack.getEnchantments());
    }
}
