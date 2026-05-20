package net.inventive_mods.client.config.enums.automatic_refilling;

import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.enums.accessors.Translatable;
import net.inventive_mods.client.keys.handler.AdvancedOperationHandler;
import net.minecraft.network.chat.Component;

public enum AutomaticRefillingMode implements Translatable {
    AUTOMATIC("automatic"),
    SEMI_AUTOMATIC("semi_automatic");

    private final String translationKey;

    AutomaticRefillingMode(String translationKey) {
        this.translationKey = "automatic_refilling.mode." + translationKey;
    }

    public static boolean isValid() {
        return ConfigManager.AUTOMATIC_REFILLING_MODE.is(SEMI_AUTOMATIC) && AdvancedOperationHandler.isPressed() ||
                ConfigManager.AUTOMATIC_REFILLING_MODE.is(AUTOMATIC) && !AdvancedOperationHandler.isPressed();
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