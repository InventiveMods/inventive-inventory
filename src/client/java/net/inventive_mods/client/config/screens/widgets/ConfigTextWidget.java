package net.inventive_mods.client.config.screens.widgets;

import net.inventive_mods.client.util.widgets.CenteredTextWidget;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class ConfigTextWidget extends CenteredTextWidget {
    public ConfigTextWidget(Component message, Font font) {
        super(message, font);
    }

    public ConfigTextWidget(int width, int height, Component message, Font font) {
        super(width, height, message, font);
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y + 6);
    }
}
