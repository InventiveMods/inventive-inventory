package net.inventive_mods.inventive_inventory.config.screens.tabs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.inventive_mods.inventive_inventory.config.screens.ConfigScreen;
import net.inventive_mods.inventive_inventory.config.screens.widgets.ConfigProfileTitleWidget;
import net.inventive_mods.inventive_inventory.config.screens.widgets.ConfigProfileWidget;
import net.inventive_mods.inventive_inventory.features.profiles.Profile;
import net.inventive_mods.inventive_inventory.features.profiles.ProfileHandler;
import net.inventive_mods.inventive_inventory.util.widgets.ScreenTab;

import java.util.ArrayList;
import java.util.List;

public class ConfigProfilesTab extends ScreenTab {
    public List<Text> availableKeys = new ArrayList<>();

    public ConfigProfilesTab(MinecraftClient client, int width, ConfigScreen screen) {
        super(client, width, screen);
        availableKeys = getAvailableKeys();
        if (ProfileHandler.getProfiles().isEmpty()) {
            this.addCenteredWidget(new TextWidget(this.width, this.height, Text.translatable("config.profiles.text.inventive_inventory.no_profiles"), this.client.textRenderer));
        } else {
            this.initTitleBar();
            this.initEntries();
        }
    }

    private void initTitleBar() {
        this.addCenteredWidget(new ConfigProfileTitleWidget(this.width, 20));
    }

    private void initEntries() {
        int i = 1;
        for (Profile profile : ProfileHandler.getProfiles()) {
            this.addCenteredWidget(new ConfigProfileWidget(this.width, 20, i, profile, this));
            i++;
        }
    }

    private List<Text> getAvailableKeys() {
        List<KeyBinding> availableBindings = ProfileHandler.getAvailableProfileKeys();
        List<Text> finalBindings = new ArrayList<>();
        availableBindings.forEach(binding -> {
            if (binding != null) finalBindings.add(binding.getBoundKeyLocalizedText());
        });
        finalBindings.add(Text.translatable("config.profiles.button.text.inventive_inventory.not_bound"));
        return finalBindings;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.children().forEach(widgetEntry -> widgetEntry.children().forEach(element -> {
            if (element instanceof ConfigProfileWidget widget) widget.horizontal.forEachChild(clickableWidget -> clickableWidget.setFocused(false));
        }));
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void onClose() {
        this.children().forEach(widgetEntry -> widgetEntry.children().forEach(element -> {
            if (element instanceof ConfigProfileWidget widget) widget.updateProfile();
        }));
    }
}
