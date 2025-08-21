package net.inventive_mods.inventive_inventory.config.screens.widgets;

import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.util.Drawer;
import net.inventive_mods.inventive_inventory.util.Textures;
import net.inventive_mods.inventive_inventory.util.widgets.CustomClickableWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.item.Items;

public class ConfigItemCounterSlotWidget extends CustomClickableWidget {

    public ConfigItemCounterSlotWidget(int width, int height) {
        super(width, height);
    }

    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        int slotX = this.getX() + this.getWidth() / 2 - 25;
        Drawer.drawTexture(context, Textures.HOTBAR_SLOT, slotX, this.getY(), 0, 20);
        Drawer.drawItemCounter(context, slotX + 2, this.getY() + 2, 128, Items.DIAMOND.getDefaultStack());

        slotX = this.getX() + this.getWidth() / 2 + 5;
        Drawer.drawLockedSlot(context, Textures.HOTBAR_SLOT, ConfigManager.LOCKED_SLOTS_HOTBAR_COLOR, this.getX() + this.getWidth() / 2 + 5, this.getY());
        Drawer.drawItemCounter(context, slotX + 2, this.getY() + 2, 128, Items.DIAMOND.getDefaultStack());
    }

    @Override
    public void playDownSound(SoundManager soundManager) {}
}
