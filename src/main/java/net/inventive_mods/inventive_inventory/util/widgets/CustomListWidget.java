package net.inventive_mods.inventive_inventory.util.widgets;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.inventive_mods.inventive_inventory.config.screens.ConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class CustomListWidget extends ElementListWidget<CustomListWidget.WidgetEntry> {

    public CustomListWidget(MinecraftClient client, int width, ConfigScreen screen) {
        super(client, width, screen.layout.getContentHeight(), screen.layout.getHeaderHeight(), 25);
        this.centerListVertically = false;
    }

    public void addAll(List<ClickableWidget> widgets) {
        for (int i = 0; i < widgets.size(); i += 2) {
            this.addWidgetEntry(widgets.get(i), i < widgets.size() - 1 ? widgets.get(i + 1) : null);
        }
    }

    public void addWidgetEntry(ClickableWidget firstWidget, @Nullable ClickableWidget secondWidget) {
        this.addEntry(WidgetEntry.create(firstWidget, secondWidget));
    }

    @Override
    public int getRowWidth() {
        if (this.children().isEmpty()) return this.width;
        AtomicInteger longest = new AtomicInteger();
        this.children().forEach(entry -> longest.set(Math.max(longest.get(), entry.widgets.getFirst().getWidth())));
        return longest.get();
    }

    @Override
    public int getRowLeft() {
        return (this.width - this.getRowWidth()) / 2;
    }

    @Environment(EnvType.CLIENT)
    protected static class WidgetEntry extends Entry<WidgetEntry> {
        private final List<ClickableWidget> widgets;

        WidgetEntry(List<ClickableWidget> widgets) {
            this.widgets = ImmutableList.copyOf(widgets);
        }

        public static WidgetEntry create(ClickableWidget firstWidget, @Nullable ClickableWidget secondWidget) {
            return secondWidget == null
                    ? new WidgetEntry(ImmutableList.of(firstWidget))
                    : new WidgetEntry(ImmutableList.of(firstWidget, secondWidget));
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int i = 0;
            for (ClickableWidget clickableWidget : this.widgets) {
                clickableWidget.setPosition(x + i, y);
                clickableWidget.render(context, mouseX, mouseY, tickDelta);
                i += 160;
            }
        }

        @Override
        public List<? extends Element> children() {
            return this.widgets;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return this.widgets;
        }
    }
}
