package net.inventive_mods.inventive_inventory.config.enums.locked_slots;

import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Translatable;
import net.minecraft.network.chat.Component;

public enum SlotStyle implements Translatable {
    FILLED("filled"),
    OUTLINED("outlined");

    private final String translationKey;

    SlotStyle(String translationKey) {
        this.translationKey = "locked_slots.style." + translationKey;
    }

    @Override
    public Component getButtonText() {
        return Component.translatable(Config.VISUALS_TRANSLATION_KEY + "." + this.translationKey);
    }

    @Override
    public String getTranslationKey() {
        return this.translationKey;
    }
}
