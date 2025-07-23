package net.inventive_mods.inventive_inventory.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = InventiveInventory.MOD_ID, value = Dist.CLIENT)
public class AdvancedOperationHandler {
    private static boolean pressed = false;

    public static boolean isPressed() {
        return pressed;
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (InventiveInventory.getMinecraft().screen == null)
            pressed = KeyHandler.advancedOperationKey.isDown();
    }

    @SubscribeEvent
    public static void onKeyPressed(ScreenEvent.KeyPressed.Pre event) {
        if (KeyHandler.advancedOperationKey.isActiveAndMatches(InputConstants.getKey(event.getKeyCode(), event.getScanCode())))
            pressed = true;
    }

    @SubscribeEvent
    public static void onKeyReleased(ScreenEvent.KeyReleased.Pre event) {
        if (KeyHandler.advancedOperationKey.isActiveAndMatches(InputConstants.getKey(event.getKeyCode(), event.getScanCode())))
            pressed = false;
    }

    @SubscribeEvent
    public static void onMouseClicked(ScreenEvent.MouseButtonPressed.Pre event) {
        if (KeyHandler.advancedOperationKey.matchesMouse(event.getButton()))
            pressed = true;
    }

    @SubscribeEvent
    public static void onMouseReleased(ScreenEvent.MouseButtonReleased.Pre event) {
        if (KeyHandler.advancedOperationKey.matchesMouse(event.getButton()))
            pressed = false;
    }
}
