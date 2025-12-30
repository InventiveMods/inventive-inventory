package net.inventive_mods.inventive_inventory.config.enums.automatic_refilling;

import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Translatable;
import net.minecraft.text.Text;

public enum ToolReplacementPriority implements Translatable {
    MATERIAL("material"),
    DURABILITY("durability");

    private final String translationKey;

    ToolReplacementPriority(String translationKey) {
        this.translationKey = "automatic_refilling.tool_replacement_priority." + translationKey;
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

