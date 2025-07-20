package net.inventive_mods.inventive_inventory.feature.locked_slots.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.feature.locked_slots.LockedSlots;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerMenu.class)
public class MixinQuickMove {
    @ModifyExpressionValue(method = "moveItemStackTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 1))
    private boolean isEmptyAndLockedSlot(boolean original,  @Local(ordinal = 2) int index) {
        if (InventiveInventory.getMinecraft().isSingleplayer() && Config.QUICK_MOVE_INTO_LOCKED_SLOTS.is(false)) {
            return original || LockedSlots.get().contains(index);
        } return original;
    }

    @ModifyExpressionValue(method = "moveItemStackTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 3))
    private boolean notEmptyAndLockedSlot(boolean original, @Local(ordinal = 2) int index) {
        if (InventiveInventory.getMinecraft().isSingleplayer() && Config.QUICK_MOVE_INTO_LOCKED_SLOTS.is(false)) {
            return original && !LockedSlots.get().contains(index);
        } return original;
    }

    @ModifyExpressionValue(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;hasItem()Z", ordinal = 3))
    private boolean preventPickupAll(boolean original, @Local(ordinal = 1) Slot slot) {
        if (InventiveInventory.getMinecraft().isSingleplayer()) {
            return original && !LockedSlots.get().contains(slot.index);
        } return original;
    }
}
