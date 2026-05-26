package net.inventive_mods.client.util;


import net.minecraft.world.item.ItemStack;

public class ItemStackUtils {
    public static boolean areEqualWithoutCount(ItemStack left, ItemStack right) {
        if (left == right) {
            return true;
        } else {
            return ItemStack.isSameItemSameComponents(left, right);
        }
    }
}
