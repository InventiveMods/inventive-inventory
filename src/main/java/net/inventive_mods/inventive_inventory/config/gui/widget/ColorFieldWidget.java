package net.inventive_mods.inventive_inventory.config.gui.widget;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.option.field.ColorFieldOption;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

public class ColorFieldWidget extends EditBox {
    private final ColorFieldOption option;

    public ColorFieldWidget(String text, ColorFieldOption option) {
        super(InventiveInventory.getFont(), 50, 20, Component.empty());
        this.option = option;
        this.setValue("#" + text);
        this.setMaxLength(7);
        this.setResponder(newText -> {
            if (newText.isEmpty() || newText.charAt(0) != '#') {
                this.setValue("#" + newText);
                this.setCursorPosition(1);
            } else if (this.getCursorPosition() == 0) this.setCursorPosition(1);
            try {
                int parsedColor = Integer.parseInt(newText.substring(1), 16);
                this.option.setValue(ARGB.color(ARGB.alpha(this.option.getValue()), parsedColor));
            } catch (NumberFormatException | IndexOutOfBoundsException ignored) {
                this.option.setValue(ARGB.color(ARGB.alpha(this.option.getValue()), this.option.getDefaultValue()));
            }
        });
    }

    public void reset() {
        this.setValue("#" + Integer.toHexString(this.option.getDefaultValue()).substring(2));
        this.option.reset();
    }
}
