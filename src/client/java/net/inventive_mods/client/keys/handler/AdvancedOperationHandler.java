package net.inventive_mods.client.keys.handler;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.inventive_mods.client.keys.KeyRegistry;

public class AdvancedOperationHandler {
    private static boolean pressed = false;

    public static boolean isPressed() {
        return pressed;
    }

    public static void setPressed(boolean state) {
        pressed = state;
    }

    public static boolean isReleased() {
        if (!pressed) return false;
        int code = KeyMappingHelper.getBoundKeyOf(KeyRegistry.advancedOperationKey).getValue();
        if (code >= InputConstants.KEY_SPACE && code <= 300) {
            return !InputConstants.isKeyDown(code);
        }
        return false;
    }
}
