package net.inventive_mods.inventive_inventory.features.profile.gui.screen;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.features.profile.ProfileHandler;
import net.inventive_mods.inventive_inventory.util.gui.widget.TextWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class ProfilesNamingScreen extends Screen {
    private EditBox editBox;

    public ProfilesNamingScreen() {
        super(Component.nullToEmpty("TextFieldScreen"));
        int width = InventiveInventory.getMinecraft().getWindow().getGuiScaledWidth();
        int height = InventiveInventory.getMinecraft().getWindow().getGuiScaledHeight();
        this.init(InventiveInventory.getMinecraft(), width, height);
    }

    @Override
    protected void init() {
        super.init();
        if (this.minecraft == null) return;
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        LinearLayout layout = LinearLayout.vertical();
        TextWidget textWidget = new TextWidget(150, 10, Component.translatable("profiles.screen.naming.text_field.inventive_inventory.placeholder"), this.minecraft.font);
        this.editBox = new EditBox(this.minecraft.font, 150, 20, Component.empty());
        this.editBox.setHint(Component.translatable("profiles.screen.naming.text_field.inventive_inventory.placeholder"));
        Button doneButton = Button.builder(CommonComponents.GUI_DONE, (button) -> createProfile()).build();

        layout.addChild(textWidget);
        layout.addChild(this.editBox);
        layout.addChild(doneButton);
        layout.spacing(5);
        layout.arrangeElements();
        layout.setPosition(centerX - layout.getWidth() / 2, centerY - centerY / 2);
        layout.visitWidgets(this::addRenderableWidget);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        if (GLFW.GLFW_KEY_ENTER == keyCode) createProfile();
        return true;
    }

    private void createProfile() {
        String name = this.editBox.getValue();
        ProfileHandler.create(name, ProfileHandler.getAvailableProfileKey());
        this.onClose();
    }
}
