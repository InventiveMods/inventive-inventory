package net.inventive_mods.inventive_inventory.config.option.button;

import net.inventive_mods.inventive_inventory.config.option.ConfigOption;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimpleButtonOption extends ConfigOption<Boolean> {

    public SimpleButtonOption(String tab, String key, boolean defaultValue) {
        super(tab, key, defaultValue);
    }

    protected void cycle() {
        this.setValue(!this.getValue());
    }

    @Override
    public void setValue(@Nullable String value) {
        if (value != null) {
            if (value.equals("true") || value.equals(Component.translatable(SIMPLE_TRANSLATION_KEY + "yes").getString())) this.setValue(true);
            else if (value.equals("false") || value.equals(Component.translatable(SIMPLE_TRANSLATION_KEY + "no").getString())) this.setValue(false);
        }
    }

    @Override
    public CycleButton<?> asWidget() {
        return CycleButton.builder(ConfigOption::getValueAsText)
                .displayOnlyValue()
                .withValues(List.of(true, false)).withInitialValue(this.getValue())
                .create(Component.empty(), (button, value) -> this.cycle());
    }
}
