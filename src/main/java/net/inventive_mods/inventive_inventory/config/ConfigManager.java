package net.inventive_mods.inventive_inventory.config;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.enums.Status;
import net.inventive_mods.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingMode;
import net.inventive_mods.inventive_inventory.config.enums.automatic_refilling.ToolReplacementBehaviour;
import net.inventive_mods.inventive_inventory.config.enums.automatic_refilling.ToolReplacementPriority;
import net.inventive_mods.inventive_inventory.config.enums.item_counter.ItemCounterCountingMode;
import net.inventive_mods.inventive_inventory.config.enums.item_counter.ItemCounterMode;
import net.inventive_mods.inventive_inventory.config.enums.locked_slots.Style;
import net.inventive_mods.inventive_inventory.config.enums.sorting.CursorStackBehaviour;
import net.inventive_mods.inventive_inventory.config.enums.sorting.SortingMode;
import net.inventive_mods.inventive_inventory.config.options.ConfigOption;
import net.inventive_mods.inventive_inventory.config.options.buttons.EnumButtonOption;
import net.inventive_mods.inventive_inventory.config.options.buttons.SimpleButtonOption;
import net.inventive_mods.inventive_inventory.config.options.fields.ColorFieldOption;
import net.inventive_mods.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.inventive_mods.inventive_inventory.features.profiles.ProfileHandler;
import net.inventive_mods.inventive_inventory.util.FileHandler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class ConfigManager {
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(InventiveInventory.MOD_ID);
    public static final ConfigOption<Status> SORTING_STATUS = new EnumButtonOption<>("options", "sorting.status", Status.ENABLED);
    public static final ConfigOption<SortingMode> SORTING_MODE = new EnumButtonOption<>("options", "sorting.mode", SortingMode.NAME);
    public static final ConfigOption<CursorStackBehaviour> CURSOR_STACK_BEHAVIOUR = new EnumButtonOption<>("options", "sorting.cursor_stack_behaviour", CursorStackBehaviour.AOK_DEPENDENT);
    public static final ConfigOption<Status> AUTOMATIC_REFILLING_STATUS = new EnumButtonOption<>("options", "automatic_refilling.status", Status.ENABLED);
    public static final ConfigOption<AutomaticRefillingMode> AUTOMATIC_REFILLING_MODE = new EnumButtonOption<>("options", "automatic_refilling.mode", AutomaticRefillingMode.AUTOMATIC);
    public static final ConfigOption<ToolReplacementBehaviour> TOOL_REPLACEMENT_BEHAVIOUR = new EnumButtonOption<>("options", "automatic_refilling.tool_replacement_behaviour", ToolReplacementBehaviour.BREAK_TOOL);
    public static final ConfigOption<ToolReplacementPriority> TOOL_REPLACEMENT_PRIORITY = new EnumButtonOption<>("options", "automatic_refilling.tool_replacement_priority", ToolReplacementPriority.MATERIAL);
    public static final ConfigOption<Boolean> AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS = new SimpleButtonOption("options", "universal.ignore_locked_slots", true);
    public static final ConfigOption<Boolean> AUTOMATIC_REFILLING_IGNORE_BUCKETS = new SimpleButtonOption("options", "automatic_refilling.ignore_buckets", false);
    public static final ConfigOption<Status> PROFILES_STATUS = new EnumButtonOption<>("options", "profiles.status", Status.ENABLED);
    public static final ConfigOption<Boolean> FAST_LOAD = new SimpleButtonOption("options", "profiles.fast_load", true);
    public static final ConfigOption<Boolean> PICKUP_INTO_LOCKED_SLOTS = new SimpleButtonOption("options", "locked_slots.pickup_into_locked_slots", false);
    public static final ConfigOption<Boolean> QUICK_MOVE_INTO_LOCKED_SLOTS = new SimpleButtonOption("options", "locked_slots.quick_move_into_locked_slots", false);
    public static final ConfigOption<Boolean> PROFILES_IGNORE_LOCKED_SLOTS = new SimpleButtonOption("options", "universal.ignore_locked_slots", true);
    public static final ConfigOption<Boolean> SHOW_LOCK = new SimpleButtonOption("visuals", "locked_slots.show_lock", true);
    public static final ConfigOption<Style> LOCKED_SLOT_STYLE = new EnumButtonOption<>("visuals", "locked_slots.style", Style.FILLED);
    public static final ConfigOption<Integer> LOCKED_SLOTS_COLOR = new ColorFieldOption("visuals", "locked_slots.color", 0xFF4D4D4D);
    public static final ConfigOption<Integer> LOCKED_SLOTS_HOTBAR_COLOR = new ColorFieldOption("visuals", "locked_slots.hotbar_color", 0xAA4D4D4D);
    public static final ConfigOption<Status> ITEM_COUNTER_STATUS = new EnumButtonOption<>("options", "item_counter.status", Status.ENABLED);
    public static final ConfigOption<ItemCounterMode> ITEM_COUNTER_MODE = new EnumButtonOption<>("options", "item_counter.mode", ItemCounterMode.ONLY_SELECTED_SLOT);
    public static final ConfigOption<ItemCounterCountingMode> ITEM_COUNTER_COUNTING_MODE = new EnumButtonOption<>("options", "item_counter.counting_mode", ItemCounterCountingMode.ITEMS);
    public static final ConfigOption<Boolean> ITEM_COUNTER_IGNORE_LOCKED_SLOTS = new SimpleButtonOption("options", "universal.ignore_locked_slots", true);
    public static final ConfigOption<Integer> ITEM_COUNTER_COLOR = new ColorFieldOption("visuals", "item_counter.color", 0xFFFFFFFF);

    public static final String OPTION_TRANSLATION_KEY = "config.options.button.text.inventive_inventory";
    public static final String VISUALS_TRANSLATION_KEY = "config.visuals.button.text.inventive_inventory";

    private static final String CONFIG_FILE = "config.json";
    private static final Path CONFIG_FILE_PATH = CONFIG_PATH.resolve(CONFIG_FILE);


    public static void init() throws IOException {
        deleteOldConfigs();
        Files.createDirectories(CONFIG_PATH);
        FileHandler.createFile(CONFIG_FILE_PATH);
        FileHandler.createFile(LockedSlotsHandler.LOCKED_SLOTS_PATH);
        FileHandler.createFile(ProfileHandler.PROFILES_PATH);
        initConfig();
        save();
    }

    public static void save() {
        try {
            JsonObject config = new JsonObject();
            for (Field field : ConfigManager.class.getDeclaredFields()) {
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

    private static void initConfig() {
        try {
            JsonObject config = FileHandler.get(CONFIG_FILE_PATH);
            for (Field field : ConfigManager.class.getDeclaredFields()) {
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void deleteOldConfigs() throws IOException {
        if (Files.exists(CONFIG_PATH.resolve("settings.properties"))) {
            try (Stream<Path> paths = Files.walk(CONFIG_PATH)) {
                paths.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
    }
}
