package net.inventive_mods.inventive_inventory.config.screens.tabs;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.config.screens.ConfigScreen;
import net.inventive_mods.inventive_inventory.config.screens.widgets.ColorPickerWidget;
import net.inventive_mods.inventive_inventory.config.screens.widgets.ConfigItemCounterSlotWidget;
import net.inventive_mods.inventive_inventory.config.screens.widgets.ConfigLockedSlotWidget;
import net.inventive_mods.inventive_inventory.util.Textures;
import net.inventive_mods.inventive_inventory.util.widgets.ScreenTab;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigVisualsTab extends ScreenTab {
    private final static String TITLE_TRANSLATION_KEY = "config.visuals.title." + InventiveInventory.MOD_ID;

    public ConfigVisualsTab(MinecraftClient client, int width, ConfigScreen screen) {
        super(client, width, screen);

        this.addTitle(Text.translatable(TITLE_TRANSLATION_KEY + ".locked_slots"));
        this.addWidget(ConfigManager.SHOW_LOCK);
        this.addWidget(ConfigManager.LOCKED_SLOT_STYLE);
        this.addWidget(ConfigManager.LOCKED_SLOTS_COLOR);
        this.addEmptyRow();
        this.addCenteredWidget(new ConfigLockedSlotWidget(Textures.SLOT, ConfigManager.LOCKED_SLOTS_COLOR, this.getRowWidth(), 20));
        this.addWidget(ConfigManager.LOCKED_SLOTS_HOTBAR_COLOR);
        this.addEmptyRow();
        this.addCenteredWidget(new ConfigLockedSlotWidget(Textures.HOTBAR_SLOT, ConfigManager.LOCKED_SLOTS_HOTBAR_COLOR, this.getRowWidth(), 20));
        this.addTitle(Text.translatable(TITLE_TRANSLATION_KEY + ".item_counter"));
        this.addWidget(ConfigManager.ITEM_COUNTER_COLOR);
        this.addEmptyRow();
        this.addCenteredWidget(new ConfigItemCounterSlotWidget(this.getRowWidth(), 20));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        AtomicBoolean bl = new AtomicBoolean(super.mouseClicked(mouseX, mouseY, button));
        for (WidgetEntry entry : this.children()) {
            entry.children().forEach(element -> {
                if (element instanceof ColorPickerWidget colorPickerWidget) {
                    if (colorPickerWidget.overSliderWidget(mouseX, mouseY) && button == 0) {
                        colorPickerWidget.clickSliderWidget(mouseX, mouseY);
                        this.setDragging(true);
                        bl.set(true);
                    }
                }
            });
        }
        return bl.get();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (WidgetEntry entry : this.children()) {
            entry.children().forEach(element -> {
                if (element instanceof ColorPickerWidget colorPickerWidget) {
                    if (colorPickerWidget.overSliderWidget(mouseX, mouseY) && button == 0)
                        colorPickerWidget.dragSliderWidget(mouseX, mouseY, deltaX, deltaY);
                }
            });
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
}
