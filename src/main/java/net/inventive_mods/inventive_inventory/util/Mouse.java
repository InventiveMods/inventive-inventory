package net.inventive_mods.inventive_inventory.util;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.feature.profile.gui.screen.ProfilesScreen;
import net.inventive_mods.inventive_inventory.util.slot.SlotRange;
import net.inventive_mods.inventive_inventory.util.slot.SlotType;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class Mouse {
    public static boolean isOverInventory() {
        if (ScreenCheck.isPlayerInventory())
            return true;
        if (InventiveInventory.getScreen() instanceof AbstractContainerScreen<?> containerScreen && containerScreen.hoveredSlot != null)
            return SlotRange.getPlayerSlots().append(SlotType.HOTBAR).contains(containerScreen.hoveredSlot.index);
        return false;
    }

    public static int getHoveredProfileSection(int mouseX, int mouseY) {
        if (ScreenCheck.isProfileScreen()) {
            int sections = ProfilesScreen.getSections().size();

            for (int section = 0; section < sections; section++) {
                int startAngle = section * (360 / sections);
                int limit = 360 / sections + startAngle;
                int x = InventiveInventory.getScreen().width / 2;
                int y = InventiveInventory.getScreen().height / 2;

                int innerRadius = ProfilesScreen.RADIUS / 2;

                for (; startAngle < limit; startAngle++) {
                    double angle = ((startAngle * Math.PI) / 180);
                    double nextAngle = (((startAngle + 1) * Math.PI) / 180);

                    float posX = (float) (x + Math.sin(angle) * ProfilesScreen.RADIUS);
                    float posY = (float) (y - Math.cos(angle) * ProfilesScreen.RADIUS);

                    float posInnerX = (float) (x + Math.sin(angle) * innerRadius);
                    float posInnerY = (float) (y - Math.cos(angle) * innerRadius);

                    float nextPosX = (float) (x + Math.sin(nextAngle) * ProfilesScreen.RADIUS);
                    float nextPosY = (float) (y - Math.cos(nextAngle) * ProfilesScreen.RADIUS);

                    float nextPosInnerX = (float) (x + Math.sin(nextAngle) * innerRadius);
                    float nextPosInnerY = (float) (y - Math.cos(nextAngle) * innerRadius);

                    if (isPointInQuadrilateral(mouseX, mouseY, posX, posY, posInnerX, posInnerY, nextPosInnerX, nextPosInnerY, nextPosX, nextPosY)) {
                        return section;
                    }
                }
            }
        }
        return -1;
    }

    private static boolean isPointInQuadrilateral(int mouseX, int mouseY, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        boolean inFirstTriangle = isPointInTriangle(mouseX, mouseY, x1, y1, x2, y2, x3, y3);
        boolean inSecondTriangle = isPointInTriangle(mouseX, mouseY, x1, y1, x3, y3, x4, y4);
        return inFirstTriangle || inSecondTriangle;
    }

    private static boolean isPointInTriangle(int mouseX, int mouseY, float x1, float y1, float x2, float y2, float x3, float y3) {
        double denominator = ((y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3));
        double a = ((y2 - y3) * (mouseX - x3) + (x3 - x2) * (mouseY - y3)) / denominator;
        double b = ((y3 - y1) * (mouseX - x3) + (x1 - x3) * (mouseY - y3)) / denominator;
        double c = 1 - a - b;

        return 0 <= a && a <= 1 && 0 <= b && b <= 1 && 0 <= c && c <= 1;
    }
}
