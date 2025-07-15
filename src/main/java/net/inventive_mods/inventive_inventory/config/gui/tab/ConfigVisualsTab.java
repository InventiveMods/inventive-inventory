package net.inventive_mods.inventive_inventory.config.gui.tab;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.config.gui.widget.ColorPickerWidget;
import net.inventive_mods.inventive_inventory.config.gui.widget.LockedSlotWidget;
import net.inventive_mods.inventive_inventory.util.Textures;
import net.inventive_mods.inventive_inventory.util.gui.screen.TabbedScreen;
import net.inventive_mods.inventive_inventory.util.gui.widget.ScreenTab;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigVisualsTab extends ScreenTab {
    private final static String TITLE_TRANSLATION_KEY = "config.visuals.title." + InventiveInventory.MOD_ID;

    public ConfigVisualsTab(Minecraft minecraft, int width, TabbedScreen screen) {
        super(minecraft, width, screen);

        this.addTitle(Component.translatable(TITLE_TRANSLATION_KEY + ".locked_slots"));
        this.addConfigOption(Config.SHOW_LOCK);
        this.addConfigOption(Config.LOCKED_SLOT_STYLE);
        this.addConfigOption(Config.LOCKED_SLOTS_COLOR);
        this.addEmptyRow();
        this.addCenteredWidget(new LockedSlotWidget(Textures.SLOT, Config.LOCKED_SLOTS_COLOR, this.getRowWidth(), 20));
        this.addConfigOption(Config.LOCKED_SLOTS_HOTBAR_COLOR);
        this.addEmptyRow();
        this.addCenteredWidget(new LockedSlotWidget(Textures.HOTBAR_SLOT, Config.LOCKED_SLOTS_HOTBAR_COLOR, this.getRowWidth(), 20));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        AtomicBoolean bl = new AtomicBoolean(super.mouseClicked(mouseX, mouseY, button));
        for (Entry entry : this.children()) {
            entry.children().forEach(element -> {
                if (element instanceof ColorPickerWidget colorPickerWidget) {
                    if (colorPickerWidget.overSliderWidget(mouseX, mouseY) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
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
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        for (Entry entry : this.children()) {
            entry.children().forEach(element -> {
                if (element instanceof ColorPickerWidget colorPickerWidget) {
                    if (colorPickerWidget.overSliderWidget(mouseX, mouseY) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT)
                        colorPickerWidget.dragSliderWidget(mouseX, mouseY, dragX, dragY);
                }
            });
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
}
