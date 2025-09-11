package net.inventive_mods.inventive_inventory.features.item_counter;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.config.enums.Status;
import net.inventive_mods.inventive_inventory.config.enums.item_counter.ItemCounterMode;
import net.inventive_mods.inventive_inventory.util.Drawer;
import net.inventive_mods.inventive_inventory.util.InteractionHandler;
import net.inventive_mods.inventive_inventory.util.slots.PlayerSlots;
import net.inventive_mods.inventive_inventory.util.slots.SlotTypes;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.LecternScreenHandler;

import java.util.List;

public class ItemCounterHudElement implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext context, float tickCounter) {
        if (ConfigManager.ITEM_COUNTER_STATUS.is(Status.DISABLED) || (InventiveInventory.getPlayer() != null && InventiveInventory.getPlayer().isCreative()) || InventiveInventory.getScreenHandler() instanceof LecternScreenHandler)
            return;

        List<ItemStack> inventory = (ConfigManager.ITEM_COUNTER_IGNORE_LOCKED_SLOTS.is(true) ? PlayerSlots.get().exclude(SlotTypes.LOCKED_SLOT) : PlayerSlots.get()).stream().map(InteractionHandler::getStackFromSlot).toList();
        int slotX = context.getScaledWindowWidth() / 2 - 90 + 2;
        int slotY = context.getScaledWindowHeight() - 16 - 3;
        for (Integer slot : PlayerSlots.get(SlotTypes.HOTBAR)) {
            ItemStack stack = InteractionHandler.getStackFromSlot(slot);
            if (!stack.isEmpty() && (ConfigManager.ITEM_COUNTER_MODE.is(ItemCounterMode.COMPLETE_HOTBAR) || ConfigManager.ITEM_COUNTER_MODE.is(ItemCounterMode.ONLY_SELECTED_SLOT) && InteractionHandler.getSelectedSlot() == slot)) {
                int count = inventory.stream()
                        .filter(itemStack -> ItemStack.areItemsEqual(itemStack, stack))
                        .mapToInt(ItemStack::getCount)
                        .sum();
                Drawer.drawItemCounter(context, slotX, slotY, count, stack);
            }
            slotX += 20;
        }
    }
}
