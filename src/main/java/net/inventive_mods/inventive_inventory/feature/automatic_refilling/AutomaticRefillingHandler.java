package net.inventive_mods.inventive_inventory.feature.automatic_refilling;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.config.enums.Status;
import net.inventive_mods.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingMode;
import net.inventive_mods.inventive_inventory.config.enums.automatic_refilling.ToolReplacementBehaviour;
import net.inventive_mods.inventive_inventory.config.enums.automatic_refilling.ToolReplacementPriority;
import net.inventive_mods.inventive_inventory.feature.locked_slots.LockedSlots;
import net.inventive_mods.inventive_inventory.util.InteractionHandler;
import net.inventive_mods.inventive_inventory.util.slot.SlotRange;
import net.inventive_mods.inventive_inventory.util.slot.SlotType;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@EventBusSubscriber(modid = InventiveInventory.MOD_ID, value = Dist.CLIENT)
public class AutomaticRefillingHandler {
    private static final List<Item> EMPTIES = List.of(Items.BUCKET, Items.GLASS_BOTTLE, Items.BOWL);
    private static ItemStack mainHandStack = ItemStack.EMPTY;
    private static ItemStack offHandStack = ItemStack.EMPTY;
    public static boolean keysPressed = false;
    private static int selectedSlot;
    private static boolean runOffHand = true;

    @SubscribeEvent
    public static void onStartTickEvent(ClientTickEvent.Pre event) {
        if (InventiveInventory.getPlayer() == null || InventiveInventory.getPlayer().isCreative())
            return;

        Options options = InventiveInventory.getMinecraft().options;
        if (options.keyAttack.isDown() || options.keyUse.isDown() || options.keyDrop.isDown()) {
            keysPressed = true;
        } else if (!options.keyAttack.isDown() && !options.keyUse.isDown() && !options.keyDrop.isDown()) {
            keysPressed = false;
        }

        if (selectedSlot != InteractionHandler.getSelectedSlot()) {
            reset();
        }

        automaticRefilling();
    }

    @SubscribeEvent
    public static void onEndTickEvent(ClientTickEvent.Post event) {
        if (InventiveInventory.getPlayer() == null || InventiveInventory.getPlayer().isCreative())
            return;

        automaticRefilling();

        LocalPlayer player = InventiveInventory.getPlayer();
        mainHandStack = player.getMainHandItem().copy();
        offHandStack = player.getOffhandItem().copy();
        selectedSlot = InteractionHandler.getSelectedSlot();
    }

    @SubscribeEvent
    public static void onPlayerJoinEvent(EntityJoinLevelEvent event) {
        if (!event.getEntity().equals(InventiveInventory.getPlayer())) return;

        reset();
    }

    public static void automaticRefilling() {
        if (AutomaticRefillingMode.isValid() && Config.AUTOMATIC_REFILLING_STATUS.is(Status.ENABLED) && shouldRun()) {
            runMainHand();
        }
        if (AutomaticRefillingMode.isValid() && Config.AUTOMATIC_REFILLING_STATUS.is(Status.ENABLED) && shouldRunOffHand()) {
            runOffHand();
        }
    }

    public static boolean shouldRun() {
        if (!keysPressed) return false;
        if (mainHandStack.isEmpty() || ItemStack.matches(mainHandStack, InteractionHandler.getMainHandStack()) || mainHandStack.getCount() > 1)
            return false;
        return !mainHandStack.isDamageableItem() || ToolReplacementBehaviour.isValid(mainHandStack);
    }

    public static boolean shouldRunOffHand() {
        if (!runOffHand) {
            runOffHand = true;
            return false;
        }
        if (!keysPressed) return false;
        if (offHandStack.isEmpty() || ItemStack.matches(offHandStack, InteractionHandler.getOffHandStack()) || offHandStack.getCount() > 1)
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
                if (Config.AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS.is(false) || !LockedSlots.get().contains(InteractionHandler.getSelectedSlot())) {
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
        SlotRange slotRange = SlotRange.getPlayerSlots().append(SlotType.HOTBAR).exclude(InteractionHandler.getSelectedSlot());
        slotRange = Config.AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS.is(true) ? slotRange.exclude(SlotType.LOCKED_SLOT) : slotRange;
        Stream<Integer> sameItemSlotsStream = slotRange.stream()
                .filter(slot -> {
                    ItemStack stack = InteractionHandler.getStackFromSlot(slot);
                    if (handStack.isDamageableItem()) {
                        if (isPlainItem(handStack) && isPlainItem(stack)) {
                            if (getToolType(handStack).equals(getToolType(stack))) {
                                return ((Config.TOOL_REPLACEMENT_BEHAVIOUR.is(ToolReplacementBehaviour.KEEP_TOOL) && stack.getMaxDamage() - stack.getDamageValue() > 1) || Config.TOOL_REPLACEMENT_BEHAVIOUR.is(ToolReplacementBehaviour.BREAK_TOOL));
                            }
                            return false;
                        }
                        return ItemStack.isSameItem(stack, handStack) &&
                                ((Config.TOOL_REPLACEMENT_BEHAVIOUR.is(ToolReplacementBehaviour.KEEP_TOOL) && stack.getMaxDamage() - stack.getDamageValue() > 1) || Config.TOOL_REPLACEMENT_BEHAVIOUR.is(ToolReplacementBehaviour.BREAK_TOOL));
                    }
                    return ItemStack.isSameItem(handStack, stack);
                });

        if (handStack.isDamageableItem()) {
            if (Config.TOOL_REPLACEMENT_PRIORITY.is(ToolReplacementPriority.MATERIAL)) {
                sameItemSlotsStream = sameItemSlotsStream
                        .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getMaxDamage(), Comparator.reverseOrder()));
            } else if (Config.TOOL_REPLACEMENT_PRIORITY.is(ToolReplacementPriority.DURABILITY)) {
                sameItemSlotsStream = sameItemSlotsStream
                        .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getMaxDamage() - InteractionHandler.getStackFromSlot(slot).getDamageValue()));
            }
        } else {
            sameItemSlotsStream = sameItemSlotsStream
                    .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount()));
        }

        List<Integer> sameItemSlots = new ArrayList<>(sameItemSlotsStream.toList());
        SlotRange hotbarSlotRange = SlotRange.of(sameItemSlots).exclude(SlotType.INVENTORY);
        SlotRange inventorySlotRange = SlotRange.of(sameItemSlots).exclude(SlotType.HOTBAR);
        sameItemSlots.removeAll(!hotbarSlotRange.isEmpty() ? inventorySlotRange : hotbarSlotRange);
        return sameItemSlots;
    }

    private static void mergeEmpties(int itemSlot) {
        SlotRange slotRange = SlotRange.getPlayerSlots().append(SlotType.HOTBAR).exclude(itemSlot);
        slotRange = Config.AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS.is(true) ? slotRange.exclude(SlotType.LOCKED_SLOT) : slotRange;
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
        String[] id = stack.getItemHolder().getRegisteredName().split(":");
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
