package net.inventive_mods.client.features.locked_slots.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.enums.locked_slots.Style;
import net.inventive_mods.client.features.locked_slots.LockedSlotsHandler;
import net.inventive_mods.client.keys.handler.AdvancedOperationHandler;
import net.inventive_mods.client.util.Drawer;
import net.inventive_mods.client.util.Textures;
import net.inventive_mods.client.util.slots.PlayerSlots;
import net.inventive_mods.client.util.slots.SlotTypes;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class MixinLockedSlotsDrawer {
    @Shadow
    @Nullable
    protected Slot hoveredSlot;

    @Shadow
    protected abstract void extractSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY);

    @Inject(method = "extractSlot", at = @At(value = "HEAD"))
    private void onDrawItem(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        if (!InventiveInventoryClient.getPlayer().isCreative() && LockedSlotsHandler.getLockedSlots().contains(slot.index)) {
            if (!(this.hoveredSlot == slot && AdvancedOperationHandler.isPressed())) {
                Drawer.drawSlotBackground(graphics, slot.x, slot.y, ConfigManager.LOCKED_SLOTS_COLOR.getValue(), ConfigManager.LOCKED_SLOT_STYLE.is(Style.OUTLINED));
            }
        }
    }

    @Inject(method = "extractSlot", at = @At(value = "TAIL"))
    private void afterDrawItem(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        if (!InventiveInventoryClient.getPlayer().isCreative() && LockedSlotsHandler.getLockedSlots().contains(slot.index)) {
            if (ConfigManager.SHOW_LOCK.is(true))
                Drawer.drawTexture(graphics, Textures.LOCK, slot.x + 11, slot.y - 2, 8);
        }
    }

    @WrapOperation(method = "extractContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;extractSlotHighlightFront(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V"))
    private void drawSlotHighlight(AbstractContainerScreen<?> instance, GuiGraphicsExtractor graphics, Operation<Void> original) {
        Slot slot = this.hoveredSlot;
        if (!InventiveInventoryClient.getPlayer().isCreative() && slot != null) {
            if (AdvancedOperationHandler.isPressed()) {
                if (LockedSlotsHandler.getLockedSlots().contains(slot.index)) {
                    Drawer.drawSlotBackground(graphics, slot.x, slot.y, LockedSlotsHandler.LOCKED_HOVER_COLOR, false);
                    this.extractSlot(graphics, slot, 0, 0);
                    return;
                } else if (PlayerSlots.get().append(SlotTypes.HOTBAR).exclude(SlotTypes.LOCKED_SLOT).contains(slot.index)) {
                    Drawer.drawSlotBackground(graphics, slot.x, slot.y, LockedSlotsHandler.HOVER_COLOR, false);
                    this.extractSlot(graphics, slot, 0, 0);
                    return;
                }
            } else if (LockedSlotsHandler.getLockedSlots().contains(slot.index)) {
                original.call(instance, graphics);
                if (ConfigManager.SHOW_LOCK.is(true))
                    Drawer.drawTexture(graphics, Textures.LOCK, slot.x + 11, slot.y - 2, 8);
                return;
            }
        }
        original.call(instance, graphics);
    }
}
