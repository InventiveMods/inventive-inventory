package net.inventive_mods.client.config.screens;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.screens.tabs.ConfigOptionsTab;
import net.inventive_mods.client.config.screens.tabs.ConfigProfilesTab;
import net.inventive_mods.client.config.screens.tabs.ConfigVisualsTab;
import net.inventive_mods.client.util.widgets.TabbedScreen;
import net.minecraft.client.gui.screens.Screen;

public class ConfigScreen extends TabbedScreen {
    public ConfigScreen(Screen lastScreen) {
        super(lastScreen, "config.screen.title." + InventiveInventoryClient.MOD_ID + ".main");
    }

    @Override
    protected void addTabs() {
        this.addTab("options", new ConfigOptionsTab(this.minecraft, this.width, this));
        this.addTab("visuals", new ConfigVisualsTab(this.minecraft, this.width, this));
        this.addTab("profiles", new ConfigProfilesTab(this.minecraft, this.width, this));
    }
}
