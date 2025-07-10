package net.inventive_mods.inventive_inventory.config.gui.tab;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.util.gui.screen.TabbedScreen;
import net.inventive_mods.inventive_inventory.util.gui.widget.ScreenTab;
import net.minecraft.client.Minecraft;


public class ConfigOptionsTab extends ScreenTab {
    private final static String TITLE_TRANSLATION_KEY = "config.options.title." + InventiveInventory.MOD_ID;

    public ConfigOptionsTab(Minecraft minecraft, int width, TabbedScreen screen) {
        super(minecraft, width, screen);
    }
}
