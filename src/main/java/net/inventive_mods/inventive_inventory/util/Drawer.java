package net.inventive_mods.inventive_inventory.util;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.config.enums.locked_slots.Style;
import net.inventive_mods.inventive_inventory.config.options.ConfigOption;

public class Drawer {

    public static void drawSlotBackground(DrawContext context, int x, int y, int color, int z, boolean outlined) {
        if (outlined) {
            int width = 15, height = 15;
            context.fill(x, y, x + width, y + 1, color);
            context.fill(x, y + height, x + width + 1, y + height + 1, color);
            context.fill(x, y, x + 1, y + height, color);
            context.fill(x + width, y, x + width + 1, y + height, color);
        } else context.fillGradient(x, y, x + 16, y + 16, z, color, color);
    }

    public static void drawTexture(DrawContext context, Identifier texture, int x, int y, int z, int size) {
        context.drawTexture(texture, x, y, z, 0, 0, size, size, size, size);
    }

    public static void drawProfileHotbar(DrawContext context, int x, int y) {
        context.drawTexture(Textures.HOTBAR, x, y, 0, 0, 0, 205, 20, 205, 20);
    }

    public static void drawLockedSlot(DrawContext context, Identifier texture, ConfigOption<Integer> option, int x, int y) {
        context.drawTexture(texture, x, y, 0, 0, 0, 20, 20, 20, 20);
        Drawer.drawSlotBackground(context, x + 2, y + 2, option.getValue(), 0, ConfigManager.LOCKED_SLOT_STYLE.is(Style.OUTLINED));
        if (ConfigManager.SHOW_LOCK.is(true)) Drawer.drawTexture(context, Textures.LOCK, x + 14, y, 200, 8);
    }
}
