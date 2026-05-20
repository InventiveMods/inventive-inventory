package net.inventive_mods.client.keys.mixins;

import com.mojang.blaze3d.platform.InputConstants;
import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.ConfigManager;
import net.inventive_mods.client.features.automatic_refilling.AutomaticRefillingHandler;
import net.inventive_mods.client.features.profiles.ProfileHandler;
import net.inventive_mods.client.keys.KeyRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Mixin(value = KeyMapping.class, priority = 10000)
public class MixinKeyBinding {
    @Shadow
    @Final
    private static Map<InputConstants.Key, List<KeyMapping>> MAP;

    @Inject(method = "click", at = @At("HEAD"), cancellable = true)
    private static void checkForProfileKeys(InputConstants.Key key, CallbackInfo ci) {
        List<KeyMapping> profileKeys = new ArrayList<>(Arrays.asList(KeyRegistry.profileKeys));
        profileKeys.removeAll(ProfileHandler.getAvailableProfileKeys());
        List<KeyMapping> list = MAP.get(key);
        if (list != null && !list.isEmpty()) {
            for (KeyMapping keyBinding : list) {
                if (profileKeys.contains(keyBinding)) {
                    if (KeyRegistry.loadProfileKey.isDown() || ConfigManager.FAST_LOAD.is(true)) {
                        ci.cancel();
                        return;
                    }
                }
            }
        }
    }

    @Inject(method = "click", at = @At("HEAD"))
    private static void checkForAutomaticRefillingHandlerKeys(InputConstants.Key key, CallbackInfo ci) {
        Options options = InventiveInventoryClient.getClient().options;

        KeyEvent keyInput = new KeyEvent(key.getValue(), key.getValue(), 0);
        MouseButtonEvent click = new MouseButtonEvent(0, 0, new MouseButtonInfo(key.getValue(), 0));

        if (options.keyAttack.matchesMouse(click) || options.keyAttack.matches(keyInput) ||
                options.keyUse.matchesMouse(click) || options.keyUse.matches(keyInput) ||
                options.keyDrop.matchesMouse(click) || options.keyDrop.matches(keyInput)) {
            AutomaticRefillingHandler.keysPressed = true;
        } else if (!options.keyAttack.isDown() && !options.keyUse.isDown() && !options.keyDrop.isDown()) {
            AutomaticRefillingHandler.keysPressed = false;
        }
    }

    @Inject(method = "release", at = @At("HEAD"))
    private static void onReset(CallbackInfo ci) {
        AutomaticRefillingHandler.keysPressed = false;
    }
}
