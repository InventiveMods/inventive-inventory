package net.inventive_mods.inventive_inventory.util.gui.widget;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class ClickableWidget extends AbstractWidget {
    public ClickableWidget(int width, int height) {
        super(0, 0, width, height, Component.empty());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
