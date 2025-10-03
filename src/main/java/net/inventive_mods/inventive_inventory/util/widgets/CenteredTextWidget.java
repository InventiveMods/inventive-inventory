package net.inventive_mods.inventive_inventory.util.widgets;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AbstractTextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Language;

public class CenteredTextWidget extends AbstractTextWidget {

    public CenteredTextWidget(Text message, TextRenderer textRenderer) {
        this(textRenderer.getWidth(message.asOrderedText()), 9, message, textRenderer);
    }

    public CenteredTextWidget(int width, int height, Text message, TextRenderer textRenderer) {
        super(0, 0, width, height, message, textRenderer);
        this.active = false;
    }

    public CenteredTextWidget setTextColor(int textColor) {
        super.setTextColor(textColor);
        return this;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        Text text = this.getMessage();
        TextRenderer textRenderer = this.getTextRenderer();
        int width = this.getWidth();
        int textWidth = textRenderer.getWidth(text);
        int x = this.getX() + Math.round(0.5f * (float)(width - textWidth));
        int y = this.getY();
        int height = this.getHeight();
        int finalY = y + (height - 9) / 2;
        OrderedText orderedText = textWidth > width ? this.trim(text, width) : text.asOrderedText();
        context.drawTextWithShadow(textRenderer, orderedText, x, finalY, this.getTextColor());
    }

    private OrderedText trim(Text text, int width) {
        TextRenderer textRenderer = this.getTextRenderer();
        StringVisitable stringVisitable = textRenderer.trimToWidth(text, width - textRenderer.getWidth(ScreenTexts.ELLIPSIS));
        return Language.getInstance().reorder(StringVisitable.concat(stringVisitable, ScreenTexts.ELLIPSIS));
    }
}
