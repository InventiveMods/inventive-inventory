package net.inventive_mods.client.keys.handler;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.keys.KeyRegistry;
import org.lwjgl.glfw.GLFW;

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
        Window window = InventiveInventoryClient.getClient().getWindow();
        int code = KeyMappingHelper.getBoundKeyOf(KeyRegistry.advancedOperationKey).getValue();
        if (code >= GLFW.GLFW_KEY_SPACE && code <= GLFW.GLFW_KEY_LAST) {
            return !InputConstants.isKeyDown(window, code);
        } else if (code >= GLFW.GLFW_MOUSE_BUTTON_1 && code <= GLFW.GLFW_MOUSE_BUTTON_LAST) {
            return GLFW.glfwGetMouseButton(window.handle(), code) == GLFW.GLFW_RELEASE;
        }
        return true;
    }
}
