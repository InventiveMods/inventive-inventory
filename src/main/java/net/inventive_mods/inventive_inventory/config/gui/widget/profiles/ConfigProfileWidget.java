package net.inventive_mods.inventive_inventory.config.gui.widget.profiles;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.gui.tab.ConfigProfilesTab;
import net.inventive_mods.inventive_inventory.feature.profile.Profile;
import net.inventive_mods.inventive_inventory.feature.profile.ProfileHandler;
import net.inventive_mods.inventive_inventory.feature.profile.SavedSlot;
import net.inventive_mods.inventive_inventory.key.KeyHandler;
import net.inventive_mods.inventive_inventory.util.Renderer;
import net.inventive_mods.inventive_inventory.util.gui.widget.ClickableWidget;
import net.inventive_mods.inventive_inventory.util.gui.widget.TextWidget;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.List;

public class ConfigProfileWidget extends ClickableWidget {
    public final LinearLayout layout = LinearLayout.horizontal().spacing(10);
    private final Profile profile;
    private final EditBox name;
    private final Button key;
    private final ConfigProfilesTab parent;

    public ConfigProfileWidget(int width, int height, int index, Profile profile, ConfigProfilesTab parent) {
        super(width, height);
        this.profile = profile;
        this.parent = parent;
        Font font = InventiveInventory.getFont();

        this.name = new EditBox(font, 80, height, Component.empty());
        this.name.setValue(this.profile.getName());
        this.name.setHint(Component.translatable("config.profiles.text_field.inventive_inventory.placeholder"));

        KeyMapping profileKey = KeyHandler.getByTranslationKey(this.profile.getKey());
        Component initially = profileKey != null ? profileKey.getTranslatedKeyMessage() : Component.translatable("config.profiles.button.text.inventive_inventory.not_bound");

        this.key = Button.builder(initially, this.toggle()).build();
        this.key.setWidth(60);

        Hotbar hotbar = new Hotbar(profile.getSavedSlots());

        this.layout.addChild(new TextWidget(font.width(index + "."), this.height, Component.nullToEmpty(index + "."), font));
        this.layout.addChild(this.name);
        this.layout.addChild(this.key);
        this.layout.addChild(hotbar);
        this.layout.arrangeElements();
        this.width = this.layout.getWidth();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.layout.setPosition(this.getX(), this.getY());
        this.layout.visitWidgets(widget -> widget.render(guiGraphics, mouseX, mouseY, partialTick));
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        this.layout.visitWidgets(widget -> {
            if (widget.isMouseOver(mouseX, mouseY)) {
                widget.setFocused(true);
                widget.onClick(mouseX, mouseY, button);
                if (widget instanceof Button buttonWidget)
                    buttonWidget.playDownSound(InventiveInventory.getMinecraft().getSoundManager());
            }
        });
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.layout.visitWidgets(widget -> widget.keyPressed(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        this.layout.visitWidgets(widget -> widget.charTyped(codePoint, modifiers));
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public void playDownSound(SoundManager handler) {
    }

    public void updateProfile() {
        String name = this.name.getValue();
        KeyMapping keyBinding = KeyHandler.getByBoundKey(this.key.getMessage().getString());
        String key = keyBinding != null ? keyBinding.getName() : "";
        if (!name.equals(this.profile.getName()) || !key.equals(this.profile.getKey())) {
            this.profile.setName(name);
            this.profile.setKey(key);
            ProfileHandler.update(this.profile);
        }
    }

    private Button.OnPress toggle() {
        return button -> {
            Component message = button.getMessage();
            Component newMessage = this.parent.availableKeys.getFirst();
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
            KeyMapping keyMapping = KeyHandler.getByBoundKey(button.getMessage().getString());
            if (keyMapping != null) this.profile.setKey(keyMapping.getName());
        };
    }


    private static class Hotbar extends ClickableWidget {

        private final List<SavedSlot> savedSlots;

        public Hotbar(List<SavedSlot> savedSlots) {
            super(205, 20);
            this.savedSlots = savedSlots;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            Renderer.renderProfileHotbar(guiGraphics, this.getX(), this.getY());

            int slotX = this.getX() + 27;
            int slotY = this.getY() + 2;
            for (SavedSlot savedSlot : this.savedSlots) {
                if (savedSlot.slot() == InventoryMenu.SHIELD_SLOT) {
                    guiGraphics.renderItem(savedSlot.stack(), this.getX() + 2, slotY);
                    boolean inX = this.getX() + 2 < mouseX && mouseX < this.getX() + 2 + 16;
                    boolean inY = slotY < mouseY && mouseY < slotY + 16;
                    boolean isMouseOverItem = inX && inY;
                    if (isMouseOverItem) guiGraphics.renderTooltip(InventiveInventory.getFont(), savedSlot.stack(), mouseX, mouseY);
                }
                for (int i = 0; i < 9; i++) {
                    if (savedSlot.slot() - Inventory.INVENTORY_SIZE == i) {
                        guiGraphics.renderItem(savedSlot.stack(), slotX, slotY);
                        boolean inX = slotX < mouseX && mouseX < slotX + 16;
                        boolean inY = slotY < mouseY && mouseY < slotY + 16;
                        boolean isMouseOverItem = inX && inY;
                        if (isMouseOverItem) guiGraphics.renderTooltip(InventiveInventory.getFont(), savedSlot.stack(), mouseX, mouseY);
                        break;
                    }
                }
                slotX += 20;
            }
        }
    }
}
