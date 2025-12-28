package net.inventive_mods.inventive_inventory.keys.mixins;


import net.inventive_mods.inventive_inventory.context.ContextManager;
import net.inventive_mods.inventive_inventory.features.sorting.SortingHandler;
import net.inventive_mods.inventive_inventory.keys.KeyRegistry;
import net.inventive_mods.inventive_inventory.keys.handler.AdvancedOperationHandler;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.input.KeyInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public class MixinKeyInputHandler {
    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void onKeyPressed(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        if (KeyRegistry.advancedOperationKey.matchesKey(input)) {
            AdvancedOperationHandler.setPressed(true);
        }
        if (KeyRegistry.sortKey.matchesKey(input) && ContextManager.isInit()) {
            SortingHandler.sort();
        }
    }

    @Inject(method = "onMouseClick(Lnet/minecraft/client/gui/Click;)V", at = @At("HEAD"))
    private void onMouseClick(Click click, CallbackInfo ci) {
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
