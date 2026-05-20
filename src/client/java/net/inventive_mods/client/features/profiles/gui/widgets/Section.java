package net.inventive_mods.client.features.profiles.gui.widgets;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.features.profiles.Profile;
import net.inventive_mods.client.features.profiles.gui.ProfilesScreen;
import net.inventive_mods.client.features.profiles.gui.render_state.ProfileScreenBackgroundGuiElementRenderState;
import net.inventive_mods.client.util.Textures;
import net.inventive_mods.client.util.mouse.MouseLocation;
import net.inventive_mods.client.util.tooltips.TooltipBuilder;
import net.inventive_mods.client.util.tooltips.TooltipType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

import java.util.List;

public class Section {
    private final int ID;
    private final Profile profile;
    private int iconX;
    private int iconY;

    public Section(int ID, Profile profile) {
        this.ID = ID;
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return MouseLocation.getHoveredProfileSection(mouseX, mouseY) == this.ID;
    }

    public void drawBackground(GuiGraphicsExtractor context, boolean hovered) {
        int startAngle = 360 / ProfilesScreen.getSections().size() * this.ID;
        int limit = 360 / ProfilesScreen.getSections().size() + startAngle;
        int centerX = InventiveInventoryClient.getScreen().width / 2;
        int centerY = InventiveInventoryClient.getScreen().height / 2;

        int innerRadius = ProfilesScreen.RADIUS / 2;

        int color = hovered ? ProfilesScreen.HOVER_COLOR : ProfilesScreen.COLOR;
        if (this.profile != null && hovered) {
            if (ProfilesScreen.DELETE_KEY_PRESSED) {
                color = ProfilesScreen.DELETE_COLOR;
            } else if (ProfilesScreen.OVERWRITE_KEY_PRESSED) {
                color = ProfilesScreen.OVERWRITE_COLOR;
            }
        }

        for (; startAngle < limit; startAngle++) {
            double angle = (startAngle * Math.PI) / 180;
            double nextAngle = ((startAngle + 1) * Math.PI) / 180;

            float posX = centerX + (float) Math.sin(angle) * ProfilesScreen.RADIUS;
            float posY = centerY - (float) Math.cos(angle) * ProfilesScreen.RADIUS;

            float posInnerX = centerX + (float) Math.sin(angle) * innerRadius;
            float posInnerY = centerY - (float) Math.cos(angle) * innerRadius;

            float nextPosX = centerX + (float) Math.sin(nextAngle) * ProfilesScreen.RADIUS;
            float nextPosY = centerY - (float) Math.cos(nextAngle) * ProfilesScreen.RADIUS;

            float nextPosInnerX = centerX + (float) Math.sin(nextAngle) * innerRadius;
            float nextPosInnerY = centerY - (float) Math.cos(nextAngle) * innerRadius;

            context.guiRenderState.addGuiElement(new ProfileScreenBackgroundGuiElementRenderState(context, posX, posY, posInnerX, posInnerY, nextPosInnerX, nextPosInnerY, nextPosX, nextPosY, color));
        }
    }

    public void drawIcon(GuiGraphicsExtractor context) {
        int startAngle = 360 / ProfilesScreen.getSections().size() * this.ID;
        int limit = 360 / ProfilesScreen.getSections().size() + startAngle;
        int centerX = InventiveInventoryClient.getScreen().width / 2;
        int centerY = InventiveInventoryClient.getScreen().height / 2;

        int innerRadius = ProfilesScreen.RADIUS / 2;

        int middleAngle = startAngle + (limit - startAngle) / 2;
        double middleRadian = (middleAngle * Math.PI) / 180;

        int middleX = (int) (centerX + Math.sin(middleRadian) * (ProfilesScreen.RADIUS - ((double) (ProfilesScreen.RADIUS - innerRadius) / 2)));
        int middleY = (int) (centerY - Math.cos(middleRadian) * (ProfilesScreen.RADIUS - ((double) (ProfilesScreen.RADIUS - innerRadius) / 2)));

        this.iconX = middleX - 8;
        this.iconY = middleY - 8;

        if (this.profile == null) {
            context.blit(RenderPipelines.GUI_TEXTURED, Textures.PLUS, iconX, iconY, 0, 0, 16, 16, 16, 16);
        } else if (this.profile.getDisplayStack().isEmpty()) {
            context.blit(RenderPipelines.GUI_TEXTURED, Textures.TOOLS, iconX, iconY, 0, 0, 16, 16, 16, 16);
        } else context.item(this.profile.getDisplayStack(), iconX, iconY);
    }

    public void drawTooltips(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        boolean inX = this.iconX < mouseX && mouseX < this.iconX + 16;
        boolean inY = this.iconY < mouseY && mouseY < this.iconY + 16;
        boolean isMouseOverIcon = inX && inY;
        if (isMouseOverIcon) {
            List<Component> textList;
            if (this.profile != null) {
                if (!this.profile.getName().isEmpty()) textList = TooltipBuilder.of(TooltipType.NAME, this.profile);
                else if (!this.profile.getDisplayStack().isEmpty())
                    textList = TooltipBuilder.of(TooltipType.ITEM, this.profile);
                else textList = TooltipBuilder.of(TooltipType.UNKNOWN, this.profile);
            } else textList = TooltipBuilder.of(TooltipType.PLUS, null);
            context.setComponentTooltipForNextFrame(InventiveInventoryClient.getClient().font, textList, mouseX, mouseY);
        }
    }
}
