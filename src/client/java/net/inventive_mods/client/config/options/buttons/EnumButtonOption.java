package net.inventive_mods.client.config.options.buttons;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.enums.accessors.Translatable;
import net.inventive_mods.client.config.options.ConfigOption;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

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
    public CycleButton<?> asWidget() {
        return CycleButton.builder(ConfigOption::getValueAsText, this.getValue())
                .withTooltip(value -> Tooltip.create(Component.translatable("config." + this.tab + ".button.tooltip." + InventiveInventoryClient.MOD_ID + "." + ((Translatable) value).getTranslationKey())))
                .displayOnlyValue()
                .withValues(this.enumClass.getEnumConstants())
                .create(Component.empty(), (_, _) -> this.cycle());
    }

}
