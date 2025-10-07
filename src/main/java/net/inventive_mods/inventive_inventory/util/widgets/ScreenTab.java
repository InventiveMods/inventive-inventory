package net.inventive_mods.inventive_inventory.util.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.options.ConfigOption;
import net.inventive_mods.inventive_inventory.config.screens.ConfigScreen;
import net.inventive_mods.inventive_inventory.config.screens.widgets.ConfigTextWidget;

public abstract class ScreenTab extends CustomListWidget {

    public ScreenTab(MinecraftClient client, int width, ConfigScreen screen) {
        super(client, width, screen);
    }

    protected void addTitle(Text title) {
        if (client == null) return;
        ConfigTextWidget text = new ConfigTextWidget(310, client.textRenderer.fontHeight, title.copy().setStyle(Style.EMPTY.withBold(true)), client.textRenderer);
        this.addWidgetEntry(text, null);
    }

    protected void addWidget(ConfigOption<?> option) {
        if (client == null) return;
        this.addWidgetEntry(option.createLabel(), option.asWidget());
    }

    protected void addCenteredWidget(ClickableWidget widget) {
        if (client == null) return;
        this.addWidgetEntry(widget, null);
    }

    protected void addEmptyRow() {
        if (client == null) return;
        TextWidget textWidget = new TextWidget(Text.empty(), InventiveInventory.getClient().textRenderer);
        this.addWidgetEntry(textWidget, textWidget);
    }

    protected void onClose() {}
}
