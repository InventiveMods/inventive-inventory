package net.inventive_mods.client.config.screens.tabs;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.screens.ConfigScreen;
import net.inventive_mods.client.config.screens.widgets.ConfigProfileTitleWidget;
import net.inventive_mods.client.config.screens.widgets.ConfigProfileWidget;
import net.inventive_mods.client.features.profiles.Profile;
import net.inventive_mods.client.features.profiles.ProfileHandler;
import net.inventive_mods.client.util.widgets.CenteredTextWidget;
import net.inventive_mods.client.util.widgets.ScreenTab;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ConfigProfilesTab extends ScreenTab {
    public List<Component> availableKeys = new ArrayList<>();

    public ConfigProfilesTab(Minecraft client, int width, ConfigScreen screen) {
        super(client, width, screen);
        availableKeys = getAvailableKeys();
        if (ProfileHandler.getProfiles().isEmpty()) {
            this.addCenteredWidget(new CenteredTextWidget(this.getRowWidth(), this.getHeight(), Component.translatable("config.profiles.text." + InventiveInventoryClient.MOD_ID + ".no_profiles"), this.minecraft.font));
        } else {
            this.initTitleBar();
            this.initEntries();
        }
    }

    private void initTitleBar() {
        this.addCenteredWidget(new ConfigProfileTitleWidget(0, 20));
    }

    private void initEntries() {
        int i = 1;
        for (Profile profile : ProfileHandler.getProfiles()) {
            this.addCenteredWidget(new ConfigProfileWidget(0, 20, i, profile, this));
            i++;
        }
    }

    private List<Component> getAvailableKeys() {
        List<KeyMapping> availableBindings = ProfileHandler.getAvailableProfileKeys();
        List<Component> finalBindings = new ArrayList<>();
        availableBindings.forEach(binding -> {
            if (binding != null) finalBindings.add(binding.getTranslatedKeyMessage());
        });
        finalBindings.add(Component.translatable("config.profiles.button.text." + InventiveInventoryClient.MOD_ID + ".not_bound"));
        return finalBindings;
    }

    @Override
    public boolean mouseClicked(@NonNull MouseButtonEvent click, boolean doubled) {
        this.children().forEach(widgetEntry -> widgetEntry.children().forEach(element -> {
            if (element instanceof ConfigProfileWidget widget)
                widget.horizontal.visitWidgets(clickableWidget -> clickableWidget.setFocused(false));
        }));
        return super.mouseClicked(click, doubled);
    }

    @Override
    protected void onClose() {
        this.children().forEach(widgetEntry -> widgetEntry.children().forEach(element -> {
            if (element instanceof ConfigProfileWidget widget) widget.updateProfile();
        }));
    }
}
