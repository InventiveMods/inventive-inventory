package net.inventive_mods.client.util;

import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.enums.item_counter.ItemCounterCountingMode;
import net.inventive_mods.client.config.enums.locked_slots.Style;
import net.inventive_mods.client.config.options.ConfigOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;

public class Drawer {

    public static void drawSlotBackground(GuiGraphicsExtractor context, int x, int y, int color, boolean outlined) {
        if (outlined) {
            int width = 15, height = 15;
            context.fill(x, y, x + width, y + 1, color);
            context.fill(x, y + height, x + width + 1, y + height + 1, color);
            context.fill(x, y, x + 1, y + height, color);
            context.fill(x + width, y, x + width + 1, y + height, color);
        } else context.fill(x, y, x + 16, y + 16, color);
    }

    public static void drawTexture(GuiGraphicsExtractor context, Identifier texture, int x, int y, int size) {
        context.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 0, 0, size, size, size, size);
    }

    public static void drawProfileHotbar(GuiGraphicsExtractor context, int x, int y) {
        context.blit(RenderPipelines.GUI_TEXTURED, Textures.HOTBAR, x, y, 0, 0, 205, 20, 205, 20);
    }

    public static void drawLockedSlot(GuiGraphicsExtractor context, Identifier texture, ConfigOption<Integer> option, int x, int y) {
        context.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 0, 0, 20, 20, 20, 20);
        Drawer.drawSlotBackground(context, x + 2, y + 2, option.getValue(), ConfigManager.LOCKED_SLOT_STYLE.is(Style.OUTLINED));
        if (ConfigManager.SHOW_LOCK.is(true)) Drawer.drawTexture(context, Textures.LOCK, x + 14, y, 8);
    }

    public static void drawItemCounter(GuiGraphicsExtractor context, int x, int y, int count, ItemStack stack) {
        String text = ConfigManager.ITEM_COUNTER_COUNTING_MODE.is(ItemCounterCountingMode.ITEMS) ? Integer.toString(count) : "[" + (count / stack.getMaxStackSize()) + "]";
        Matrix3x2fStack matrices = context.pose();
        matrices.pushMatrix();
        matrices.scale(0.5f, 0.5f);
        if ((ConfigManager.ITEM_COUNTER_COUNTING_MODE.is(ItemCounterCountingMode.ITEMS) && count != 0) || count / stack.getMaxStackSize() != 0)
            context.text(Minecraft.getInstance().font, text, x * 2, y * 2, ConfigManager.ITEM_COUNTER_COLOR.getValue());
        matrices.popMatrix();
    }

    public static void drawItemCounter(GuiGraphicsExtractor context, int x, int y, int count, Item item) {
        String text = ConfigManager.ITEM_COUNTER_COUNTING_MODE.is(ItemCounterCountingMode.ITEMS) ? Integer.toString(count) : "[" + (count / item.getDefaultMaxStackSize()) + "]";
        Matrix3x2fStack matrices = context.pose();
        matrices.pushMatrix();
        matrices.scale(0.5f, 0.5f);
        if ((ConfigManager.ITEM_COUNTER_COUNTING_MODE.is(ItemCounterCountingMode.ITEMS) && count != 0) || count / item.getDefaultMaxStackSize() != 0)
            context.text(Minecraft.getInstance().font, text, x * 2, y * 2, ConfigManager.ITEM_COUNTER_COLOR.getValue());
        matrices.popMatrix();
    }
}
