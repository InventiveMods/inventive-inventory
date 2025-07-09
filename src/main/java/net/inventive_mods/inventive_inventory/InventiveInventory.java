package net.inventive_mods.inventive_inventory;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod(value = InventiveInventory.MOD_ID, dist = Dist.CLIENT)
public class InventiveInventory {
    public static final String MOD_ID = "inventive_inventory";
    public static final String MOD_NAME = "Inventive Inventory";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public InventiveInventory(ModContainer container) {
        LOGGER.info(MOD_NAME + " initialized successfully!");
    }
}