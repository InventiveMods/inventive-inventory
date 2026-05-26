package net.inventive_mods.client.config.screens.widgets;

import net.inventive_mods.client.util.widgets.BaseWidget;
import net.inventive_mods.client.config.options.ConfigOption;
import net.inventive_mods.client.util.Drawer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class ConfigLockedSlotWidget extends BaseWidget {
    private final Identifier texture;
    private final ConfigOption<Integer> option;

    public ConfigLockedSlotWidget(Identifier texture, ConfigOption<Integer> option, int width, int height) {
        super(width, height);
        this.texture = texture;
        this.option = option;
    }

    @Override
    protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        Drawer.drawLockedSlot(graphics, this.texture, this.option, this.getX() + this.getWidth() / 2 - 10, this.getY());
    }
}

