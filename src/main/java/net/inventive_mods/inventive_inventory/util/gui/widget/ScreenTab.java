package net.inventive_mods.inventive_inventory.util.gui.widget;

import net.inventive_mods.inventive_inventory.config.option.ConfigOption;
import net.inventive_mods.inventive_inventory.util.gui.screen.TabbedScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public class ScreenTab extends ListWidget {
    public ScreenTab(Minecraft minecraft, int width, TabbedScreen screen) {
        super(minecraft, width, screen);
    }

    protected void addTitle(Component title) {
        TextWidget text = new TextWidget(310, minecraft.font.lineHeight, title.copy().withStyle(style -> style.withBold(true)), minecraft.font);
        this.addEntry(Entry.create(text, null));
    }

    protected void addConfigOption(ConfigOption<?> option) {
        this.addEntry(Entry.create(option.createLabel(), option.asWidget()));
    }

    protected void addCenteredWidget(AbstractWidget widget) {
        this.addEntry(Entry.create(widget, null));
    }

    protected void addEmptyRow() {
        TextWidget textWidget = new TextWidget(Component.empty(), minecraft.font);
        this.addEntry(Entry.create(textWidget, textWidget));
    }

    public void onClose() {}
}
