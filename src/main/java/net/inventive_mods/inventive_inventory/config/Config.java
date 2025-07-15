package net.inventive_mods.inventive_inventory.config;

import com.google.gson.JsonObject;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.enums.locked_slots.SlotStyle;
import net.inventive_mods.inventive_inventory.config.gui.ConfigScreen;
import net.inventive_mods.inventive_inventory.config.option.ConfigOption;
import net.inventive_mods.inventive_inventory.config.option.button.EnumButtonOption;
import net.inventive_mods.inventive_inventory.config.option.button.SimpleButtonOption;
import net.inventive_mods.inventive_inventory.config.option.field.ColorFieldOption;
import net.inventive_mods.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.inventive_mods.inventive_inventory.util.FileHandler;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    public static final String OPTION_TRANSLATION_KEY = "config.options.button.text.inventive_inventory";
    public static final String VISUALS_TRANSLATION_KEY = "config.visuals.button.text.inventive_inventory";
    public static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve(InventiveInventory.MOD_ID);

    private static final String CONFIG_FILE = "config.json";
    private static final Path CONFIG_FILE_PATH = CONFIG_PATH.resolve(CONFIG_FILE);

    public static final ConfigOption<Boolean> PICKUP_INTO_LOCKED_SLOTS = new SimpleButtonOption("options", "locked_slots.pickup_into_locked_slots", false);
    public static final ConfigOption<Boolean> QUICK_MOVE_INTO_LOCKED_SLOTS = new SimpleButtonOption("options", "locked_slots.quick_move_into_locked_slots", false);
    public static final ConfigOption<Boolean> SHOW_LOCK = new SimpleButtonOption("visuals", "locked_slots.show_lock", true);
    public static final ConfigOption<SlotStyle> LOCKED_SLOT_STYLE = new EnumButtonOption<>("visuals", "locked_slots.style", SlotStyle.FILLED);
    public static final ConfigOption<Integer> LOCKED_SLOTS_COLOR = new ColorFieldOption("visuals", "locked_slots.color", 0xFF4D4D4D);
    public static final ConfigOption<Integer> LOCKED_SLOTS_HOTBAR_COLOR = new ColorFieldOption("visuals", "locked_slots.hotbar_color", 0xAA4D4D4D);

    public static void init(ModContainer container) throws IOException {
        Files.createDirectories(CONFIG_PATH);
        FileHandler.createFile(CONFIG_FILE_PATH);
        FileHandler.createFile(LockedSlotsHandler.LOCKED_SLOTS_PATH);
        initConfig();
        save();
        container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, modListScreen) -> new ConfigScreen(modListScreen));
    }

    private static void initConfig() {
        try {
            JsonObject config = FileHandler.get(CONFIG_FILE_PATH);
            for (Field field : Config.class.getDeclaredFields()) {
                String value = null;
                if (ConfigOption.class.isAssignableFrom(field.getType())) {
                    ConfigOption<?> option = (ConfigOption<?>) field.get(null);
                    if (config.has(field.getName().toLowerCase())) {
                        value = config.get(field.getName().toLowerCase()).getAsString();
                    }
                    option.setValue(value);
                }
            }
        } catch (IllegalAccessException ex) {
            for (StackTraceElement traceElement : ex.getStackTrace()) {
                InventiveInventory.LOGGER.error(traceElement.toString());
            }
        }
    }

    public static void save() {
        try {
            JsonObject config = new JsonObject();
            for (Field field : Config.class.getDeclaredFields()) {
                if (ConfigOption.class.isAssignableFrom(field.getType())) {
                    ConfigOption<?> option = (ConfigOption<?>) field.get(null);
                    config.addProperty(field.getName().toLowerCase(), option.getValue().toString().toLowerCase());
                }
            }
            FileHandler.write(CONFIG_FILE_PATH, config);
        } catch (IllegalAccessException ex) {
            for (StackTraceElement traceElement : ex.getStackTrace()) {
                InventiveInventory.LOGGER.error(traceElement.toString());
            }
        }

    }
}
