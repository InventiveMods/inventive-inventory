package net.inventive_mods.inventive_inventory.config.gui.widget.profiles;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.util.gui.widget.ClickableWidget;
import net.inventive_mods.inventive_inventory.util.gui.widget.TextWidget;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.network.chat.Component;

public class ConfigProfileTitleWidget extends ClickableWidget {
    private final LinearLayout layout = LinearLayout.horizontal().spacing(10);

    public ConfigProfileTitleWidget(int width, int height) {
        super(width, height);
        Font font = InventiveInventory.getFont();
        String baseTranslationKey = "config.profiles.label.inventive_inventory.";
        this.layout.addChild(SpacerElement.width(font.width("1.")));
        this.layout.addChild(new TextWidget(80, height, Component.translatable(baseTranslationKey + "name"), font).alignCenter());
        this.layout.addChild(new TextWidget(60, height, Component.translatable(baseTranslationKey + "key"), font).alignCenter());
        this.layout.addChild(new TextWidget(205, height, Component.translatable(baseTranslationKey + "preview"), font).alignCenter());
        this.layout.arrangeElements();
        this.width = this.layout.getWidth();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.layout.setPosition(this.getX(), this.getY());
        this.layout.visitWidgets(widget -> widget.render(guiGraphics, mouseX, mouseY, partialTick));
    }
}
