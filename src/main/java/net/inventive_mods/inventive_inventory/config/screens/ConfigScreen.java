package net.inventive_mods.inventive_inventory.config.screens;

import net.inventive_mods.inventive_inventory.config.screens.tabs.ConfigOptionsTab;
import net.inventive_mods.inventive_inventory.config.screens.tabs.ConfigProfilesTab;
import net.inventive_mods.inventive_inventory.config.screens.tabs.ConfigVisualsTab;
import net.inventive_mods.inventive_inventory.util.widgets.TabbedScreen;
import net.minecraft.client.gui.screen.Screen;


public class ConfigScreen extends TabbedScreen {
    public ConfigScreen(Screen parent) {
        super(parent, "config.screen.title.inventive_inventory.main");
    }

    @Override
    protected void addTabs() {
        this.addTab("options", new ConfigOptionsTab(this.client, this.width, this), true);
        this.addTab("visuals", new ConfigVisualsTab(this.client, this.width, this), false);
        this.addTab("profiles", new ConfigProfilesTab(this.client, this.width, this), false);
    }
}
