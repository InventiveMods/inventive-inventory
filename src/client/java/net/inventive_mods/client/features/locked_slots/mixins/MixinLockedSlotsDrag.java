package net.inventive_mods.client.features.locked_slots.mixins;


import net.inventive_mods.client.context.ContextManager;
import net.inventive_mods.client.context.Contexts;
import net.inventive_mods.client.features.locked_slots.LockedSlotsHandler;
import net.inventive_mods.client.util.mouse.MouseLocation;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public class MixinLockedSlotsDrag {

    @Inject(method = "mouseDragged", at = @At("HEAD"))
    private void onMouseDragged(MouseButtonEvent click, double offsetX, double offsetY, CallbackInfoReturnable<Boolean> cir) {
        if (ContextManager.isLockedSlots() && MouseLocation.getHoveredSlot() != null) {
            LockedSlotsHandler.dragToggle(MouseLocation.getHoveredSlot().index);
        }
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"))
    private void onMouseReleased(MouseButtonEvent click, CallbackInfoReturnable<Boolean> cir) {
        if (ContextManager.isLockedSlots()) ContextManager.setContext(Contexts.INIT);
    }
}
