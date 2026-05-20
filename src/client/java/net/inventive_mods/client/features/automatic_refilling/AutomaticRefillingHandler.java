package net.inventive_mods.client.features.automatic_refilling;

import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.enums.automatic_refilling.ToolReplacementBehaviour;
import net.inventive_mods.client.config.enums.automatic_refilling.ToolReplacementPriority;
import net.inventive_mods.client.features.locked_slots.LockedSlotsHandler;
import net.inventive_mods.client.util.InteractionHandler;
import net.inventive_mods.client.util.slots.PlayerSlots;
import net.inventive_mods.client.util.slots.SlotRange;
import net.inventive_mods.client.util.slots.SlotTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class AutomaticRefillingHandler {
    private static final List<Item> EMPTIES = List.of(Items.BUCKET, Items.GLASS_BOTTLE, Items.BOWL);
    private static final List<Item> BUCKETS = List.of(Items.BUCKET, Items.WATER_BUCKET, Items.LAVA_BUCKET, Items.POWDER_SNOW_BUCKET, Items.MILK_BUCKET, Items.PUFFERFISH_BUCKET, Items.SALMON_BUCKET, Items.COD_BUCKET, Items.TROPICAL_FISH_BUCKET, Items.AXOLOTL_BUCKET, Items.TADPOLE_BUCKET);
    private static ItemStack mainHandStack = ItemStack.EMPTY;
    private static ItemStack offHandStack = ItemStack.EMPTY;
    public static boolean keysPressed = false;
    private static int selectedSlot;
    private static boolean runOffHand = true;

    public static void setMainHandStack(ItemStack stack) {
        mainHandStack = stack.copy();
    }

    public static void setOffHandStack(ItemStack stack) {
        offHandStack = stack.copy();
    }

    public static int getSelectedSlot() {
        return selectedSlot;
    }

    public static void setSelectedSlot(int selectedSlot) {
        AutomaticRefillingHandler.selectedSlot = selectedSlot;
    }

    public static boolean shouldRun() {
        if (!AutomaticRefillingHandler.keysPressed) return false;
        if (mainHandStack.isEmpty() || ItemStack.matches(mainHandStack, InteractionHandler.getMainHandStack()) || mainHandStack.getCount() > 1)
            return false;
        if (ConfigManager.AUTOMATIC_REFILLING_IGNORE_BUCKETS.is(true) && BUCKETS.contains(InteractionHandler.getMainHandStack().getItem()))
            return false;
        return !mainHandStack.isDamageableItem() || ToolReplacementBehaviour.isValid(mainHandStack);
    }

    public static boolean shouldRunOffHand() {
        if (!runOffHand) {
            runOffHand = true;
            return false;
        }
        if (!AutomaticRefillingHandler.keysPressed) return false;
        if (offHandStack.isEmpty() || ItemStack.matches(offHandStack, InteractionHandler.getOffHandStack()) || offHandStack.getCount() > 1)
            return false;
        if (ConfigManager.AUTOMATIC_REFILLING_IGNORE_BUCKETS.is(true) && BUCKETS.contains(InteractionHandler.getOffHandStack().getItem()))
            return false;
        return !offHandStack.isDamageableItem() || ToolReplacementBehaviour.isValid(offHandStack);
    }

    public static void runMainHand() {
        List<Integer> sameItemSlots = getSameItemSlots(mainHandStack);

        int emptiesSlot = InteractionHandler.getSelectedSlot();
        if (!sameItemSlots.isEmpty()) {
            if (InventoryMenu.isHotbarSlot(sameItemSlots.getFirst())) {
                InteractionHandler.setSelectedSlot(sameItemSlots.getFirst() - Inventory.INVENTORY_SIZE);
            } else {
                if (ConfigManager.AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS.is(false) || !LockedSlotsHandler.getLockedSlots().contains(InteractionHandler.getSelectedSlot())) {
                    InteractionHandler.swapStacks(sameItemSlots.getFirst(), InteractionHandler.getSelectedSlot());
                    emptiesSlot = sameItemSlots.getFirst();
                }
            }
        } else runOffHand = false;

        mainHandStack = ItemStack.EMPTY;
        if (EMPTIES.contains(InteractionHandler.getStackFromSlot(emptiesSlot).getItem())) mergeEmpties(emptiesSlot);
    }

    public static void runOffHand() {
        List<Integer> sameItemSlots = getSameItemSlots(offHandStack);

        int emptiesSlot = InventoryMenu.SHIELD_SLOT;
        if (!sameItemSlots.isEmpty()) {
            InteractionHandler.swapStacks(sameItemSlots.getFirst(), InventoryMenu.SHIELD_SLOT);
            emptiesSlot = sameItemSlots.getFirst();
        }

        offHandStack = ItemStack.EMPTY;
        if (EMPTIES.contains(InteractionHandler.getStackFromSlot(emptiesSlot).getItem())) mergeEmpties(emptiesSlot);
    }

    private static List<Integer> getSameItemSlots(ItemStack handStack) {
        SlotRange slotRange = PlayerSlots.get().append(SlotTypes.HOTBAR).exclude(InteractionHandler.getSelectedSlot());
        slotRange = ConfigManager.AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS.is(true) ? slotRange.exclude(SlotTypes.LOCKED_SLOT) : slotRange;
        Stream<Integer> sameItemSlotsStream = slotRange.stream()
                .filter(slot -> {
                    ItemStack stack = InteractionHandler.getStackFromSlot(slot);
                    if (handStack.isDamageableItem()) {
                        if (isPlainItem(handStack) && isPlainItem(stack)) {
                            if (getToolType(handStack).equals(getToolType(stack))) {
                                return ((ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR.is(ToolReplacementBehaviour.KEEP_TOOL) && stack.getMaxDamage() - stack.getDamageValue() > 1) || ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR.is(ToolReplacementBehaviour.BREAK_TOOL));
                            }
                            return false;
                        }
                        return ItemStack.isSameItem(stack, handStack) &&
                                ((ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR.is(ToolReplacementBehaviour.KEEP_TOOL) && stack.getMaxDamage() - stack.getDamageValue() > 1) || ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR.is(ToolReplacementBehaviour.BREAK_TOOL));
                    }
                    return ItemStack.isSameItem(handStack, stack);
                });

        if (handStack.isDamageableItem()) {
            if (ConfigManager.TOOL_REPLACEMENT_PRIORITY.is(ToolReplacementPriority.MATERIAL)) {
                sameItemSlotsStream = sameItemSlotsStream
                        .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getMaxDamage(), Comparator.reverseOrder()));
            } else if (ConfigManager.TOOL_REPLACEMENT_PRIORITY.is(ToolReplacementPriority.DURABILITY)) {
                sameItemSlotsStream = sameItemSlotsStream
                        .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getMaxDamage() - InteractionHandler.getStackFromSlot(slot).getDamageValue()));
            }
        } else {
            sameItemSlotsStream = sameItemSlotsStream
                    .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount()));
        }

        List<Integer> sameItemSlots = new ArrayList<>(sameItemSlotsStream.toList());
        SlotRange hotbarSlotRange = SlotRange.of(sameItemSlots).exclude(SlotTypes.INVENTORY);
        SlotRange inventorySlotRange = SlotRange.of(sameItemSlots).exclude(SlotTypes.HOTBAR);
        sameItemSlots.removeAll(!hotbarSlotRange.isEmpty() ? inventorySlotRange : hotbarSlotRange);
        return sameItemSlots;
    }

    private static void mergeEmpties(int itemSlot) {
        SlotRange slotRange = PlayerSlots.get().append(SlotTypes.HOTBAR).exclude(itemSlot);
        slotRange = ConfigManager.AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS.is(true) ? slotRange.exclude(SlotTypes.LOCKED_SLOT) : slotRange;
        List<Integer> sameItemSlots = slotRange.stream()
                .filter(slot -> InteractionHandler.getStackFromSlot(slot).getItem().equals(InteractionHandler.getStackFromSlot(itemSlot).getItem()))
                .filter(slot -> InteractionHandler.getStackFromSlot(slot).getCount() < InteractionHandler.getStackFromSlot(slot).getMaxStackSize())
                .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount(), Comparator.reverseOrder()))
                .toList();
        if (!sameItemSlots.isEmpty()) {
            InteractionHandler.leftClickStack(itemSlot);
            InteractionHandler.leftClickStack(sameItemSlots.getFirst());
        }
    }

    private static String getToolType(ItemStack stack) {
        String[] id = stack.getItem().getDescriptionId().split(":");
        String itemId = id[id.length - 1];
        String[] parts = itemId.split("_");
        return parts[parts.length - 1];
    }

    private static boolean isPlainItem(ItemStack stack) {
        return stack.getItem().getClass().equals(Item.class);
    }

    public static void reset() {
        mainHandStack = ItemStack.EMPTY;
        offHandStack = ItemStack.EMPTY;
    }
}
