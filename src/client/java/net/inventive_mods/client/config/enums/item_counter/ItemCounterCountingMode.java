package net.inventive_mods.client.config.enums.item_counter;

import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.enums.accessors.Translatable;
import net.minecraft.network.chat.Component;

public enum ItemCounterCountingMode implements Translatable {
    ITEMS("items"),
    STACKS("stacks");

    private final String translationKey;

    ItemCounterCountingMode(String translationKey) {
        this.translationKey = "item_counter.counting_mode." + translationKey;
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
