package net.inventive_mods.inventive_inventory.util;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class Notifier {
    public static final Style style = Style.EMPTY.withBold(true);

    public static void error(String message) {
        send(message, ChatFormatting.RED);
    }

    public static void send(String message, ChatFormatting color) {
        Component text = Component.nullToEmpty(message).copy().setStyle(style.withColor(color));
        InventiveInventory.getPlayer().displayClientMessage(text, true);
    }
}
