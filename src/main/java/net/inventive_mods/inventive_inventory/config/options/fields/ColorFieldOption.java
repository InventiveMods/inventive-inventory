package net.inventive_mods.inventive_inventory.config.options.fields;

import net.inventive_mods.inventive_inventory.config.options.ConfigOption;
import net.inventive_mods.inventive_inventory.config.screens.widgets.ColorPickerWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
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
    public ClickableWidget asWidget() {
        return new ColorPickerWidget(this);
    }
}
