package net.inventive_mods.inventive_inventory.config.gui.widget.locked_slots;

import net.inventive_mods.inventive_inventory.config.option.field.ColorFieldOption;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

public class ColorOpacitySliderWidget extends AbstractSliderButton {
    private final ColorFieldOption option;
    private int opacity;

public ColorOpacitySliderWidget(int width, int height, double value, ColorFieldOption option) {
    super(0, 0, width, height, Component.translatable("config.visuals.slider.text.inventive_inventory.locked_slots.color.opacity", (int) (value * 255)), value);
        this.option = option;
        this.opacity = (int) (this.value * 255);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.translatable("config.visuals.slider.text.inventive_inventory.locked_slots.color.opacity", this.opacity));
    }

    @Override
    protected void applyValue() {
        this.opacity = (int) (this.value * 255);
        this.option.setValue(ARGB.color(this.opacity, this.option.getValue() & 0x00FFFFFF));
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        super.onDrag(mouseX, mouseY, dragX, dragY);
    }

    public void reset() {
        double oldValue = this.value;
        this.value = Mth.clamp(((double) ARGB.alpha(this.option.getDefaultValue()) / 255), 0.0F, 1.0F);
        if (oldValue != this.value) {
            this.applyValue();
        }
        this.updateMessage();
    }
}
