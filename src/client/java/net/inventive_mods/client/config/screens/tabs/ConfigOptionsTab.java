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
        this.addWidget(ConfigManager.SORTING_STATUS);
        this.addWidget(ConfigManager.SORTING_MODE);
        this.addWidget(ConfigManager.CURSOR_STACK_BEHAVIOUR);

        this.addTitle(Component.translatable(TITLE_TRANSLATION_KEY + ".automatic_refilling"));
        this.addWidget(ConfigManager.AUTOMATIC_REFILLING_STATUS);
        this.addWidget(ConfigManager.AUTOMATIC_REFILLING_MODE);
        this.addWidget(ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR);
        this.addWidget(ConfigManager.TOOL_REPLACEMENT_PRIORITY);
        this.addWidget(ConfigManager.AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS);
        this.addWidget(ConfigManager.AUTOMATIC_REFILLING_IGNORE_BUCKETS);

        this.addTitle(Component.translatable(TITLE_TRANSLATION_KEY + ".profiles"));
        this.addWidget(ConfigManager.PROFILES_STATUS);
        this.addWidget(ConfigManager.FAST_LOAD);
        this.addWidget(ConfigManager.PROFILES_IGNORE_LOCKED_SLOTS);

        this.addTitle(Component.translatable(TITLE_TRANSLATION_KEY + ".locked_slots"));
        this.addWidget(ConfigManager.PICKUP_INTO_LOCKED_SLOTS);
        this.addWidget(ConfigManager.QUICK_MOVE_INTO_LOCKED_SLOTS);

        this.addTitle(Component.translatable(TITLE_TRANSLATION_KEY + ".item_counter"));
        this.addWidget(ConfigManager.ITEM_COUNTER_STATUS);
        this.addWidget(ConfigManager.ITEM_COUNTER_MODE);
        this.addWidget(ConfigManager.ITEM_COUNTER_COUNTING_MODE);
        this.addWidget(ConfigManager.ITEM_COUNTER_IGNORE_LOCKED_SLOTS);
    }
}
