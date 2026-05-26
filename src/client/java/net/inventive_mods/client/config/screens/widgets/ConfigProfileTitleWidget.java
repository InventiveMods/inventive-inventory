package net.inventive_mods.client.config.screens.widgets;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.util.widgets.BaseWidget;
import net.inventive_mods.client.util.widgets.CenteredTextWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class ConfigProfileTitleWidget extends BaseWidget {
    private final LinearLayout horizontal = LinearLayout.horizontal().spacing(10);

    public ConfigProfileTitleWidget(int width, int height) {
        super(width, height);
        Minecraft client = InventiveInventoryClient.getClient();
        String translationKey = "config.profiles.label." + InventiveInventoryClient.MOD_ID + ".";
        this.horizontal.addChild(SpacerElement.width(client.font.width("1.")));
        this.horizontal.addChild(new CenteredTextWidget(80, height, Component.translatable(translationKey + "name"), client.font));
        this.horizontal.addChild(new CenteredTextWidget(60, height, Component.translatable(translationKey + "key"), client.font));
        this.horizontal.addChild(new CenteredTextWidget(205, height, Component.translatable(translationKey + "preview"), client.font));
        this.horizontal.arrangeElements();
        this.width = this.horizontal.getWidth();
    }

    @Override
    protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        this.horizontal.setPosition(this.getX(), this.getY());
        this.horizontal.visitWidgets(widget -> widget.extractRenderState(graphics, mouseX, mouseY, a));
    }
}
