package net.inventive_mods.inventive_inventory.keys.mixins;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.features.automatic_refilling.AutomaticRefillingHandler;
import net.inventive_mods.inventive_inventory.features.profiles.ProfileHandler;
import net.inventive_mods.inventive_inventory.keys.KeyRegistry;
import net.minecraft.client.gui.Click;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.input.MouseInput;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
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

@Mixin(value = KeyBinding.class, priority = 10000)
public class MixinKeyBinding {
    @Shadow
    @Final
    private static Map<InputUtil.Key, List<KeyBinding>> KEY_TO_BINDINGS;

    @Inject(method = "onKeyPressed", at = @At("HEAD"), cancellable = true)
    private static void checkForProfileKeys(InputUtil.Key key, CallbackInfo ci) {
        List<KeyBinding> profileKeys = new ArrayList<>(Arrays.asList(KeyRegistry.profileKeys));
        profileKeys.removeAll(ProfileHandler.getAvailableProfileKeys());
        List<KeyBinding> list = KEY_TO_BINDINGS.get(key);
        if (list != null && !list.isEmpty()) {
            for (KeyBinding keyBinding : list) {
                if (profileKeys.contains(keyBinding)) {
                    if (KeyRegistry.loadProfileKey.isPressed() || ConfigManager.FAST_LOAD.is(true)) {
                        ci.cancel();
                        return;
                    }
                }
            }
        }
    }

    @Inject(method = "onKeyPressed", at = @At("HEAD"))
    private static void checkForAutomaticRefillingHandlerKeys(InputUtil.Key key, CallbackInfo ci) {
        GameOptions options = InventiveInventory.getClient().options;

        KeyInput keyInput = new KeyInput(key.getCode(), key.getCode(), 0);
        Click click = new Click(0, 0, new MouseInput(key.getCode(), 0));

        if (options.attackKey.matchesMouse(click) || options.attackKey.matchesKey(keyInput) ||
                options.useKey.matchesMouse(click) || options.useKey.matchesKey(keyInput) ||
                options.dropKey.matchesMouse(click) || options.dropKey.matchesKey(keyInput)) {
            AutomaticRefillingHandler.keysPressed = true;
        } else if (!options.attackKey.isPressed() && !options.useKey.isPressed() && !options.dropKey.isPressed()) {
            AutomaticRefillingHandler.keysPressed = false;
        }
    }

    @Inject(method = "reset", at = @At("HEAD"))
    private static void onReset(CallbackInfo ci) {
        AutomaticRefillingHandler.keysPressed = false;
    }
}
