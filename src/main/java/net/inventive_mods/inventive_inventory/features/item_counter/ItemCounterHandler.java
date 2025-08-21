package net.inventive_mods.inventive_inventory.features.item_counter;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class ItemCounterHandler {
    public static void register() {
        HudRenderCallback.EVENT.register(new ItemCounterHudElement());
    }
}
