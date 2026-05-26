package net.inventive_mods.client.config.enums.locked_slots;

import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.enums.accessors.Translatable;
import net.minecraft.network.chat.Component;

public enum Style implements Translatable {
    FILLED("filled"),
    OUTLINED("outlined");

    private final String translationKey;

    Style(String translationKey) {
        this.translationKey = "locked_slots.style." + translationKey;
    }

    @Override
    public Component getButtonText() {
        return Component.translatable(ConfigManager.VISUALS_TRANSLATION_KEY + "." + this.translationKey);
    }

    @Override
    public String getTranslationKey() {
        return this.translationKey;
    }
}
