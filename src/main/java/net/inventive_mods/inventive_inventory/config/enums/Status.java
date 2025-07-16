package net.inventive_mods.inventive_inventory.config.enums;

import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Stylable;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Translatable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public enum Status implements Stylable, Translatable {
    ENABLED("enabled", ChatFormatting.GREEN),
    DISABLED("disabled", ChatFormatting.RED);

    private final String translationKey;
    private final Style style;

    Status(String translationKey, ChatFormatting color) {
        this.translationKey = "universal.status." + translationKey;
        this.style = Style.EMPTY.withColor(color);
    }

    @Override
    public Style getStyle() {
        return this.style;
    }

    @Override
    public Component getButtonText() {
        return Component.translatable(Config.OPTION_TRANSLATION_KEY + "." + this.translationKey);
    }

    @Override
    public String getTranslationKey() {
        return this.translationKey;
    }
}
