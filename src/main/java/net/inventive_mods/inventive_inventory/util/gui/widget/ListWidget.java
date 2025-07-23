package net.inventive_mods.inventive_inventory.util.gui.widget;

import net.inventive_mods.inventive_inventory.util.gui.screen.TabbedScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.util.List;

public class ListWidget extends ContainerObjectSelectionList<ListWidget.Entry> {
    public ListWidget(Minecraft minecraft, int width, TabbedScreen screen) {
        super(minecraft, width, screen.layout.getContentHeight(), screen.layout.getHeaderHeight(), 25);
        this.centerListVertically = false;
    }

    @Override
    public int getRowWidth() {
        return 310;
    }

    @Override
    public int getRowLeft() {
        return (this.width - this.getRowWidth()) / 2;
    }

    protected static class Entry extends ContainerObjectSelectionList.Entry<ListWidget.Entry> {
        private final List<AbstractWidget> children;

        public Entry(List<AbstractWidget> children) {
            this.children = children;
        }

        public static Entry create(AbstractWidget left, @Nullable AbstractWidget right) {
            return right == null
                    ? new Entry(ImmutableList.of(left))
                    : new Entry(ImmutableList.of(left, right));
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            int i = 0;
            for (AbstractWidget widget : children) {
                widget.setPosition(left + i, top);
                widget.render(guiGraphics, mouseX, mouseY, partialTick);
                i += 160;
            }
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return this.children;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return this.children;
        }
    }
}
