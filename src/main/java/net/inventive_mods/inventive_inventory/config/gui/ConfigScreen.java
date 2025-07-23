package net.inventive_mods.inventive_inventory.config.gui;

import net.inventive_mods.inventive_inventory.config.gui.tab.ConfigOptionsTab;
import net.inventive_mods.inventive_inventory.config.gui.tab.ConfigProfilesTab;
import net.inventive_mods.inventive_inventory.config.gui.tab.ConfigVisualsTab;
import net.inventive_mods.inventive_inventory.util.gui.screen.TabbedScreen;
import net.minecraft.client.gui.screens.Screen;

public class ConfigScreen extends TabbedScreen {
    public ConfigScreen(Screen parent) {
        super(parent, "config.screen.title.inventive_inventory.main");
    }

    @Override
    protected void addTabs() {
        this.addTab("options", new ConfigOptionsTab(this.minecraft, this.width, this), true);
        this.addTab("visuals", new ConfigVisualsTab(this.minecraft, this.width, this), false);
        this.addTab("profiles", new ConfigProfilesTab(this.minecraft, this.width, this), false);
    }
}
