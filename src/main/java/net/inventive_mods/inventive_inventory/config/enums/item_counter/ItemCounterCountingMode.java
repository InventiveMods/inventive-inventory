package net.inventive_mods.inventive_inventory.config.enums.item_counter;

import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Translatable;
import net.minecraft.text.Text;

public enum ItemCounterCountingMode implements Translatable {
    ITEMS("items"),
    STACKS("stacks");

    private final String translationKey;

    ItemCounterCountingMode(String translationKey) {
        this.translationKey = "item_counter.counting_mode." + translationKey;
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
