package net.inventive_mods.inventive_inventory.config.screens.widgets;

import net.inventive_mods.inventive_inventory.util.widgets.CenteredTextWidget;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;

public class ConfigTextWidget extends CenteredTextWidget {
    public ConfigTextWidget(Text message, TextRenderer textRenderer) {
        super(message, textRenderer);
    }

    public ConfigTextWidget(int width, int height, Text message, TextRenderer textRenderer) {
        super(width, height, message, textRenderer);
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y + 6);
    }
}
