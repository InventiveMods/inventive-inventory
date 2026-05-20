package net.inventive_mods.client.keys.mixins;


import net.inventive_mods.client.context.ContextManager;
import net.inventive_mods.client.features.sorting.SortingHandler;
import net.inventive_mods.client.keys.KeyRegistry;
import net.inventive_mods.client.keys.handler.AdvancedOperationHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public class MixinKeyInputHandler {
    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void onKeyPressed(KeyEvent input, CallbackInfoReturnable<Boolean> cir) {
        if (KeyRegistry.advancedOperationKey.matches(input)) {
            AdvancedOperationHandler.setPressed(true);
        }
        if (KeyRegistry.sortKey.matches(input) && ContextManager.isInit()) {
            SortingHandler.sort();
        }
    }

    @Inject(method = "checkHotbarMouseClicked(Lnet/minecraft/client/input/MouseButtonEvent;)V", at = @At("HEAD"))
    private void onMouseClick(MouseButtonEvent click, CallbackInfo ci) {
        if (KeyRegistry.advancedOperationKey.matchesMouse(click)) {
            AdvancedOperationHandler.setPressed(true);
        }
        if (KeyRegistry.sortKey.matchesMouse(click) && ContextManager.isInit()) {
            SortingHandler.sort();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (AdvancedOperationHandler.isReleased()) {
            AdvancedOperationHandler.setPressed(false);
        }
    }
}
