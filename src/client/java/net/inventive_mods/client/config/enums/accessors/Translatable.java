package net.inventive_mods.client.config.enums.accessors;

import net.minecraft.network.chat.Component;

public interface Translatable {
    Component getButtonText();

    String getTranslationKey();
}
