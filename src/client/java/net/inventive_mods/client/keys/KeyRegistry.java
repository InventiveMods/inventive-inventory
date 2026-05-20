package net.inventive_mods.client.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.inventive_mods.client.InventiveInventoryClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class KeyRegistry {
    public static final KeyMapping.Category INVENTIVE_INVENTORY_CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(InventiveInventoryClient.MOD_ID, "main"));
    public static final KeyMapping.Category INVENTIVE_INVENTORY_PROFILES_CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(InventiveInventoryClient.MOD_ID, "profiles"));
    public static final KeyMapping[] profileKeys = new KeyMapping[3];
    private static final String KEY_SORT = "key." + InventiveInventoryClient.MOD_ID + ".sort";
    private static final String KEY_ADVANCED_OPERATION = "key." + InventiveInventoryClient.MOD_ID + ".advanced_operation";
    private static final String KEY_OPEN_PROFILES_SCREEN = "key." + InventiveInventoryClient.MOD_ID + ".open_profiles_screen";
    private static final String KEY_LOAD_PROFILE = "key." + InventiveInventoryClient.MOD_ID + ".load_profile";
    public static KeyMapping sortKey;
    public static KeyMapping advancedOperationKey;
    public static KeyMapping openProfilesScreenKey;
    public static KeyMapping loadProfileKey;

    public static void register() {
        sortKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                KEY_SORT,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                INVENTIVE_INVENTORY_CATEGORY
        ));
        advancedOperationKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                KEY_ADVANCED_OPERATION,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                INVENTIVE_INVENTORY_CATEGORY
        ));
        openProfilesScreenKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                KEY_OPEN_PROFILES_SCREEN,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                INVENTIVE_INVENTORY_PROFILES_CATEGORY
        ));
        loadProfileKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                KEY_LOAD_PROFILE,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                INVENTIVE_INVENTORY_PROFILES_CATEGORY
        ));
        for (int i = 0; i < profileKeys.length; i++) {
            profileKeys[i] = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                    "key." + InventiveInventoryClient.MOD_ID + ".profile_" + i,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_1 + i,
                    INVENTIVE_INVENTORY_PROFILES_CATEGORY
            ));
        }
    }

    @Nullable
    public static KeyMapping getByTranslationKey(String translationKey) {
        for (KeyMapping keyMapping : InventiveInventoryClient.getClient().options.keyMappings) {
            if (keyMapping.equals(translationKey)) return keyMapping;
        }
        return null;
    }

    @Nullable
    public static KeyMapping getByBoundKey(String boundKey) {
        for (KeyMapping keyBinding : profileKeys) {
            if (keyBinding.getTranslatedKeyMessage().getString().equals(boundKey)) return keyBinding;
        }
        return null;
    }
}
