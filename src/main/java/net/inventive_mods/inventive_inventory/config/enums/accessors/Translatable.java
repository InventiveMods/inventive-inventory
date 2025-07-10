package net.inventive_mods.inventive_inventory.config.enums.accessors;

import net.minecraft.network.chat.Component;

public interface Translatable {
    Component getButtonText();
    String getTranslationKey();
}
