package net.inventive_mods.inventive_inventory.config.options.buttons;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Translatable;
import net.inventive_mods.inventive_inventory.config.options.ConfigOption;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class EnumButtonOption<E extends Enum<E>> extends ConfigOption<E> {
    private final Class<E> enumClass;

    public EnumButtonOption(String tab, String translationKey, E defaultValue) {
        super(tab, translationKey, defaultValue);
        this.enumClass = defaultValue.getDeclaringClass();
    }

    protected void cycle() {
        E[] values = enumClass.getEnumConstants();
        this.setValue(values[(this.getValue().ordinal() + 1) % values.length]);
    }

    @Override
    public void setValue(@Nullable String value) {
        for (E config : enumClass.getEnumConstants()) {
            if (config.toString().equalsIgnoreCase(value)) {
                this.setValue(config);
                return;
            } else if (config instanceof Translatable) {
                if (((Translatable) config).getButtonText().getString().equalsIgnoreCase(value)) {
                    this.setValue(config);
                    return;
                }
            }
        }
    }

    @Override
    public CyclingButtonWidget<?> asWidget() {
        return CyclingButtonWidget.builder(ConfigOption::getValueAsText)
                .tooltip(value -> Tooltip.of(Text.translatable("config." + this.tab + ".button.tooltip." + InventiveInventory.MOD_ID + "." + ((Translatable) value).getTranslationKey())))
                .omitKeyText()
                .values(Arrays.stream(this.enumClass.getEnumConstants()).toArray()).initially(this.getValue())
                .build(Text.empty(), (button, value) -> this.cycle());
    }

}
