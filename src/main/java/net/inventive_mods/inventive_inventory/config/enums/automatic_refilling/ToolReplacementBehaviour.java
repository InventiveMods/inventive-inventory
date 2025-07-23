package net.inventive_mods.inventive_inventory.config.enums.automatic_refilling;

import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Translatable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public enum ToolReplacementBehaviour implements Translatable {
    KEEP_TOOL("keep_tool"),
    BREAK_TOOL("break_tool");

    private final String translationKey;

    ToolReplacementBehaviour(String translationKey) {
        this.translationKey = "automatic_refilling.tool_replacement_behaviour." + translationKey;
    }

    public static boolean isValid(ItemStack stack) {
        return Config.TOOL_REPLACEMENT_BEHAVIOUR.is(KEEP_TOOL) && stack.getMaxDamage() - stack.getDamageValue() == 2 ||
                stack.getMaxDamage() - stack.getDamageValue() == 1;
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
