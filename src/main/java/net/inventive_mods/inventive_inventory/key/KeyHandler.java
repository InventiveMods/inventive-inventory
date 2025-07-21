package net.inventive_mods.inventive_inventory.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = InventiveInventory.MOD_ID, value = Dist.CLIENT)
public class KeyHandler {
    public static final String INVENTIVE_INVENTORY_CATEGORY = "key.category.inventive_inventory.main";
    public static final String INVENTIVE_INVENTORY_PROFILES_CATEGORY = "key.category.inventive_inventory.profiles";
    private static final String KEY_ADVANCED_OPERATION = "key.inventive_inventory.advanced_operation";
    private static final String KEY_SORT = "key.inventive_inventory.sort";
    private static final String KEY_OPEN_PROFILES_SCREEN = "key.inventive_inventory.open_profiles_screen";
    private static final String KEY_LOAD_PROFILE = "key.inventive_inventory.load_profile";

    public static KeyMapping advancedOperationKey = new KeyMapping(
            KEY_ADVANCED_OPERATION,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            INVENTIVE_INVENTORY_CATEGORY
    );
    public static KeyMapping sortKey = new KeyMapping(
            KEY_SORT,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            INVENTIVE_INVENTORY_CATEGORY
    );
    public static KeyMapping openProfilesScreenKey = new KeyMapping(
            KEY_OPEN_PROFILES_SCREEN,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            INVENTIVE_INVENTORY_PROFILES_CATEGORY
    );
    public static KeyMapping loadProfileKey = new KeyMapping(
            KEY_LOAD_PROFILE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            INVENTIVE_INVENTORY_PROFILES_CATEGORY
    );
    public static KeyMapping[] profileKeys = new KeyMapping[3];


    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(advancedOperationKey);
        event.register(sortKey);
        event.register(openProfilesScreenKey);
        event.register(loadProfileKey);
        for (int i = 0; i < profileKeys.length; i++) {
            profileKeys[i] = new KeyMapping(
                    "key.inventive_inventory.profile_" + i,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_1 + i,
                    INVENTIVE_INVENTORY_PROFILES_CATEGORY
            );
            event.register(profileKeys[i]);
        }
    }

    @Nullable
    public static KeyMapping getByTranslationKey(String translationKey) {
        for (KeyMapping keyMapping : InventiveInventory.getMinecraft().options.keyMappings) {
            if (keyMapping.getName().equals(translationKey)) return keyMapping;
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
