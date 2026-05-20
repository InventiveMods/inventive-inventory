package net.inventive_mods.client.features.locked_slots.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.features.locked_slots.LockedSlotsHandler;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerMenu.class)
public class MixinItemQuickMove {

    @ModifyExpressionValue(method = "moveItemStackTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 1))
    private boolean isEmptyAndLockedSlot(boolean original, @Local(name = "destSlot") int destSlot) {
        if (InventiveInventoryClient.getClient().isSingleplayer() && ConfigManager.QUICK_MOVE_INTO_LOCKED_SLOTS.is(false)) {
            return original || LockedSlotsHandler.getLockedSlots().contains(destSlot);
        }
        return original;
    }

    @ModifyExpressionValue(method = "moveItemStackTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 3))
    private boolean notEmptyAndLockedSlot(boolean original, @Local(name = "destSlot") int destSlot) {
        if (InventiveInventoryClient.getClient().isSingleplayer() && ConfigManager.QUICK_MOVE_INTO_LOCKED_SLOTS.is(false)) {
            return original && !LockedSlotsHandler.getLockedSlots().contains(destSlot);
        }
        return original;
    }

    @ModifyExpressionValue(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;hasItem()Z", ordinal = 3))
    private boolean preventPickupAll(boolean original, @Local(name = "target") Slot target) {
        if (InventiveInventoryClient.getClient().isSingleplayer()) {
            return original && !LockedSlotsHandler.getLockedSlots().contains(target.index);
        }
        return original;
    }
}
