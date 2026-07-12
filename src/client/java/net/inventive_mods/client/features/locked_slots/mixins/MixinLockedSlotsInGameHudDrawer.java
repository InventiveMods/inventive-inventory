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
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public abstract class MixinLockedSlotsInGameHudDrawer {

    @WrapOperation(method = "extractItemHotbar", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/Hud;extractSlot(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IILnet/minecraft/client/DeltaTracker;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V"))
    private void onRenderHotbar(Hud instance, GuiGraphicsExtractor graphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int seed, Operation<Void> original, @Local(name = "i") int i) {
        if (!InventiveInventoryClient.getPlayer().isCreative() && LockedSlotsHandler.getLockedSlots().unadjust().contains(i + Inventory.INVENTORY_SIZE - Inventory.SELECTION_SIZE)) {
            Drawer.drawSlotBackground(graphics, x, y, ConfigManager.LOCKED_SLOTS_HOTBAR_COLOR.getValue(), ConfigManager.LOCKED_SLOT_STYLE.is(Style.OUTLINED));
            original.call(instance, graphics, x, y, deltaTracker, player, itemStack, seed);
            if (ConfigManager.SHOW_LOCK.is(true)) Drawer.drawTexture(graphics, Textures.LOCK, x + 11, y - 1, 8);
        } else original.call(instance, graphics, x, y, deltaTracker, player, itemStack, seed);
    }
}

