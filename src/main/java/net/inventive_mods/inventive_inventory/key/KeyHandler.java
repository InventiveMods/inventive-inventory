package net.inventive_mods.inventive_inventory.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = InventiveInventory.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyHandler {
    public static final String INVENTIVE_INVENTORY_CATEGORY = "key.category.inventive_inventory.main";
    private static final String KEY_ADVANCED_OPERATION = "key.inventive_inventory.advanced_operation";

    public static KeyMapping advancedOperationKey = new KeyMapping(
            KEY_ADVANCED_OPERATION,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            INVENTIVE_INVENTORY_CATEGORY
    );


    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(advancedOperationKey);
    }
}
