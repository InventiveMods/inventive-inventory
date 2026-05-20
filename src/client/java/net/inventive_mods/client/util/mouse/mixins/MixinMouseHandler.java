package net.inventive_mods.client.util.mouse.mixins;

import net.inventive_mods.client.util.mouse.MouseLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public abstract class MixinMouseHandler {

    @Shadow
    @Nullable
    protected Slot hoveredSlot;

    @Inject(method = "tick", at = @At("HEAD"))
    private void mouseOverSlot(CallbackInfo ci) {
        MouseLocation.setHoveredSlot(this.hoveredSlot);
    }
}
