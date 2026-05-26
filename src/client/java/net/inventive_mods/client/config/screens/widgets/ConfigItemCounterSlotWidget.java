package net.inventive_mods.client.config.screens.widgets;

import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.util.Drawer;
import net.inventive_mods.client.util.Textures;
import net.inventive_mods.client.util.widgets.BaseWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;

public class ConfigItemCounterSlotWidget extends BaseWidget {

    public ConfigItemCounterSlotWidget(int width, int height) {
        super(width, height);
    }

    @Override
    protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int slotX = this.getX() + this.getWidth() / 2 - 25;
        Drawer.drawTexture(graphics, Textures.HOTBAR_SLOT, slotX, this.getY(), 20);
        Drawer.drawItemCounter(graphics, slotX + 2, this.getY() + 2, 128, Items.DIAMOND);

        slotX = this.getX() + this.getWidth() / 2 + 5;
        Drawer.drawLockedSlot(graphics, Textures.HOTBAR_SLOT, ConfigManager.LOCKED_SLOTS_HOTBAR_COLOR, this.getX() + this.getWidth() / 2 + 5, this.getY());
        Drawer.drawItemCounter(graphics, slotX + 2, this.getY() + 2, 128, Items.DIAMOND);
    }
}
