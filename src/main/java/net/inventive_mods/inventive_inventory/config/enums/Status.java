package net.inventive_mods.inventive_inventory.config.enums;

import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Stylable;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Translatable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public enum Status implements Stylable, Translatable {
    ENABLED("enabled", Formatting.GREEN),
    DISABLED("disabled", Formatting.RED);

    private final String translationKey;
    private final Style style;

    Status(String translationKey, Formatting color) {
        this.translationKey = "universal.status." + translationKey;
        this.style = Style.EMPTY.withColor(color);
    }

    @Override
    public Style getStyle() {
        return this.style;
    }

    @Override
    public Text getButtonText() {
        return Text.translatable(ConfigManager.OPTION_TRANSLATION_KEY + "." + this.translationKey);
    }

    @Override
    public String getTranslationKey() {
        return this.translationKey;
    }
}
