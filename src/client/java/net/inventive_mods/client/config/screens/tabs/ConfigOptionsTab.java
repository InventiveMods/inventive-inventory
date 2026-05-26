package net.inventive_mods.client.config.screens.tabs;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.screens.ConfigScreen;
import net.inventive_mods.client.util.widgets.ScreenTab;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ConfigOptionsTab extends ScreenTab {
    private final static String TITLE_TRANSLATION_KEY = "config.options.title." + InventiveInventoryClient.MOD_ID;

    public ConfigOptionsTab(Minecraft client, int width, ConfigScreen screen) {
        super(client, width, screen);

        this.addTitle(Component.translatable(TITLE_TRANSLATION_KEY + ".sorting"));
        this.addOption(ConfigManager.SORTING_STATUS);
        this.addOption(ConfigManager.SORTING_MODE);
        this.addOption(ConfigManager.CURSOR_STACK_BEHAVIOUR);

        this.addTitle(Component.translatable(TITLE_TRANSLATION_KEY + ".automatic_refilling"));
        this.addOption(ConfigManager.AUTOMATIC_REFILLING_STATUS);
        this.addOption(ConfigManager.AUTOMATIC_REFILLING_MODE);
        this.addOption(ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR);
        this.addOption(ConfigManager.TOOL_REPLACEMENT_PRIORITY);
        this.addOption(ConfigManager.AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS);
        this.addOption(ConfigManager.AUTOMATIC_REFILLING_IGNORE_BUCKETS);

        this.addTitle(Component.translatable(TITLE_TRANSLATION_KEY + ".profiles"));
        this.addOption(ConfigManager.PROFILES_STATUS);
        this.addOption(ConfigManager.FAST_LOAD);
        this.addOption(ConfigManager.PROFILES_IGNORE_LOCKED_SLOTS);

        this.addTitle(Component.translatable(TITLE_TRANSLATION_KEY + ".locked_slots"));
        this.addOption(ConfigManager.PICKUP_INTO_LOCKED_SLOTS);
        this.addOption(ConfigManager.QUICK_MOVE_INTO_LOCKED_SLOTS);

        this.addTitle(Component.translatable(TITLE_TRANSLATION_KEY + ".item_counter"));
        this.addOption(ConfigManager.ITEM_COUNTER_STATUS);
        this.addOption(ConfigManager.ITEM_COUNTER_MODE);
        this.addOption(ConfigManager.ITEM_COUNTER_COUNTING_MODE);
        this.addOption(ConfigManager.ITEM_COUNTER_IGNORE_LOCKED_SLOTS);
    }
}
