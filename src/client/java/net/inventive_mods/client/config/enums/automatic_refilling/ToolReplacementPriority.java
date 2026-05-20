package net.inventive_mods.client.config.enums.automatic_refilling;

import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.enums.accessors.Translatable;
import net.minecraft.network.chat.Component;

public enum ToolReplacementPriority implements Translatable {
    MATERIAL("material"),
    DURABILITY("durability");

    private final String translationKey;

    ToolReplacementPriority(String translationKey) {
        this.translationKey = "automatic_refilling.tool_replacement_priority." + translationKey;
    }

    @Override
    public Component getButtonText() {
        return Component.translatable(ConfigManager.OPTION_TRANSLATION_KEY + "." + this.translationKey);
    }

    @Override
    public String getTranslationKey() {
        return this.translationKey;
    }
}

