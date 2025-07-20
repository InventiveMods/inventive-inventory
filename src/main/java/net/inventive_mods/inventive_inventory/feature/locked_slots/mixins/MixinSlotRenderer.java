package net.inventive_mods.inventive_inventory.feature.locked_slots.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.config.enums.locked_slots.SlotStyle;
import net.inventive_mods.inventive_inventory.feature.locked_slots.LockedSlots;
import net.inventive_mods.inventive_inventory.key.AdvancedOperationHandler;
import net.inventive_mods.inventive_inventory.util.Renderer;
import net.inventive_mods.inventive_inventory.util.Textures;
import net.inventive_mods.inventive_inventory.util.slot.SlotRange;
import net.inventive_mods.inventive_inventory.util.slot.SlotType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(AbstractContainerScreen.class)
public abstract class MixinSlotRenderer {
    @Shadow @Nullable protected Slot hoveredSlot;

    @Shadow protected abstract void renderSlot(GuiGraphics guiGraphics, Slot slot);

    @Inject(method = "renderSlot", at = @At("HEAD"))
    private void onRenderSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (!InventiveInventory.getPlayer().isCreative() && LockedSlots.get().contains(slot.index)) {
            if (!(this.hoveredSlot == slot && AdvancedOperationHandler.isPressed())) {
                Renderer.renderSlotBackground(guiGraphics, slot.x, slot.y, Config.LOCKED_SLOTS_COLOR.getValue(), Config.LOCKED_SLOT_STYLE.is(SlotStyle.OUTLINED));
            }
        }
    }

    @Inject(method = "renderSlot", at = @At("TAIL"))
    private void afterRenderSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (!InventiveInventory.getPlayer().isCreative() && LockedSlots.get().contains(slot.index)) {
            if (Config.SHOW_LOCK.is(true))
                Renderer.renderGuiTexture(guiGraphics, Textures.LOCK, slot.x + 11, slot.y - 2, 8);
        }
    }

    @WrapOperation(method = "renderContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlotHighlightFront(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    private void onRenderSlotHighlightFront(AbstractContainerScreen instance, GuiGraphics guiGraphics, Operation<Void> original) {
        if (!InventiveInventory.getPlayer().isCreative() && this.hoveredSlot != null) {
            if (AdvancedOperationHandler.isPressed()) {
                if (LockedSlots.get().contains(this.hoveredSlot.index)) {
                    Renderer.renderSlotBackground(guiGraphics, this.hoveredSlot.x, this.hoveredSlot.y, LockedSlots.LOCKED_HOVER_COLOR, false);
                    this.renderSlot(guiGraphics, this.hoveredSlot);
                    return;
                } else if (SlotRange.getPlayerSlots().append(SlotType.HOTBAR).exclude(SlotType.LOCKED_SLOT).contains(this.hoveredSlot.index)) {
                    Renderer.renderSlotBackground(guiGraphics, this.hoveredSlot.x, this.hoveredSlot.y, LockedSlots.HOVER_COLOR, false);
                    this.renderSlot(guiGraphics, this.hoveredSlot);
                    return;
                }
            } else if (LockedSlots.get().contains(this.hoveredSlot.index)) {
                original.call(instance, guiGraphics);
                if (Config.SHOW_LOCK.is(true)) Renderer.renderGuiTexture(guiGraphics, Textures.LOCK, this.hoveredSlot.x + 11, this.hoveredSlot.y - 2, 8);
                return;
            }
        }
        original.call(instance, guiGraphics);
    }
}
