package net.inventive_mods.inventive_inventory.util;

import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.config.enums.locked_slots.SlotStyle;
import net.inventive_mods.inventive_inventory.config.option.ConfigOption;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;

public class Renderer {
    private static final int SLOT_WIDTH = 16;
    private static final int SLOT_HEIGHT = 16;

    public static void renderSlotBackground(GuiGraphics guiGraphics, int x, int y, int color, boolean outlined) {
        if (outlined) {
            int width = SLOT_WIDTH - 1, height = SLOT_HEIGHT - 1;
            guiGraphics.fill(x, y, x + width, y + 1, color);
            guiGraphics.fill(x, y + height, x + width + 1, y + height + 1, color);
            guiGraphics.fill(x, y, x + 1, y + width, color);
            guiGraphics.fill(x + width, y, x + width + 1, y + height, color);
        } else {
            guiGraphics.fill(x, y, x + SLOT_WIDTH, y + SLOT_HEIGHT, color);
        }
    }

    public static void renderGuiTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int size) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 0, 0, size, size, size, size);
    }

    public static void renderLockedSlot(GuiGraphics guiGraphics, ResourceLocation texture, ConfigOption<Integer> option, int x, int y) {
        renderGuiTexture(guiGraphics, texture, x, y, 20);
        renderSlotBackground(guiGraphics, x + 2, y + 2, option.getValue(), Config.LOCKED_SLOT_STYLE.is(SlotStyle.OUTLINED));
        if (Config.SHOW_LOCK.is(true))
            renderGuiTexture(guiGraphics, Textures.LOCK, x + 14, y, 8);
    }
}
