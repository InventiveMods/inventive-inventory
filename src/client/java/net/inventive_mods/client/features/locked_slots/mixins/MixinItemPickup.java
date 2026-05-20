package net.inventive_mods.client.features.locked_slots.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.features.locked_slots.LockedSlotsHandler;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Inventory.class)
public class MixinItemPickup {

    @SuppressWarnings("UnresolvedLocalCapture")
    @ModifyExpressionValue(method = "getSlotWithRemainingSpace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;hasRemainingSpaceForItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean canStackAddMoreAndIsNotLockedSlot(boolean original, @Local(name = "i") int i) {
        if (ConfigManager.PICKUP_INTO_LOCKED_SLOTS.is(false)) {
            return original && !LockedSlotsHandler.getLockedSlots().contains(i);
        }
        return original;
    }

    @ModifyExpressionValue(method = "getFreeSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
    private boolean stackIsEmptyAndNotLockedSlot(boolean original, @Local(name = "i") int i) {
        if (ConfigManager.PICKUP_INTO_LOCKED_SLOTS.is(false)) {
            return original && !LockedSlotsHandler.getLockedSlots().contains(i);
        }
        return original;
    }
}
