package net.inventive_mods.client.util.widgets;

import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

public class CenteredTextWidget extends StringWidget {
    public CenteredTextWidget(Component message, Font font) {
        this(font.width(message.getVisualOrderText()), 9, message, font);
    }

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
        int finalY = y + (height - 9) / 2;
        FormattedCharSequence orderedText = textWidth > width ? this.trim(text, width) : text.getVisualOrderText();
        textCollector.accept(x, finalY, orderedText);
    }

    private FormattedCharSequence trim(Component text, int width) {
        FormattedText stringVisitable = this.getFont().substrByWidth(text, width - this.getFont().width(Component.literal("...")));
        return Language.getInstance().getVisualOrder(FormattedText.composite(stringVisitable, CommonComponents.ELLIPSIS));
    }
}