package net.inventive_mods.inventive_inventory.util.mouse.mixins;

import net.inventive_mods.inventive_inventory.util.mouse.MouseLocation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class MixinMouseHandler {
    @Shadow
    @Nullable
    protected Slot focusedSlot;

    @Inject(method = "render", at = @At("HEAD"))
    private void mouseOverSlot(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MouseLocation.setHoveredSlot(this.focusedSlot);
    }
}
