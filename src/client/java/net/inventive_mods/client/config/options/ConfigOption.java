package net.inventive_mods.client.config.options;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.enums.accessors.Stylable;
import net.inventive_mods.client.config.enums.accessors.Translatable;
import net.inventive_mods.client.config.screens.widgets.ConfigTextWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;


public abstract class ConfigOption<T> {
    protected static final String SIMPLE_TRANSLATION_KEY = "config.universal.button.text." + InventiveInventoryClient.MOD_ID + ".universal.simple.";
    protected final String tab;
    private final String key;
    private final T defaultValue;
    private T value;

    public static Component getValueAsText(Object value) {
        if (value instanceof Boolean)
            return (Boolean) value ? Component.translatable(SIMPLE_TRANSLATION_KEY + "yes") : Component.translatable(SIMPLE_TRANSLATION_KEY + "no");
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
        return "config." + this.tab + ".label." + InventiveInventoryClient.MOD_ID + "." + this.key;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
        ConfigManager.save();
    }

    public void reset() {
        this.setValue(this.defaultValue);
    }

    public ConfigTextWidget createLabel() {
        return new ConfigTextWidget(Component.translatable(this.getTranslationKey()), InventiveInventoryClient.getClient().font);
    }

    public abstract void setValue(@Nullable String value);

    public abstract AbstractWidget asWidget();
}
