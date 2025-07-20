package net.inventive_mods.inventive_inventory.config.option.field;

import net.inventive_mods.inventive_inventory.config.gui.widget.locked_slots.ColorPickerWidget;
import net.inventive_mods.inventive_inventory.config.option.ConfigOption;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jetbrains.annotations.Nullable;

public class ColorFieldOption extends ConfigOption<Integer> {
    public ColorFieldOption(String tab, String key, int defaultValue) {
        super(tab, key, defaultValue);
    }

    @Override
    public void setValue(@Nullable String value) {
        if (value != null) this.setValue(Integer.parseInt(value));
    }

    @Override
    public AbstractWidget asWidget() {
        return new ColorPickerWidget(this);
    }
}
