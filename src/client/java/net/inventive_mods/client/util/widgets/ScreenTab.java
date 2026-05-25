package net.inventive_mods.client.util.widgets;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.options.ConfigOption;
import net.inventive_mods.client.config.screens.widgets.ConfigTextWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ScreenTab extends OptionsList {
    private final OptionsSubScreen screen;

    public ScreenTab(Minecraft minecraft, int width, OptionsSubScreen screen) {
        super(minecraft, width, screen);
        this.screen = screen;
    }

    protected void addTitle(Component title) {
        StringWidget text = new ConfigTextWidget(this.getRowWidth(), this.minecraft.font.lineHeight, title.copy().setStyle(Style.EMPTY.withBold(true)), this.minecraft.font);
        this.addCenteredWidget(text);
    }

    protected void addOption(ConfigOption<?> option) {
        this.addEntry(RowEntry.of(option.createLabel(), option.asWidget(), this.screen));
    }

    protected void addEmptyRow() {
        StringWidget textWidget = new StringWidget(Component.empty(), InventiveInventoryClient.getClient().font);
        this.addCenteredWidget(textWidget);
    }

    protected void addCenteredWidget(AbstractWidget widget) {
        this.addEntry(RowEntry.of(widget, null, this.screen));
    }

    protected void onClose() {

    }

    @Override
    public int getRowWidth() {
        if (this.children().isEmpty()) return super.getRowWidth();
        AtomicInteger longest = new AtomicInteger();
        this.children().forEach(entry -> longest.set(Math.max(longest.get(), entry.children().getFirst().getRectangle().width())));
        return longest.get();
    }


    protected static class RowEntry extends OptionsList.AbstractEntry {
        private final List<AbstractWidget> children;
        private final Screen screen;

        public RowEntry(List<AbstractWidget> children, Screen screen) {
            this.children = children;
            this.screen = screen;
        }

        public static RowEntry of(AbstractWidget left, @Nullable AbstractWidget right, Screen screen) {
            return new RowEntry(right == null ? List.of(left) : List.of(left, right), screen);
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
            if (this.children.size() == 1) {
                AbstractWidget widget = this.children.getFirst();
                widget.setPosition(this.screen.width / 2 - widget.getWidth() / 2, this.getContentY());
                widget.extractRenderState(graphics, mouseX, mouseY, a);
            } else {
                int xOffset = 0;
                int x = this.screen.width / 2 - 155;

                for (AbstractWidget widget : this.children) {
                    widget.setPosition(x + xOffset, this.getContentY());
                    widget.extractRenderState(graphics, mouseX, mouseY, a);
                    xOffset += 160;
                }
            }
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return this.children;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return this.children;
        }
    }
}
