package net.inventive_mods.client.config.screens.widgets;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.options.fields.ColorFieldOption;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.NonNull;

public class ColorFieldWidget extends EditBox {
    private final ColorFieldOption option;

    public ColorFieldWidget(Component text, ColorFieldOption option) {
        super(InventiveInventoryClient.getClient().font, 50, 20, Component.empty());
        this.option = option;
        this.setValue("#" + text.getString());
        this.setMaxLength(7);
    }
    
    @Override
    public void onValueChange(@NonNull String newText) {
        super.onValueChange(newText);
        if (newText.isEmpty() || newText.charAt(0) != '#') {
            this.setValue("#" + newText);
            this.moveCursorTo(1, false);
        } else if (this.getCursorPosition() == 0) this.moveCursorTo(1, false);
        try {
            int parsedColor = Integer.parseInt(newText.substring(1), 16);
            this.option.setValue(ARGB.color(ARGB.alpha(this.option.getValue()), parsedColor));
        } catch (NumberFormatException | IndexOutOfBoundsException ignored) {
            this.option.setValue(ARGB.color(ARGB.alpha(this.option.getValue()), this.option.getDefaultValue()));
        }
    }

    public void reset() {
        this.setValue("#" + Integer.toHexString(this.option.getDefaultValue()).substring(2));
        this.option.reset();
    }
}
