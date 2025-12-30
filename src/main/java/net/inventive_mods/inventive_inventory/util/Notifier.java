package net.inventive_mods.inventive_inventory.util;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Notifier {
    public static final Style style = Style.EMPTY.withBold(true);

    public static void error(String message) {
        send(message, Formatting.RED);
    }

    public static void send(String message, Formatting color) {
        Text text = Text.of(message).copy().setStyle(style.withColor(color));
        InventiveInventory.getPlayer().sendMessage(text, true);
    }
}
