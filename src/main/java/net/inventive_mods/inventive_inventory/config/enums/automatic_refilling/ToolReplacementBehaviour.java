package net.inventive_mods.inventive_inventory.config.enums.automatic_refilling;

import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.config.enums.accessors.Translatable;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public enum ToolReplacementBehaviour implements Translatable {
    KEEP_TOOL("keep_tool"),
    BREAK_TOOL("break_tool");

    private final String translationKey;

    ToolReplacementBehaviour(String translationKey) {
        this.translationKey = "automatic_refilling.tool_replacement_behaviour." + translationKey;
    }

    public static boolean isValid(ItemStack stack) {
        return ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR.is(KEEP_TOOL) && stack.getMaxDamage() - stack.getDamage() == 2 ||
                stack.getMaxDamage() - stack.getDamage() == 1;
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
