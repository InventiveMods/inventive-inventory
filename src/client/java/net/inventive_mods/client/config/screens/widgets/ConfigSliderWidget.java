package net.inventive_mods.client.config.screens.widgets;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.options.fields.ColorFieldOption;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.NonNull;

public class ConfigSliderWidget extends AbstractSliderButton {
    private final ColorFieldOption option;
    private int opacity;

    public ConfigSliderWidget(int width, int height, double value, ColorFieldOption option) {
        super(0, 0, width, height, Component.translatable("config.visuals.slider.text." + InventiveInventoryClient.MOD_ID + ".locked_slots.color.opacity", (int) (value * 255)), value);
        this.option = option;
        this.opacity = (int) (this.value * 255);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.translatable("config.visuals.slider.text." + InventiveInventoryClient.MOD_ID + ".locked_slots.color.opacity", this.opacity));
    }

    @Override
    protected void applyValue() {
        this.opacity = (int) (this.value * 255);
        this.option.setValue(ARGB.color(this.opacity, this.option.getValue() & 0x00FFFFFF));
    }

    @Override
    protected void onDrag(@NonNull MouseButtonEvent click, double deltaX, double deltaY) {
        super.onDrag(click, deltaX, deltaY);
    }

    public void reset() {
        this.setValue((double) ARGB.alpha(this.option.getDefaultValue()) / 255);
    }
}
