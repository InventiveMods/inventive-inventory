package net.inventive_mods.inventive_inventory.config.screens.widgets;

import net.inventive_mods.inventive_inventory.config.options.fields.ColorFieldOption;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

public class ConfigSliderWidget extends SliderWidget {
    private final ColorFieldOption option;
    private int opacity;

    public ConfigSliderWidget(int width, int height, double value, ColorFieldOption option) {
        super(0, 0, width, height, Text.translatable("config.visuals.slider.text.inventive_inventory.locked_slots.color.opacity", (int) (value * 255)), value);
        this.option = option;
        this.opacity = (int) (this.value * 255);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.translatable("config.visuals.slider.text.inventive_inventory.locked_slots.color.opacity", this.opacity));
    }

    @Override
    protected void applyValue() {
        this.opacity = (int) (this.value * 255);
        this.option.setValue(ColorHelper.withAlpha(this.opacity, this.option.getValue() & 0x00FFFFFF));
    }

    @Override
    protected void onDrag(Click click, double deltaX, double deltaY) {
        super.onDrag(click, deltaX, deltaY);
    }

    public void reset() {
        this.setValue((double) ColorHelper.getAlpha(this.option.getDefaultValue()) / 255);
    }
}
