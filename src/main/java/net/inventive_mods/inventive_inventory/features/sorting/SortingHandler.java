package net.inventive_mods.inventive_inventory.features.sorting;

import com.mojang.blaze3d.platform.InputConstants;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.config.enums.Status;
import net.inventive_mods.inventive_inventory.config.enums.sorting.CursorStackBehaviour;
import net.inventive_mods.inventive_inventory.key.KeyHandler;
import net.inventive_mods.inventive_inventory.util.InteractionHandler;
import net.inventive_mods.inventive_inventory.util.Mouse;
import net.inventive_mods.inventive_inventory.util.ScreenCheck;
import net.inventive_mods.inventive_inventory.util.slot.SlotRange;
import net.inventive_mods.inventive_inventory.util.slot.SlotType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = InventiveInventory.MOD_ID, value = Dist.CLIENT)
public class SortingHandler {
    @SubscribeEvent
    public static void onKeyPressed(ScreenEvent.KeyPressed.Pre event) {
        if (InventiveInventory.getPlayer() != null && InventiveInventory.getPlayer().isCreative() || Config.SORTING_STATUS.is(Status.DISABLED))
            return;
        if (KeyHandler.sortKey.isActiveAndMatches(InputConstants.getKey(event.getKeyCode(), event.getScanCode()))) {
            sort();
        }
    }

    private static void sort() {
        SlotRange slotRange = Mouse.isOverInventory() || !ScreenCheck.isContainerScreen() ? SlotRange.getPlayerSlots().exclude(SlotType.LOCKED_SLOT) : SlotRange.getContainerSlots();
        ItemStack targetStack = InteractionHandler.getCursorStack().copy();

        SortingHelper.mergeItemStacks(slotRange);
        SortingHelper.sortItemStacks(slotRange);

        if (CursorStackBehaviour.isValid())
            SortingHelper.adjustCursorStack(slotRange, targetStack);
    }
}
