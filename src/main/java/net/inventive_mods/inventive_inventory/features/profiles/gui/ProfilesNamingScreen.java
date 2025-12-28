package net.inventive_mods.inventive_inventory.features.profiles.gui;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.features.profiles.ProfileHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ProfilesNamingScreen extends Screen {
    private TextFieldWidget textFieldWidget;

    public ProfilesNamingScreen() {
        super(Text.of("TextFieldScreen"));
        int width = InventiveInventory.getClient().getWindow().getScaledWidth();
        int height = InventiveInventory.getClient().getWindow().getScaledHeight();
        this.init(width, height);
    }

    @Override
    protected void init() {
        super.init();
        if (this.client == null) return;
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        DirectionalLayoutWidget layout = DirectionalLayoutWidget.vertical();
        TextWidget textWidget = new TextWidget(150, 10, Text.translatable("profiles.screen.naming.text_field.inventive_inventory.placeholder"), this.client.textRenderer);
        this.textFieldWidget = new TextFieldWidget(this.client.textRenderer, 150, 20, Text.empty());
        this.textFieldWidget.setPlaceholder(Text.translatable("profiles.screen.naming.text_field.inventive_inventory.placeholder"));
        ButtonWidget doneButton = ButtonWidget.builder(ScreenTexts.DONE, (button) -> createProfile()).build();

        layout.add(textWidget);
        layout.add(this.textFieldWidget);
        layout.add(doneButton);
        layout.spacing(5);
        layout.refreshPositions();
        layout.setPosition(centerX - layout.getWidth() / 2, centerY - centerY / 2);
        layout.forEachChild(this::addDrawableChild);
    }

    @Override
    public boolean keyPressed(KeyInput keyInput) {
        super.keyPressed(keyInput);
        if (keyInput.isEnter()) createProfile();
        return true;
    }

    private void createProfile() {
        String name = this.textFieldWidget.getText();
        ProfileHandler.create(name, ProfileHandler.getAvailableProfileKey());
        this.close();
    }
}
