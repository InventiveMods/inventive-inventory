package net.inventive_mods.inventive_inventory.config.gui.tab;

import net.inventive_mods.inventive_inventory.config.gui.widget.profiles.ConfigProfileTitleWidget;
import net.inventive_mods.inventive_inventory.config.gui.widget.profiles.ConfigProfileWidget;
import net.inventive_mods.inventive_inventory.features.profile.Profile;
import net.inventive_mods.inventive_inventory.features.profile.ProfileHandler;
import net.inventive_mods.inventive_inventory.util.gui.screen.TabbedScreen;
import net.inventive_mods.inventive_inventory.util.gui.widget.ScreenTab;
import net.inventive_mods.inventive_inventory.util.gui.widget.TextWidget;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigProfilesTab extends ScreenTab {
    public List<Component> availableKeys = new ArrayList<>();

    public ConfigProfilesTab(Minecraft minecraft, int width, TabbedScreen screen) {
        super(minecraft, width, screen);
        availableKeys = getAvailableKeys();
        if (ProfileHandler.getProfiles().isEmpty()) {
            this.addCenteredWidget(new TextWidget(this.width, this.height, Component.translatable("config.profiles.text.inventive_inventory.no_profiles"), minecraft.font));
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.children().forEach(entry -> entry.children().forEach(guiEventListener -> {
            if (guiEventListener instanceof ConfigProfileWidget configProfileWidget)
                configProfileWidget.layout.visitWidgets(widget -> widget.setFocused(false));
        }));
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public int getRowWidth() {
        if (this.children().isEmpty()) return this.width;
        AtomicInteger longest = new AtomicInteger();
        this.children().forEach(entry -> longest.set(Math.max(longest.get(), entry.children().getFirst().getRectangle().width())));
        return longest.get();
    }

    @Override
    public void onClose() {
        this.children().forEach(entry -> entry.children().forEach(guiEventListener -> {
            if (guiEventListener instanceof ConfigProfileWidget configProfileWidget)
                configProfileWidget.updateProfile();
        }));
    }

    private List<Component> getAvailableKeys() {
        List<KeyMapping> availableBindings = ProfileHandler.getAvailableProfileKeys();
        List<Component> finalBindings = new ArrayList<>();
        availableBindings.forEach(binding -> {
            if (binding != null) finalBindings.add(binding.getTranslatedKeyMessage());
        });
        finalBindings.add(Component.translatable("config.profiles.button.text.inventive_inventory.not_bound"));
        return finalBindings;
    }
}
