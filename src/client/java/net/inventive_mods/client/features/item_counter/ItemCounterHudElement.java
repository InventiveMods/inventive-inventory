package net.inventive_mods.client.features.item_counter;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.enums.Status;
import net.inventive_mods.client.config.enums.item_counter.ItemCounterMode;
import net.inventive_mods.client.util.Drawer;
import net.inventive_mods.client.util.InteractionHandler;
import net.inventive_mods.client.util.slots.PlayerSlots;
import net.inventive_mods.client.util.slots.SlotTypes;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class ItemCounterHudElement implements HudElement {
    public static Identifier IDENTIFIER = Identifier.fromNamespaceAndPath(InventiveInventoryClient.MOD_ID, "item_counter_hud_element");

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor context, @NonNull DeltaTracker tickCounter) {
        if (ConfigManager.ITEM_COUNTER_STATUS.is(Status.DISABLED) || (InventiveInventoryClient.getPlayer() != null && InventiveInventoryClient.getPlayer().isCreative()))
            return;

        List<ItemStack> inventory = (ConfigManager.ITEM_COUNTER_IGNORE_LOCKED_SLOTS.is(true) ? PlayerSlots.get().exclude(SlotTypes.LOCKED_SLOT) : PlayerSlots.get()).stream().map(InteractionHandler::getStackFromSlot).toList();
        int slotX = context.guiWidth() / 2 - 90 + 2;
        int slotY = context.guiHeight() - 16 - 3;
        for (Integer slot : PlayerSlots.get(SlotTypes.HOTBAR)) {
            ItemStack stack = InteractionHandler.getStackFromSlot(slot);
            if (!stack.isEmpty() && (ConfigManager.ITEM_COUNTER_MODE.is(ItemCounterMode.COMPLETE_HOTBAR) || ConfigManager.ITEM_COUNTER_MODE.is(ItemCounterMode.ONLY_SELECTED_SLOT) && InteractionHandler.getSelectedSlot() == slot)) {
                int count = inventory.stream()
                        .filter(itemStack -> ItemStack.isSameItem(itemStack, stack))
                        .mapToInt(ItemStack::getCount)
                        .sum();
                Drawer.drawItemCounter(context, slotX, slotY, count, stack);
            }
            slotX += 20;
        }
    }
}
