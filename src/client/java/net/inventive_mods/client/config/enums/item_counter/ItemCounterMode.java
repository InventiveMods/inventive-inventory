package net.inventive_mods.client.config.enums.item_counter;

import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.enums.accessors.Translatable;
import net.minecraft.network.chat.Component;

public enum ItemCounterMode implements Translatable {
    COMPLETE_HOTBAR("complete_hotbar"),
    ONLY_SELECTED_SLOT("only_selected_slot");

    private final String translationKey;

    ItemCounterMode(String translationKey) {
        this.translationKey = "item_counter.mode." + translationKey;
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
