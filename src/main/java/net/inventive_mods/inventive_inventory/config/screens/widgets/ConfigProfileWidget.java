package net.inventive_mods.inventive_inventory.config.screens.widgets;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.screens.tabs.ConfigProfilesTab;
import net.inventive_mods.inventive_inventory.features.profiles.Profile;
import net.inventive_mods.inventive_inventory.features.profiles.ProfileHandler;
import net.inventive_mods.inventive_inventory.features.profiles.SavedSlot;
import net.inventive_mods.inventive_inventory.keys.KeyRegistry;
import net.inventive_mods.inventive_inventory.util.Drawer;
import net.inventive_mods.inventive_inventory.util.widgets.CustomClickableWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;

import java.util.List;

public class ConfigProfileWidget extends CustomClickableWidget {
    public final DirectionalLayoutWidget horizontal = DirectionalLayoutWidget.horizontal().spacing(10);
    private final Profile profile;
    private final TextFieldWidget name;
    private final ButtonWidget key;
    private final ConfigProfilesTab parent;

    public ConfigProfileWidget(int width, int height, int index, Profile profile, ConfigProfilesTab parent) {
        super(width, height);
        this.profile = profile;
        this.parent = parent;
        MinecraftClient client = InventiveInventory.getClient();

        this.name = new TextFieldWidget(client.textRenderer, 80, height, Text.empty());
        this.name.setText(this.profile.getName());
        this.name.setPlaceholder(Text.translatable("config.profiles.text_field.inventive_inventory.placeholder"));

        KeyBinding profileKey = KeyRegistry.getByTranslationKey(this.profile.getKey());
        Text initially = profileKey != null ? profileKey.getBoundKeyLocalizedText() : Text.translatable("config.profiles.button.text.inventive_inventory.not_bound");

        this.key = ButtonWidget.builder(initially, this.toggle()).build();
        this.key.setWidth(60);

        Hotbar hotbar = new Hotbar(profile.getSavedSlots());

        this.horizontal.add(new TextWidget(client.textRenderer.getWidth(index + "."), this.height, Text.of(index + "."), client.textRenderer));
        this.horizontal.add(this.name);
        this.horizontal.add(this.key);
        this.horizontal.add(hotbar);
        this.horizontal.refreshPositions();
        this.width = this.horizontal.getWidth();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        this.horizontal.setPosition(this.getX(), this.getY());
        this.horizontal.forEachChild(clickableWidget -> clickableWidget.render(context, mouseX, mouseY, delta));
    }

    public void updateProfile() {
        String name = this.name.getText();
        KeyBinding keyBinding = KeyRegistry.getByBoundKey(this.key.getMessage().getString());
        String key = keyBinding != null ? keyBinding.getTranslationKey() : "";
        if (!name.equals(this.profile.getName()) || !key.equals(this.profile.getKey())) {
            this.profile.setName(name);
            this.profile.setKey(key);
            ProfileHandler.update(this.profile);
        }
    }

    private ButtonWidget.PressAction toggle() {
        return button -> {
            Text message = button.getMessage();
            Text newMessage = this.parent.availableKeys.getFirst();
            if (message.getString().equals(newMessage.getString())) {
                this.parent.availableKeys.remove(newMessage);
                this.parent.availableKeys.add(newMessage);
                newMessage = this.parent.availableKeys.getFirst();
            }
            if (!message.getString().equals("Not Bound")) {
                this.parent.availableKeys.add(message);
            }
            if (!newMessage.getString().equals("Not Bound")) {
                this.parent.availableKeys.remove(newMessage);
            }
            button.setMessage(newMessage);
            KeyBinding keyBinding = KeyRegistry.getByBoundKey(button.getMessage().getString());
            if (keyBinding != null) this.profile.setKey(keyBinding.getTranslationKey());
        };
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.horizontal.forEachChild(widget -> {
            if (mouseX >= widget.getX() && mouseX <= widget.getRight() && mouseY >= widget.getY() && mouseY <= widget.getBottom()) {
                widget.setFocused(true);
                widget.onClick(mouseX, mouseY);
                if (widget instanceof ButtonWidget button) {
                    button.playDownSound(InventiveInventory.getClient().getSoundManager());
                }
            }
        });
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.horizontal.forEachChild(widget -> widget.keyPressed(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        this.horizontal.forEachChild(widget -> widget.charTyped(chr, modifiers));
        return super.charTyped(chr, modifiers);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {}

    private static class Hotbar extends CustomClickableWidget {

        private final List<SavedSlot> savedSlots;

        public Hotbar(List<SavedSlot> savedSlots) {
            super(205, 20);
            this.savedSlots = savedSlots;
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            Drawer.drawProfileHotbar(context, this.getX(), this.getY());

            int slotX = this.getX() + 27;
            int slotY = this.getY() + 2;
            for (SavedSlot savedSlot : this.savedSlots) {
                if (savedSlot.slot() == PlayerScreenHandler.OFFHAND_ID) {
                    context.drawItem(savedSlot.stack(), this.getX() + 2, slotY);
                    boolean inX = this.getX() + 2 < mouseX && mouseX < this.getX() + 2 + 16;
                    boolean inY = slotY < mouseY && mouseY < slotY + 16;
                    boolean isMouseOverItem = inX && inY;
                    if (isMouseOverItem)
                        context.drawItemTooltip(InventiveInventory.getClient().textRenderer, savedSlot.stack(), mouseX, mouseY);
                }
                for (int i = 0; i < 9; i++) {
                    if (savedSlot.slot() - PlayerInventory.MAIN_SIZE == i) {
                        context.drawItem(savedSlot.stack(), slotX, slotY);
                        boolean inX = slotX < mouseX && mouseX < slotX + 16;
                        boolean inY = slotY < mouseY && mouseY < slotY + 16;
                        boolean isMouseOverItem = inX && inY;
                        if (isMouseOverItem)
                            context.drawItemTooltip(InventiveInventory.getClient().textRenderer, savedSlot.stack(), mouseX, mouseY);
                        break;
                    }
                }
                slotX += 20;
            }
        }
    }
}
