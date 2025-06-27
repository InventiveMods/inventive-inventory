package net.inventive_mods.inventive_inventory.features.locked_slots.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.config.enums.locked_slots.Style;
import net.inventive_mods.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.inventive_mods.inventive_inventory.util.Drawer;
import net.inventive_mods.inventive_inventory.util.Textures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public abstract class MixinLockedSlotsInGameHudDrawer {

    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/client/render/RenderTickCounter;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V"))
    private void onRenderHotbar(InGameHud instance, DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed, Operation<Void> original, @Local(ordinal = 4) int hotbarSlot) {
        if (!InventiveInventory.getPlayer().isInCreativeMode() && LockedSlotsHandler.getLockedSlots().contains(hotbarSlot + PlayerInventory.MAIN_SIZE)) {
            Drawer.drawSlotBackground(context, x, y, ConfigManager.LOCKED_SLOTS_HOTBAR_COLOR.getValue(), ConfigManager.LOCKED_SLOT_STYLE.is(Style.OUTLINED));
            original.call(instance, context, x, y, tickCounter, player, stack, seed);
            if (ConfigManager.SHOW_LOCK.is(true)) Drawer.drawTexture(context, Textures.LOCK, x + 11, y - 1, 8);
        } else original.call(instance, context, x, y, tickCounter, player, stack, seed);
    }
}

