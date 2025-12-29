package net.inventive_mods.inventive_inventory.features.locked_slots.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerInventory.class)
public class MixinItemPickup {

    @SuppressWarnings("UnresolvedLocalCapture")
    @ModifyExpressionValue(method = "getOccupiedSlotWithRoomForStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;canStackAddMore(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    private boolean canStackAddMoreAndIsNotLockedSlot(boolean original, @Local int i) {
        if (ConfigManager.PICKUP_INTO_LOCKED_SLOTS.is(false)) {
            return original && !LockedSlotsHandler.getLockedSlots().contains(i);
        }
        return original;
    }

    @ModifyExpressionValue(method = "getEmptySlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    private boolean stackIsEmptyAndNotLockedSlot(boolean original, @Local int i) {
        if (ConfigManager.PICKUP_INTO_LOCKED_SLOTS.is(false)) {
            return original && !LockedSlotsHandler.getLockedSlots().contains(i);
        }
        return original;
    }
}
