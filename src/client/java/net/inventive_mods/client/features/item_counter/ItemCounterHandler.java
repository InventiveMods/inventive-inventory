package net.inventive_mods.client.features.item_counter;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

public class ItemCounterHandler {
    public static void register() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.HOTBAR, ItemCounterHudElement.IDENTIFIER, new ItemCounterHudElement());
    }
}
