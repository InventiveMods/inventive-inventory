package net.inventive_mods.client.config.screens.tabs;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.config.screens.ConfigScreen;
import net.inventive_mods.client.config.screens.widgets.ColorPickerWidget;
import net.inventive_mods.client.config.screens.widgets.ConfigItemCounterSlotWidget;
import net.inventive_mods.client.config.screens.widgets.ConfigLockedSlotWidget;
import net.inventive_mods.client.util.Textures;
import net.inventive_mods.client.util.widgets.ScreenTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigVisualsTab extends ScreenTab {
    private final static String TITLE_TRANSLATION_KEY = "config.visuals.title." + InventiveInventoryClient.MOD_ID;

    public ConfigVisualsTab(Minecraft client, int width, ConfigScreen screen) {
        super(client, width, screen);

        this.addTitle(Component.translatable(TITLE_TRANSLATION_KEY + ".locked_slots"));
        this.addOption(ConfigManager.SHOW_LOCK);
        this.addOption(ConfigManager.LOCKED_SLOT_STYLE);
        this.addOption(ConfigManager.LOCKED_SLOTS_COLOR);
        this.addEmptyRow();
        this.addCenteredWidget(new ConfigLockedSlotWidget(Textures.SLOT, ConfigManager.LOCKED_SLOTS_COLOR, this.getRowWidth(), 20));
        this.addOption(ConfigManager.LOCKED_SLOTS_HOTBAR_COLOR);
        this.addEmptyRow();
        this.addCenteredWidget(new ConfigLockedSlotWidget(Textures.HOTBAR_SLOT, ConfigManager.LOCKED_SLOTS_HOTBAR_COLOR, this.getRowWidth(), 20));
        this.addTitle(Component.translatable(TITLE_TRANSLATION_KEY + ".item_counter"));
        this.addOption(ConfigManager.ITEM_COUNTER_COLOR);
        this.addEmptyRow();
        this.addCenteredWidget(new ConfigItemCounterSlotWidget(this.getRowWidth(), 20));
    }

    @Override
    public boolean mouseClicked(@NonNull MouseButtonEvent click, boolean doubled) {
        AtomicBoolean bl = new AtomicBoolean(super.mouseClicked(click, doubled));
        for (AbstractEntry entry : this.children()) {
            entry.children().forEach(widget -> {
                if (widget instanceof ColorPickerWidget colorPickerWidget) {
                    if (colorPickerWidget.overSliderWidget(click) && click.button() == 0) {
                        colorPickerWidget.clickSliderWidget(click, doubled);
                        this.setDragging(true);
                        bl.set(true);
                    }
                }
            });
        }
        return bl.get();
    }

    @Override
    public boolean mouseDragged(@NonNull MouseButtonEvent click, double deltaX, double deltaY) {
        for (AbstractEntry entry : this.children()) {
            entry.children().forEach(widget -> {
                if (widget instanceof ColorPickerWidget colorPickerWidget) {
                    if (colorPickerWidget.overSliderWidget(click) && click.button() == 0)
                        colorPickerWidget.dragSliderWidget(click, deltaX, deltaY);
                }
            });
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }
}
