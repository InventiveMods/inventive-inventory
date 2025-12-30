package net.inventive_mods.inventive_inventory.config.enums.locked_slots;

import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Translatable;
import net.minecraft.text.Text;

public enum Style implements Translatable {
    FILLED("filled"),
    OUTLINED("outlined");

    private final String translationKey;

    Style(String translationKey) {
        this.translationKey = "locked_slots.style." + translationKey;
    }

    @Override
    public Text getButtonText() {
        return Text.translatable(ConfigManager.VISUALS_TRANSLATION_KEY + "." + this.translationKey);
    }

    @Override
    public String getTranslationKey() {
        return this.translationKey;
    }
}
