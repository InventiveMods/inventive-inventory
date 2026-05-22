package net.inventive_mods.client.config.screens.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;

public class ConfigTextWidget extends StringWidget {
    public ConfigTextWidget(Component message, Font font) {
        super(message, font);
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y + 6);
    }
}
