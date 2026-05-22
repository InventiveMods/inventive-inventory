package net.inventive_mods.client.util.widgets;

import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;

public class CenteredTextWidget extends StringWidget {

    public CenteredTextWidget(int width, int height, Component message, Font font) {
        super(0, 0, width, height, message, font);
        this.active = false;
    }

    @Override
    public void visitLines(ActiveTextCollector textCollector) {
        Component text = this.getMessage();
        int width = this.getWidth();
        int textWidth = this.getFont().width(text);
        int x = this.getX() + Math.round(0.5f * (float) (width - textWidth));
        int y = this.getY();
        int height = this.getHeight();
        int finalY = y + height / 2;
        textCollector.accept(x, finalY, text);
    }
}