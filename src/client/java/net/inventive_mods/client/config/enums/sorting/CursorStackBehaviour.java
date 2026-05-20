package net.inventive_mods.client.config.enums.sorting;


import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.enums.accessors.Translatable;
import net.inventive_mods.client.keys.handler.AdvancedOperationHandler;
import net.minecraft.network.chat.Component;

public enum CursorStackBehaviour implements Translatable {
    AOK_DEPENDENT("aok_dependent"),
    AOK_DEPENDENT_INVERTED("aok_dependent_inverted"),
    @SuppressWarnings("unused") SORT_CURSOR_STACK("sort_cursor_stack"),
    KEEP_CURSOR_STACK("keep_cursor_stack");

    private final String translationKey;

    CursorStackBehaviour(String translationKey) {
        this.translationKey = "sorting.cursor_stack_behaviour." + translationKey;
    }

    public static boolean isValid() {
        return ConfigManager.CURSOR_STACK_BEHAVIOUR.is(KEEP_CURSOR_STACK) ||
                ConfigManager.CURSOR_STACK_BEHAVIOUR.is(AOK_DEPENDENT) && AdvancedOperationHandler.isPressed() ||
                ConfigManager.CURSOR_STACK_BEHAVIOUR.is(AOK_DEPENDENT_INVERTED) && !AdvancedOperationHandler.isPressed();
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
