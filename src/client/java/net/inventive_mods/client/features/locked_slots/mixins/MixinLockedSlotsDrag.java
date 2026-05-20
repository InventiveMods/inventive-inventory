package net.inventive_mods.client.features.locked_slots.mixins;


import net.inventive_mods.client.context.ContextManager;
import net.inventive_mods.client.context.Contexts;
import net.inventive_mods.client.features.locked_slots.LockedSlotsHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.inventory.Slot;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public class MixinLockedSlotsDrag {

    @Shadow
    @Nullable
    protected Slot hoveredSlot;

    @Inject(method = "mouseDragged", at = @At("HEAD"))
    private void onMouseDragged(MouseButtonEvent event, double dx, double dy, CallbackInfoReturnable<Boolean> cir) {
        if (ContextManager.isLockedSlots() && this.hoveredSlot != null) {
            LockedSlotsHandler.dragToggle(this.hoveredSlot.index);
        }
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"))
    private void onMouseReleased(MouseButtonEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (ContextManager.isLockedSlots()) ContextManager.setContext(Contexts.INIT);
    }
}
