package net.inventive_mods.inventive_inventory.config.option;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Stylable;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Translatable;
import net.inventive_mods.inventive_inventory.util.gui.widget.TextWidget;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigOption<T> {
    protected static final String SIMPLE_TRANSLATION_KEY = "config.universal.button.text." + InventiveInventory.MOD_ID + ".universal.simple.";
    protected final String tab;
    private final String key;
    private final T defaultValue;
    private T value;

    public static Component getValueAsText(Object value) {
        if (value instanceof Boolean) return (Boolean) value ? Component.translatable(SIMPLE_TRANSLATION_KEY + "yes") : Component.translatable(SIMPLE_TRANSLATION_KEY + "no");
        else if (value instanceof Enum<?>) {
            Component text = Component.empty();
            if (value instanceof Translatable) text = ((Translatable) value).getButtonText();
            if (value instanceof Stylable) text = text.copy().setStyle(((Stylable) value).getStyle());
            return text;
        } else return Component.empty();
    }

    public ConfigOption(String tab, String key, T defaultValue) {
        this.tab = tab;
        this.key = key;
        this.value = this.defaultValue = defaultValue;
    }

    public boolean is(T value) {
        return this.value == value;
    }

    public String getTranslationKey() {
        return "config." + this.tab +  ".label." + InventiveInventory.MOD_ID + "." + this.key;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
        Config.save();
    }

    public void reset() {
        this.setValue(this.defaultValue);
    }

    public TextWidget createLabel() {
        return new TextWidget(Component.translatable(this.getTranslationKey()), InventiveInventory.getFont());
    }

    public abstract void setValue(@Nullable String value);

    public abstract AbstractButton asWidget();
}
