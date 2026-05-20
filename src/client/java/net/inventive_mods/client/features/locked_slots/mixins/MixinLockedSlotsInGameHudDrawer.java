package net.inventive_mods.client.features.locked_slots.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.enums.locked_slots.Style;
import net.inventive_mods.client.features.locked_slots.LockedSlotsHandler;
import net.inventive_mods.client.util.Drawer;
import net.inventive_mods.client.util.Textures;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public abstract class MixinLockedSlotsInGameHudDrawer {

    @WrapOperation(method = "renderItemHotbar", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/client/DeltaTracker;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V"))
    private void onRenderHotbar(Gui instance, GuiGraphicsExtractor context, int x, int y, DeltaTracker tickCounter, Player player, ItemStack stack, int seed, Operation<Void> original, @Local(ordinal = 4) int hotbarSlot) {
        if (!InventiveInventoryClient.getPlayer().hasInfiniteMaterials() && LockedSlotsHandler.getLockedSlots().contains(hotbarSlot + Inventory.INVENTORY_SIZE)) {
            Drawer.drawSlotBackground(context, x, y, ConfigManager.LOCKED_SLOTS_HOTBAR_COLOR.getValue(), ConfigManager.LOCKED_SLOT_STYLE.is(Style.OUTLINED));
            original.call(instance, context, x, y, tickCounter, player, stack, seed);
            if (ConfigManager.SHOW_LOCK.is(true)) Drawer.drawTexture(context, Textures.LOCK, x + 11, y - 1, 8);
        } else original.call(instance, context, x, y, tickCounter, player, stack, seed);
    }
}

