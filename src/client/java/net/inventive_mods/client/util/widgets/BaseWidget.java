package net.inventive_mods.client.util.widgets;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public abstract class BaseWidget extends AbstractWidget {

    public BaseWidget(int width, int height) {
        super(0, 0, width, height, Component.empty());
    }

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {

    }
}
