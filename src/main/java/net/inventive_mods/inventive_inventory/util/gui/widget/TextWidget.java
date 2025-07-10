package net.inventive_mods.inventive_inventory.util.gui.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;

public class TextWidget extends StringWidget {
    public TextWidget(Component message, Font font) {
        super(message, font);
    }

    public TextWidget(int width, int height, Component message, Font font) {
        super(width, height, message, font);
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y + 6);
    }
}
