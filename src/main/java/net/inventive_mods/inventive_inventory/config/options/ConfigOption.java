package net.inventive_mods.inventive_inventory.config.options;

import net.inventive_mods.inventive_inventory.util.widgets.CenteredTextWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Stylable;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Translatable;
import net.inventive_mods.inventive_inventory.config.screens.widgets.ConfigTextWidget;
import org.jetbrains.annotations.Nullable;


public abstract class ConfigOption<T> {
    protected static final String SIMPLE_TRANSLATION_KEY = "config.universal.button.text." + InventiveInventory.MOD_ID + ".universal.simple.";
    protected final String tab;
    private final String key;
    private final T defaultValue;
    private T value;

    public static Text getValueAsText(Object value) {
        if (value instanceof Boolean) return (Boolean) value ? Text.translatable(SIMPLE_TRANSLATION_KEY + "yes") : Text.translatable(SIMPLE_TRANSLATION_KEY + "no");
        else if (value instanceof Enum<?>) {
            Text text = Text.empty();
            if (value instanceof Translatable) text = ((Translatable) value).getButtonText();
            if (value instanceof Stylable) text = text.copy().setStyle(((Stylable) value).getStyle());
            return text;
        } else return Text.empty();
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
        ConfigManager.save();
    }

    public void reset() {
        this.setValue(this.defaultValue);
    }

    public CenteredTextWidget createLabel() {
        return new ConfigTextWidget(Text.translatable(this.getTranslationKey()), InventiveInventory.getClient().textRenderer);
    }

    public abstract void setValue(@Nullable String value);

    public abstract ClickableWidget asWidget();
}
