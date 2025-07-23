package net.inventive_mods.inventive_inventory.config.gui.widget.locked_slots;

import net.inventive_mods.inventive_inventory.config.option.ConfigOption;
import net.inventive_mods.inventive_inventory.util.Renderer;
import net.inventive_mods.inventive_inventory.util.gui.widget.ClickableWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;

public class LockedSlotWidget extends ClickableWidget {
    private final ResourceLocation texture;
    private final ConfigOption<Integer> option;

    public LockedSlotWidget(ResourceLocation texture, ConfigOption<Integer> option, int width, int height) {
        super(width, height);
        this.texture = texture;
        this.option = option;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        Renderer.renderLockedSlot(guiGraphics, this.texture, this.option, this.getX() + this.getWidth() / 2 - 10, this.getY());
    }

    @Override
    public void playDownSound(SoundManager handler) {
    }
}
