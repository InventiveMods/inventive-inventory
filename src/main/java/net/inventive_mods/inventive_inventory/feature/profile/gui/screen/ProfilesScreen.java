package net.inventive_mods.inventive_inventory.feature.profile.gui.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.inventive_mods.inventive_inventory.feature.profile.Profile;
import net.inventive_mods.inventive_inventory.feature.profile.ProfileHandler;
import net.inventive_mods.inventive_inventory.feature.profile.gui.widget.Section;
import net.inventive_mods.inventive_inventory.key.KeyHandler;
import net.inventive_mods.inventive_inventory.util.Mouse;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ProfilesScreen extends Screen {
    public static final int RADIUS = 60;
    public static final int COLOR = 0x7F000000;
    public static final int HOVER_COLOR = 0x3FFFFFFF;
    public static final int DELETE_COLOR = 0x7FE4080A;
    public static final int OVERWRITE_COLOR = 0x7FFFDE59;
    private static final List<Section> sections = new ArrayList<>();
    public static boolean DELETE_KEY_PRESSED;
    public static boolean OVERWRITE_KEY_PRESSED;
    private int mouseX;
    private int mouseY;

    public ProfilesScreen() {
        super(Component.empty());
        DELETE_KEY_PRESSED = false;
        OVERWRITE_KEY_PRESSED = false;
        sections.clear();
        for (Profile profile : ProfileHandler.getProfiles()) {
            if (sections.size() <= ProfileHandler.MAX_PROFILES) sections.add(new Section(sections.size(), profile));
        }
        if (sections.size() < ProfileHandler.MAX_PROFILES) sections.add(new Section(sections.size(), null));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static List<Section> getSections() {
        return sections;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        for (Section section : sections) {
            section.drawBackground(bufferBuilder, section.isHovered(mouseX, mouseY));
        }

        RenderType.gui().draw(bufferBuilder.buildOrThrow());

        for (Section section : sections) {
            section.drawIcon(guiGraphics);
            section.drawTooltips(guiGraphics, mouseX, mouseY);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (GLFW.GLFW_KEY_LEFT_ALT == keyCode) {
            DELETE_KEY_PRESSED = true;
            return true;
        } else if (GLFW.GLFW_KEY_LEFT_CONTROL == keyCode) {
            OVERWRITE_KEY_PRESSED = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (KeyHandler.openProfilesScreenKey.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode))) {
            return inputAction();
        }
        if (GLFW.GLFW_KEY_LEFT_CONTROL == keyCode) {
            OVERWRITE_KEY_PRESSED = false;
            return true;
        }
        if (GLFW.GLFW_KEY_LEFT_ALT == keyCode) {
            DELETE_KEY_PRESSED = false;
            return true;
        }
        return false;
    }

    private boolean inputAction() {
        int section = Mouse.getHoveredProfileSection(this.mouseX, this.mouseY);
        if (section != -1) {
            Profile profile = sections.get(section).getProfile();
            if (profile == null) {
                if (DELETE_KEY_PRESSED) {
                    if (this.minecraft != null) this.minecraft.schedule(() -> this.minecraft.setScreen(new ProfilesNamingScreen()));
                } else {
                    ProfileHandler.create("", ProfileHandler.getAvailableProfileKey());
                }
            } else if (DELETE_KEY_PRESSED) {
                ProfileHandler.delete(profile);
            } else if (OVERWRITE_KEY_PRESSED) {
                ProfileHandler.overwrite(profile);
            } else {
                ProfileHandler.load(profile);
            }
        }
        this.onClose();
        return true;
    }
}
