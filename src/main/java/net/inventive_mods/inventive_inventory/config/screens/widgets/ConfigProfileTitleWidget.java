package net.inventive_mods.inventive_inventory.config.screens.widgets;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.util.widgets.CenteredTextWidget;
import net.inventive_mods.inventive_inventory.util.widgets.CustomClickableWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.text.Text;

public class ConfigProfileTitleWidget extends CustomClickableWidget {
    private final DirectionalLayoutWidget horizontal = DirectionalLayoutWidget.horizontal().spacing(10);

    public ConfigProfileTitleWidget(int width, int height) {
        super(width, height);
        MinecraftClient client = InventiveInventory.getClient();
        String translationKey = "config.profiles.label.inventive_inventory.";
        this.horizontal.add(EmptyWidget.ofWidth(client.textRenderer.getWidth("1.")));
        this.horizontal.add(new CenteredTextWidget(80, height, Text.translatable(translationKey + "name"), client.textRenderer));
        this.horizontal.add(new CenteredTextWidget(60, height, Text.translatable(translationKey + "key"), client.textRenderer));
        this.horizontal.add(new CenteredTextWidget(205, height, Text.translatable(translationKey + "preview"), client.textRenderer));
        this.horizontal.refreshPositions();
        this.width = this.horizontal.getWidth();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        this.horizontal.setPosition(this.getX(), this.getY());
        this.horizontal.forEachChild(clickableWidget -> clickableWidget.render(context, mouseX, mouseY, delta));
    }
}
