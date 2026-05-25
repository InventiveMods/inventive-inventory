package net.inventive_mods.client.config.screens.widgets;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.screens.tabs.ConfigProfilesTab;
import net.inventive_mods.client.features.profiles.Profile;
import net.inventive_mods.client.features.profiles.ProfileHandler;
import net.inventive_mods.client.features.profiles.SavedSlot;
import net.inventive_mods.client.keys.KeyRegistry;
import net.inventive_mods.client.util.Drawer;
import net.inventive_mods.client.util.widgets.BaseWidget;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class ConfigProfileWidget extends BaseWidget {
    public final LinearLayout horizontal = LinearLayout.horizontal().spacing(10);
    private final Profile profile;
    private final EditBox name;
    private final Button key;
    private final ConfigProfilesTab parent;

    public ConfigProfileWidget(int width, int height, int index, Profile profile, ConfigProfilesTab parent) {
        super(width, height);
        this.profile = profile;
        this.parent = parent;
        Minecraft client = InventiveInventoryClient.getClient();

        this.name = new EditBox(client.font, 80, height, Component.empty());
        this.name.setValue(this.profile.getName());
        this.name.setHint(Component.translatable("config.profiles.text_field." + InventiveInventoryClient.MOD_ID + ".placeholder"));

        KeyMapping profileKey = KeyRegistry.getByTranslationKey(this.profile.getKey());
        Component initially = profileKey != null ? profileKey.getTranslatedKeyMessage() : Component.translatable("config.profiles.button.text." + InventiveInventoryClient.MOD_ID + ".not_bound");

        this.key = Button.builder(initially, this.toggle()).build();
        this.key.setWidth(60);

        Hotbar hotbar = new Hotbar(profile.getSavedSlots());

        this.horizontal.addChild(new StringWidget(client.font.width(index + "."), this.height, Component.nullToEmpty(index + "."), client.font));
        this.horizontal.addChild(this.name);
        this.horizontal.addChild(this.key);
        this.horizontal.addChild(hotbar);
        this.horizontal.arrangeElements();
        this.width = this.horizontal.getWidth();
    }

    @Override
    protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        this.horizontal.setPosition(this.getX(), this.getY());
        this.horizontal.visitWidgets(widget -> widget.extractRenderState(graphics, mouseX, mouseY, a));
    }

    public void updateProfile() {
        String name = this.name.getValue();
        KeyMapping keyMapping = KeyRegistry.getByBoundKey(this.key.getMessage().getString());
        String key = keyMapping != null ? keyMapping.getName() : "";
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
            Component notBoundText = Component.translatable("config.profiles.button.text." + InventiveInventoryClient.MOD_ID + ".not_bound");
            if (!message.equals(notBoundText)) {
                this.parent.availableKeys.add(message);
            }
            if (!newMessage.equals(notBoundText)) {
                this.parent.availableKeys.remove(newMessage);
            }
            button.setMessage(newMessage);
        };
    }

    @Override
    public void onClick(@NonNull MouseButtonEvent click, boolean doubled) {
        this.horizontal.visitWidgets(widget -> {
            if (click.x() >= widget.getX() && click.x() <= widget.getRight() && click.y() >= widget.getY() && click.y() <= widget.getBottom()) {
                widget.setFocused(true);
                widget.onClick(click, doubled);
                if (widget instanceof Button button) {
                    button.playDownSound(InventiveInventoryClient.getClient().getSoundManager());
                }
            }
        });
    }

    @Override
    public boolean keyPressed(@NonNull KeyEvent keyInput) {
        this.horizontal.visitWidgets(widget -> widget.keyPressed(keyInput));
        return super.keyPressed(keyInput);
    }

    @Override
    public boolean charTyped(@NonNull CharacterEvent charInput) {
        this.horizontal.visitWidgets(widget -> widget.charTyped(charInput));
        return super.charTyped(charInput);
    }

    private static class Hotbar extends BaseWidget {

        private final List<SavedSlot> savedSlots;

        public Hotbar(List<SavedSlot> savedSlots) {
            super(205, 20);
            this.savedSlots = savedSlots;
        }

        @Override
        protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
            Drawer.drawProfileHotbar(graphics, this.getX(), this.getY());

            int slotY = this.getY() + 2;
            for (SavedSlot savedSlot : this.savedSlots) {
                if (savedSlot.slot() == InventoryMenu.SHIELD_SLOT) {
                    graphics.item(savedSlot.stack(), this.getX() + 2, slotY);
                    boolean inX = this.getX() + 2 < mouseX && mouseX < this.getX() + 2 + 16;
                    boolean inY = slotY < mouseY && mouseY < slotY + 16;
                    boolean isMouseOverItem = inX && inY;
                    if (isMouseOverItem)
                        graphics.setTooltipForNextFrame(InventiveInventoryClient.getClient().font, savedSlot.stack(), mouseX, mouseY);
                }
                for (int i = 0; i < 9; i++) {
                    if (savedSlot.slot() - Inventory.INVENTORY_SIZE == i) {
                        int slotX = this.getX() + 27 + 20 * i;
                        graphics.item(savedSlot.stack(), slotX, slotY);
                        boolean inX = slotX < mouseX && mouseX < slotX + 16;
                        boolean inY = slotY < mouseY && mouseY < slotY + 16;
                        boolean isMouseOverItem = inX && inY;
                        if (isMouseOverItem)
                            graphics.setTooltipForNextFrame(InventiveInventoryClient.getClient().font, savedSlot.stack(), mouseX, mouseY);
                        break;
                    }
                }
            }
        }
    }
}
