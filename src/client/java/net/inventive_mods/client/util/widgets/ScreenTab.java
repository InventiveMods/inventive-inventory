package net.inventive_mods.client.util.widgets;

import com.google.common.collect.ImmutableList;
import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.options.ConfigOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ScreenTab extends OptionsList {
    private final Screen screen;

    public ScreenTab(Minecraft client, int width, OptionsSubScreen screen) {
        super(client, width, screen);
        this.screen = screen;
    }

    protected void addTitle(Component title) {
        StringWidget text = new CenteredTextWidget(310, InventiveInventoryClient.getClient().font.lineHeight, title.copy().setStyle(Style.EMPTY.withBold(true)), InventiveInventoryClient.getClient().font);
        this.addCenteredWidget(text);
    }

    protected void addWidget(ConfigOption<?> option) {
        this.addEntry(Entry.small(option.createLabel(), option.asWidget(), this.screen));
    }

    protected void addCenteredWidget(AbstractWidget widget) {
        this.addEntry(Entry.small(widget, null, this.screen));
    }

    public void addSpecialWidget(AbstractWidget widget) {
        this.addEntry(new Entry(ImmutableList.of(new OptionInstanceWidget(widget)), screen));
    }

    protected void addEmptyRow() {
        StringWidget textWidget = new StringWidget(Component.empty(), InventiveInventoryClient.getClient().font);
        this.addCenteredWidget(textWidget);
    }

    @Override
    public int getRowWidth() {
        if (this.children().isEmpty()) return this.width;
        AtomicInteger longest = new AtomicInteger();
        this.children().forEach(entry -> longest.set(Math.max(longest.get(), entry.children().getFirst().getRectangle().width())));
        return longest.get();
    }

    @Override
    public int getRowLeft() {
        return (this.width - this.getRowWidth()) / 2;
    }

    protected void onClose() {
    }

    @Override
    public void setFocused(boolean focused) {
        
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public @NonNull ScreenRectangle getRectangle() {
        return super.getRectangle();
    }
}
