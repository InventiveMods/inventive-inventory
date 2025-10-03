package net.inventive_mods.inventive_inventory.keys.handler;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.keys.KeyRegistry;
import net.minecraft.client.util.Window;
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
        Window window = InventiveInventory.getClient().getWindow();
        int code = KeyBindingHelper.getBoundKeyOf(KeyRegistry.advancedOperationKey).getCode();
        if (code >= GLFW.GLFW_KEY_SPACE && code <= GLFW.GLFW_KEY_LAST) {
            return !InputUtil.isKeyPressed(window, code);
        } else if (code >= GLFW.GLFW_MOUSE_BUTTON_1 && code <= GLFW.GLFW_MOUSE_BUTTON_LAST) {
            return GLFW.glfwGetMouseButton(window.getHandle(), code) == GLFW.GLFW_RELEASE;
        }
        return true;
    }
}
