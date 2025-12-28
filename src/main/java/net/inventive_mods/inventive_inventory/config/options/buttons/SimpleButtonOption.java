package net.inventive_mods.inventive_inventory.config.options.buttons;

import net.inventive_mods.inventive_inventory.config.options.ConfigOption;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;
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
            if (value.equals("true") || value.equals(Text.translatable(SIMPLE_TRANSLATION_KEY + "yes").getString()))
                this.setValue(true);
            else if (value.equals("false") || value.equals(Text.translatable(SIMPLE_TRANSLATION_KEY + "no").getString()))
                this.setValue(false);
        }
    }

    @Override
    public CyclingButtonWidget<?> asWidget() {
        return CyclingButtonWidget.builder(ConfigOption::getValueAsText, this.getValue())
                .omitKeyText()
                .values(List.of(true, false))
                .build(Text.empty(), (button, value) -> this.cycle());
    }
}
