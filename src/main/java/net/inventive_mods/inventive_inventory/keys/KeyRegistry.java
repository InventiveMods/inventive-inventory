package net.inventive_mods.inventive_inventory.keys;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class KeyRegistry {
    public static final KeyBinding.Category INVENTIVE_INVENTORY_CATEGORY = KeyBinding.Category.create(Identifier.of(InventiveInventory.MOD_ID, "main"));
    public static final KeyBinding.Category INVENTIVE_INVENTORY_PROFILES_CATEGORY = KeyBinding.Category.create(Identifier.of(InventiveInventory.MOD_ID, "profiles"));
    public static final KeyBinding[] profileKeys = new KeyBinding[3];
    private static final String KEY_SORT = "key.inventive_inventory.sort";
    private static final String KEY_ADVANCED_OPERATION = "key.inventive_inventory.advanced_operation";
    private static final String KEY_OPEN_PROFILES_SCREEN = "key.inventive_inventory.open_profiles_screen";
    private static final String KEY_LOAD_PROFILE = "key.inventive_inventory.load_profile";
    public static KeyBinding sortKey;
    public static KeyBinding advancedOperationKey;
    public static KeyBinding openProfilesScreenKey;
    public static KeyBinding loadProfileKey;

    public static void register() {
        sortKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SORT,
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_R,
                INVENTIVE_INVENTORY_CATEGORY
        ));
        advancedOperationKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_ADVANCED_OPERATION,
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_LEFT_ALT,
                INVENTIVE_INVENTORY_CATEGORY
        ));
        openProfilesScreenKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_OPEN_PROFILES_SCREEN,
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_V,
                INVENTIVE_INVENTORY_PROFILES_CATEGORY
        ));
        loadProfileKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_LOAD_PROFILE,
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_LEFT_ALT,
                INVENTIVE_INVENTORY_PROFILES_CATEGORY
        ));
        for (int i = 0; i < profileKeys.length; i++) {
            profileKeys[i] = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.inventive_inventory.profile_" + i,
                    InputUtil.Type.KEYSYM,
                    InputUtil.GLFW_KEY_1 + i,
                    INVENTIVE_INVENTORY_PROFILES_CATEGORY
            ));
        }
    }

    @Nullable
    public static KeyBinding getByTranslationKey(String translationKey) {
        for (KeyBinding keyBinding : InventiveInventory.getClient().options.allKeys) {
            if (keyBinding.getId().equals(translationKey)) return keyBinding;
        }
        return null;
    }

    @Nullable
    public static KeyBinding getByBoundKey(String boundKey) {
        for (KeyBinding keyBinding : profileKeys) {
            if (keyBinding.getBoundKeyLocalizedText().getString().equals(boundKey)) return keyBinding;
        }
        return null;
    }
}
