package net.inventive_mods.inventive_inventory.features.locked_slots.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.config.enums.locked_slots.SlotStyle;
import net.inventive_mods.inventive_inventory.features.locked_slots.LockedSlots;
import net.inventive_mods.inventive_inventory.util.Renderer;
import net.inventive_mods.inventive_inventory.util.Textures;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public class MixinSlotHudRenderer {
    @WrapOperation(method = "renderItemHotbar", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/client/DeltaTracker;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V"))
    private void onRenderHotbar(Gui instance, GuiGraphics guiGraphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack stack, int seed, Operation<Void> original, @Local(ordinal = 4) int hotbarSlot) {
        if (!InventiveInventory.getPlayer().isCreative() && LockedSlots.get().contains(hotbarSlot + Inventory.INVENTORY_SIZE)) {
            Renderer.renderSlotBackground(guiGraphics, x, y, Config.LOCKED_SLOTS_HOTBAR_COLOR.getValue(), Config.LOCKED_SLOT_STYLE.is(SlotStyle.OUTLINED));
            original.call(instance, guiGraphics, x, y, deltaTracker, player, stack, seed);
            if (Config.SHOW_LOCK.is(true)) Renderer.renderGuiTexture(guiGraphics, Textures.LOCK, x + 11, y - 1, 8);
        } else original.call(instance, guiGraphics, x, y, deltaTracker, player, stack, seed);
    }
}
