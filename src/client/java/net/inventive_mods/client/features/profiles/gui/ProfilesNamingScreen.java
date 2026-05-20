package net.inventive_mods.client.features.profiles.gui;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.features.profiles.ProfileHandler;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class ProfilesNamingScreen extends Screen {
    private EditBox textFieldWidget;

    public ProfilesNamingScreen() {
        super(Component.nullToEmpty("TextFieldScreen"));
        int width = InventiveInventoryClient.getClient().getWindow().getGuiScaledWidth();
        int height = InventiveInventoryClient.getClient().getWindow().getGuiScaledHeight();
        this.init(width, height);
    }

    @Override
    protected void init() {
        super.init();
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        LinearLayout layout = LinearLayout.vertical();
        StringWidget textWidget = new StringWidget(150, 10, Component.translatable("profiles.screen.naming.text_field." + InventiveInventoryClient.MOD_ID + ".placeholder"), this.minecraft.font);
        this.textFieldWidget = new EditBox(this.minecraft.font, 150, 20, Component.empty());
        this.textFieldWidget.setHint(Component.translatable("profiles.screen.naming.text_field." + InventiveInventoryClient.MOD_ID + ".placeholder"));
        Button doneButton = Button.builder(CommonComponents.GUI_DONE, (_) -> createProfile()).build();

        layout.addChild(textWidget);
        layout.addChild(this.textFieldWidget);
        layout.addChild(doneButton);
        layout.spacing(5);
        layout.arrangeElements();
        layout.setPosition(centerX - layout.getWidth() / 2, centerY - centerY / 2);
        layout.visitWidgets(this::addRenderableWidget);
    }

    @Override
    public boolean keyPressed(@NonNull KeyEvent keyInput) {
        super.keyPressed(keyInput);
        if (keyInput.isConfirmation()) createProfile();
        return true;
    }

    private void createProfile() {
        String name = this.textFieldWidget.getValue();
        ProfileHandler.create(name, ProfileHandler.getAvailableProfileKey());
        this.onClose();
    }
}
