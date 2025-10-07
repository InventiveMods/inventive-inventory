package net.inventive_mods.inventive_inventory.config.screens.tabs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.config.screens.ConfigScreen;
import net.inventive_mods.inventive_inventory.util.widgets.ScreenTab;

public class ConfigOptionsTab extends ScreenTab {
    private final static String TITLE_TRANSLATION_KEY = "config.options.title." + InventiveInventory.MOD_ID;

    public ConfigOptionsTab(MinecraftClient client, int width, ConfigScreen screen) {
        super(client, width, screen);

        this.addTitle(Text.translatable(TITLE_TRANSLATION_KEY + ".sorting"));
        this.addWidget(ConfigManager.SORTING_STATUS);
        this.addWidget(ConfigManager.SORTING_MODE);
        this.addWidget(ConfigManager.CURSOR_STACK_BEHAVIOUR);

        this.addTitle(Text.translatable(TITLE_TRANSLATION_KEY + ".automatic_refilling"));
        this.addWidget(ConfigManager.AUTOMATIC_REFILLING_STATUS);
        this.addWidget(ConfigManager.AUTOMATIC_REFILLING_MODE);
        this.addWidget(ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR);
        this.addWidget(ConfigManager.TOOL_REPLACEMENT_PRIORITY);
        this.addWidget(ConfigManager.AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS);
        this.addWidget(ConfigManager.AUTOMATIC_REFILLING_IGNORE_BUCKETS);

        this.addTitle(Text.translatable(TITLE_TRANSLATION_KEY + ".profiles"));
        this.addWidget(ConfigManager.PROFILES_STATUS);
        this.addWidget(ConfigManager.FAST_LOAD);
        this.addWidget(ConfigManager.PROFILES_IGNORE_LOCKED_SLOTS);

        this.addTitle(Text.translatable(TITLE_TRANSLATION_KEY + ".locked_slots"));
        this.addWidget(ConfigManager.PICKUP_INTO_LOCKED_SLOTS);
        this.addWidget(ConfigManager.QUICK_MOVE_INTO_LOCKED_SLOTS);

        this.addTitle(Text.translatable(TITLE_TRANSLATION_KEY + ".item_counter"));
        this.addWidget(ConfigManager.ITEM_COUNTER_STATUS);
        this.addWidget(ConfigManager.ITEM_COUNTER_MODE);
        this.addWidget(ConfigManager.ITEM_COUNTER_COUNTING_MODE);
        this.addWidget(ConfigManager.ITEM_COUNTER_IGNORE_LOCKED_SLOTS);
    }
}
