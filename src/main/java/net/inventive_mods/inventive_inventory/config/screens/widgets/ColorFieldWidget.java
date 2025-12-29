package net.inventive_mods.inventive_inventory.config.screens.widgets;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.options.fields.ColorFieldOption;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

public class ColorFieldWidget extends TextFieldWidget {
    private final ColorFieldOption option;

    public ColorFieldWidget(Text text, ColorFieldOption option) {
        super(InventiveInventory.getClient().textRenderer, 50, 20, Text.empty());
        this.option = option;
        this.setText("#" + text.getString());
        this.setMaxLength(7);
    }

    @Override
    public void onChanged(String newText) {
        super.onChanged(newText);
        if (newText.isEmpty() || newText.charAt(0) != '#') {
            this.setText("#" + newText);
            this.setCursor(1, false);
        } else if (this.getCursor() == 0) this.setCursor(1, false);
        try {
            int parsedColor = Integer.parseInt(newText.substring(1), 16);
            this.option.setValue(ColorHelper.withAlpha(ColorHelper.getAlpha(this.option.getValue()), parsedColor));
        } catch (NumberFormatException | IndexOutOfBoundsException ignored) {
            this.option.setValue(ColorHelper.withAlpha(ColorHelper.getAlpha(this.option.getValue()), this.option.getDefaultValue()));
        }
    }

    public void reset() {
        this.setText("#" + Integer.toHexString(this.option.getDefaultValue()).substring(2));
        this.option.reset();
    }
}
