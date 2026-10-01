package net.inventive_mods.client.keys.mixins;


import com.mojang.blaze3d.platform.InputConstants;
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
    private void onKeyPressed(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (KeyRegistry.advancedOperationKey.matches(event)) {
            AdvancedOperationHandler.setPressed(true);
        }
        if (KeyRegistry.sortKey.matches(event) && ContextManager.isInit()) {
            SortingHandler.sort();
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void onMouseClick(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        if (KeyRegistry.advancedOperationKey.matchesMouse(event)) {
            AdvancedOperationHandler.setPressed(true);
        }
        if (KeyRegistry.sortKey.matchesMouse(event) && ContextManager.isInit()) {
            SortingHandler.sort();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (AdvancedOperationHandler.isReleased()) {
            AdvancedOperationHandler.setPressed(false);
        }
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"))
    private void onMouseReleased(MouseButtonEvent event, CallbackInfoReturnable<Boolean> cir) {
        System.out.println("AdvancedOperationHandler: isReleased() called, event: " + event);
        if (KeyRegistry.advancedOperationKey.matchesMouse(event)) {
            AdvancedOperationHandler.setPressed(false);
        }
    }
}
