package net.inventive_mods.client.features.profiles.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.inventive_mods.client.features.profiles.Profile;
import net.inventive_mods.client.features.profiles.ProfileHandler;
import net.inventive_mods.client.features.profiles.gui.widgets.Section;
import net.inventive_mods.client.keys.KeyRegistry;
import net.inventive_mods.client.util.mouse.MouseLocation;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ProfilesScreen extends Screen {
    public static final int RADIUS = 60;
    public static final int COLOR = 0x7F000000;
    public static final int HOVER_COLOR = 0x3FFFFFFF;
    public static final int DELETE_COLOR = 0x7FE4080A;
    public static final int OVERWRITE_COLOR = 0x7FFFDE59;
    private static final List<Section> sections = new ArrayList<>();
    public static boolean DELETE_KEY_PRESSED = false;
    public static boolean OVERWRITE_KEY_PRESSED = false;
    private int mouseX;
    private int mouseY;

    public ProfilesScreen() {
        super(Component.literal("Profile Screen"));
        this.minecraft.setOverlay(null);
        sections.clear();
        for (Profile profile : ProfileHandler.getProfiles()) {
            if (sections.size() <= ProfileHandler.MAX_PROFILES) sections.add(new Section(sections.size(), profile));
        }
        if (sections.size() < ProfileHandler.MAX_PROFILES) sections.add(new Section(sections.size(), null));
    }

    public static List<Section> getSections() {
        return sections;
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        for (Section section : sections) {
            section.drawBackground(context, section.isHovered(mouseX, mouseY));
        }

        for (Section section : sections) {
            section.drawIcon(context);
            section.drawTooltips(context, mouseX, mouseY);
        }
    }

    @Override
    public boolean keyPressed(KeyEvent keyInput) {
        if (InputConstants.KEY_LALT == keyInput.key()) {
            DELETE_KEY_PRESSED = true;
            return true;
        } else if (InputConstants.KEY_LCONTROL == keyInput.key()) {
            OVERWRITE_KEY_PRESSED = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyReleased(@NonNull KeyEvent keyInput) {
        if (KeyRegistry.openProfilesScreenKey.matches(keyInput)) {
            return inputAction();
        }
        if (InputConstants.KEY_LCONTROL == keyInput.key()) {
            OVERWRITE_KEY_PRESSED = false;
            return true;
        }
        if (InputConstants.KEY_LALT == keyInput.key()) {
            DELETE_KEY_PRESSED = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(@NonNull MouseButtonEvent click) {
        if (KeyRegistry.openProfilesScreenKey.matchesMouse(click)) {
            return inputAction();
        }
        return false;
    }

    private boolean inputAction() {
        int section = MouseLocation.getHoveredProfileSection(this.mouseX, this.mouseY);
        if (section != -1) {
            Profile profile = sections.get(section).getProfile();
            if (profile == null) {
                if (DELETE_KEY_PRESSED) {
                    minecraft.schedule(() -> minecraft.setScreen(new ProfilesNamingScreen()));
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
