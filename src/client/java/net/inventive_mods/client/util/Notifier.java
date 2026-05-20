package net.inventive_mods.client.util;

import net.inventive_mods.client.InventiveInventoryClient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class Notifier {
    public static final Style style = Style.EMPTY.withBold(true);

    public static void error(String message) {
        send(message, ChatFormatting.RED);
    }

    public static void send(String message, ChatFormatting color) {
        Component text = Component.literal(message).copy().setStyle(style.withColor(color));
        InventiveInventoryClient.getPlayer().sendOverlayMessage(text);
    }
}
