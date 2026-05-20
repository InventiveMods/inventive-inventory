package net.inventive_mods.client.features.locked_slots.mixins;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.context.ContextManager;
import net.inventive_mods.client.features.locked_slots.LockedSlotsHandler;
import net.inventive_mods.client.keys.handler.AdvancedOperationHandler;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class MixinLockedSlotsToggle {

    @Inject(method = "handleContainerInput", at = @At("HEAD"), cancellable = true)
    private void toggleLockedSlots(int containerId, int slotNum, int buttonNum, ContainerInput containerInput, Player player, CallbackInfo ci) {
        if (AdvancedOperationHandler.isPressed() && buttonNum == 0 && containerInput == ContainerInput.PICKUP && ContextManager.isInit()) {
            if (!InventiveInventoryClient.getPlayer().isCreative()) {
                LockedSlotsHandler.toggle(slotNum);
                ci.cancel();
            }
        }
    }
}

