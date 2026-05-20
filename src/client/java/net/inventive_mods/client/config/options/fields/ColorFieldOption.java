package net.inventive_mods.client.config.options.fields;

import net.inventive_mods.client.config.options.ConfigOption;
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
//        return new ColorPickerWidget(this);
        return null;
    }
}
