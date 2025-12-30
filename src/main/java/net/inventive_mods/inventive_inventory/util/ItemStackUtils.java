package net.inventive_mods.inventive_inventory.util;

import net.minecraft.item.ItemStack;

public class ItemStackUtils {
    public static boolean areEqualWithoutCount(ItemStack left, ItemStack right) {
        if (left == right) {
            return true;
        } else {
            return ItemStack.canCombine(left, right);
        }
    }
}
