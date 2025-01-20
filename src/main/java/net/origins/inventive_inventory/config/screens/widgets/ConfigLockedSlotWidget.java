package net.origins.inventive_inventory.config.screens.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.util.Identifier;
import net.origins.inventive_inventory.config.options.ConfigOption;
import net.origins.inventive_inventory.util.Drawer;
import net.origins.inventive_inventory.util.widgets.CustomClickableWidget;

public class ConfigLockedSlotWidget extends CustomClickableWidget {
    private final Identifier texture;
    private final ConfigOption<Integer> option;

    public ConfigLockedSlotWidget(Identifier texture, ConfigOption<Integer> option, int width, int height) {
        super(width, height);
        this.texture = texture;
        this.option = option;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        Drawer.drawLockedSlot(context, this.texture, this.option, this.getX() + this.getWidth() / 2 - 10, this.getY());
    }

    @Override
    public void playDownSound(SoundManager soundManager) {}
}

