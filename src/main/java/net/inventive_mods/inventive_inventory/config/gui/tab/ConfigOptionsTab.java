package net.inventive_mods.inventive_inventory.config.gui.tab;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.util.gui.screen.TabbedScreen;
import net.inventive_mods.inventive_inventory.util.gui.widget.ScreenTab;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;


public class ConfigOptionsTab extends ScreenTab {
    private final static String TITLE_TRANSLATION_KEY = "config.options.title." + InventiveInventory.MOD_ID;

    public ConfigOptionsTab(Minecraft minecraft, int width, TabbedScreen screen) {
        super(minecraft, width, screen);

        this.addTitle(Component.translatable(TITLE_TRANSLATION_KEY + ".locked_slots"));
        this.addConfigOption(Config.PICKUP_INTO_LOCKED_SLOTS);
        this.addConfigOption(Config.QUICK_MOVE_INTO_LOCKED_SLOTS);
    }
}
