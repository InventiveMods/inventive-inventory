package net.inventive_mods.inventive_inventory.features.item_counter;

import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;

public class ItemCounterHandler {
    public static void register() {
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> layeredDrawer.attachLayerAfter(IdentifiedLayer.HOTBAR_AND_BARS, ItemCounterHudElement.IDENTIFIER, new ItemCounterHudElement()));
    }
}
