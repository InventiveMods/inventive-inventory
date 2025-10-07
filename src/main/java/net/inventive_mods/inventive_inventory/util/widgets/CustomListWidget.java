package net.inventive_mods.inventive_inventory.util.widgets;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.inventive_mods.inventive_inventory.config.screens.ConfigScreen;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class CustomListWidget extends ElementListWidget<CustomListWidget.WidgetEntry> {
    private final ConfigScreen screen;

    public CustomListWidget(MinecraftClient client, int width, ConfigScreen screen) {
        super(client, width, screen.layout.getContentHeight(), screen.layout.getHeaderHeight(), 25);
        this.centerListVertically = false;
        this.screen = screen;
    }

    public void addAll(List<ClickableWidget> widgets) {
        for (int i = 0; i < widgets.size(); i += 2) {
            this.addWidgetEntry(widgets.get(i), i < widgets.size() - 1 ? widgets.get(i + 1) : null);
        }
    }

    public void addWidgetEntry(ClickableWidget firstWidget, @Nullable ClickableWidget secondWidget) {
        this.addEntry(WidgetEntry.create(this.screen, firstWidget, secondWidget));
    }

    public void addSpecialWidget(ClickableWidget widget) {
        this.addEntry(WidgetEntry.createSpecial(this.screen, widget));
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
        private final ConfigScreen screen;
        private final boolean special;

        WidgetEntry(List<ClickableWidget> widgets, ConfigScreen screen, boolean special) {
            this.widgets = ImmutableList.copyOf(widgets);
            this.screen = screen;
            this.special = special;
        }

        public static WidgetEntry create(ConfigScreen screen, ClickableWidget firstWidget, @Nullable ClickableWidget secondWidget) {
            return secondWidget == null
                    ? new WidgetEntry(ImmutableList.of(firstWidget), screen, false)
                    : new WidgetEntry(ImmutableList.of(firstWidget, secondWidget), screen, false);
        }

        public static WidgetEntry createSpecial(ConfigScreen screen, ClickableWidget widget) {
            return new WidgetEntry(ImmutableList.of(widget), screen, true);
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            if (this.special) {
                ClickableWidget widget = this.widgets.getFirst();
                widget.setPosition(this.screen.width / 2 - widget.getWidth() / 2, this.getContentY());
                widget.render(context, mouseX, mouseY, deltaTicks);
            } else {
                int i = 0;
                int x = this.screen.width / 2 - 155;

                for(ClickableWidget clickableWidget : this.widgets) {
                    clickableWidget.setPosition(x + i, this.getContentY());
                    clickableWidget.render(context, mouseX, mouseY, deltaTicks);
                    i += 160;
                }
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
