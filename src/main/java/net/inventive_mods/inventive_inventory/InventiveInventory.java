package net.inventive_mods.inventive_inventory;

import net.inventive_mods.inventive_inventory.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


@Mod(value = InventiveInventory.MOD_ID, dist = Dist.CLIENT)
public class InventiveInventory {
    public static final String MOD_ID = "inventive_inventory";
    public static final String MOD_NAME = "Inventive Inventory";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public InventiveInventory(ModContainer container) {
        try {
            Config.init(container);
            LOGGER.info(MOD_NAME + " initialized successfully!");
        } catch (IOException e) {
            LOGGER.error("Couldn't create config files", e);
            LOGGER.error(MOD_NAME + " could not be initialized correctly!");
            LOGGER.error("DELETE THE " + InventiveInventory.MOD_ID + " CONFIG DIRECTORY!");
        }
    }

    public static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    public static Font getFont() {
        return getMinecraft().font;
    }

    public static AbstractContainerMenu getMenu() {
        return getMinecraft().player != null ? getMinecraft().player.containerMenu : null;
    }
}