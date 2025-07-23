package net.inventive_mods.inventive_inventory.feature.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.Config;
import net.inventive_mods.inventive_inventory.config.enums.Status;
import net.inventive_mods.inventive_inventory.feature.profile.gui.screen.ProfilesScreen;
import net.inventive_mods.inventive_inventory.key.KeyHandler;
import net.inventive_mods.inventive_inventory.util.ComponentsHelper;
import net.inventive_mods.inventive_inventory.util.FileHandler;
import net.inventive_mods.inventive_inventory.util.InteractionHandler;
import net.inventive_mods.inventive_inventory.util.Notifier;
import net.inventive_mods.inventive_inventory.util.slot.SlotRange;
import net.inventive_mods.inventive_inventory.util.slot.SlotType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EventBusSubscriber(modid = InventiveInventory.MOD_ID, value = Dist.CLIENT)
public class ProfileHandler {
    private final static String NOTIFICATION_TRANSLATION_KEY = "notification.profiles.inventive_inventory.";
    public static final int MAX_PROFILES = 5;
    private static final String PROFILES_FILE = "profiles.json";
    public static final Path PROFILES_PATH = Config.CONFIG_PATH.resolve(PROFILES_FILE);
    private static final List<Profile> profiles = new ArrayList<>();

    @SubscribeEvent
    public static void onTickEvent(ClientTickEvent.Post event) {
        if (InventiveInventory.getPlayer() == null || InventiveInventory.getPlayer().isCreative()) return;
        for (KeyMapping keyMapping : KeyHandler.profileKeys) {
            if (keyMapping.isDown()) {
                boolean validMode = Config.FAST_LOAD.is(true) || (Config.FAST_LOAD.is(false) && KeyHandler.loadProfileKey.isDown());
                if (validMode) {
                    ProfileHandler.getProfiles().forEach(profile -> {
                        if (keyMapping.getName().equals(profile.getKey())) ProfileHandler.load(profile);
                    });
                }
            }
        }
        if (KeyHandler.openProfilesScreenKey.isDown())
            InventiveInventory.getMinecraft().setScreen(new ProfilesScreen());

    }

    public static void create(String name, String key) {
        if (InventiveInventory.getPlayer().isCreative() || Config.PROFILES_STATUS.is(Status.DISABLED)) return;
        Profile profile = new Profile(profiles.size(), name, key, createSavedSlots());
        if (profiles.size() < MAX_PROFILES) {
            profiles.add(profile);
            save();
            Notifier.send(Component.translatable(NOTIFICATION_TRANSLATION_KEY + "created").getString(), ChatFormatting.GREEN);
            return;
        }
        Notifier.error(Component.translatable("error.profiles.inventive_inventory.max_amount").getString());
    }

    public static void load(Profile profile) {
        if (InventiveInventory.getPlayer().isCreative() || Config.PROFILES_STATUS.is(Status.DISABLED)) return;
        SlotRange slotRange = SlotRange.getPlayerSlots(SlotType.INVENTORY, SlotType.HOTBAR, SlotType.OFFHAND);
        slotRange = Config.PROFILES_IGNORE_LOCKED_SLOTS.is(true) ? slotRange.exclude(SlotType.LOCKED_SLOT) : slotRange;
        for (SavedSlot savedSlot : profile.getSavedSlots()) {
            for (int slot : slotRange) {
                ItemStack slotStack = InteractionHandler.getStackFromSlot(slot);
                if (!ItemStack.isSameItem(slotStack, savedSlot.stack())) continue;
                if (!ComponentsHelper.areCustomNamesEqual(slotStack, savedSlot.stack())) continue;
                if (!ComponentsHelper.areEnchantmentsEqual(slotStack, savedSlot.stack())) continue;
                if (!ComponentsHelper.arePotionsEqual(slotStack, savedSlot.stack())) continue;
                InteractionHandler.swapStacks(slot, savedSlot.slot());
                break;
            }
        }
        Notifier.send(Component.translatable(NOTIFICATION_TRANSLATION_KEY + "loaded").getString(), ChatFormatting.BLUE);
    }

    public static void overwrite(Profile profile) {
        if (InventiveInventory.getPlayer().isCreative() || Config.PROFILES_STATUS.is(Status.DISABLED)) return;
        Profile newProfile = new Profile(profile.getId(), profile.getName(), profile.getKey(), createSavedSlots());
        profiles.set(profile.getId(), newProfile);
        save();
        Notifier.send(Component.translatable(NOTIFICATION_TRANSLATION_KEY + "overwritten").getString(), ChatFormatting.GOLD);
    }

    public static void update(Profile profile) {
        if (InventiveInventory.getPlayer().isCreative() || Config.PROFILES_STATUS.is(Status.DISABLED)) return;
        Profile newProfile = new Profile(profile.getId(), profile.getName(), profile.getKey(), profile.getSavedSlots(), profile.getDisplayStack());
        profiles.set(profile.getId(), newProfile);
        save();
        Notifier.send(Component.translatable(NOTIFICATION_TRANSLATION_KEY + "updated").getString(), ChatFormatting.GOLD);
    }

    public static void delete(Profile profile) {
        if (InventiveInventory.getPlayer().isCreative() || Config.PROFILES_STATUS.is(Status.DISABLED)) return;
        profiles.remove(profile.getId());
        for (int i = 0; i < profiles.size(); i++) {
            profiles.get(i).setId(i);
        }
        save();
        Notifier.send(Component.translatable(NOTIFICATION_TRANSLATION_KEY + "deleted").getString(), ChatFormatting.RED);
    }

    public static List<Profile> getProfiles() {
        return profiles;
    }

    public static boolean isNoProfile(String name) {
        for (Profile profile : profiles) {
            if (profile.getName().equals(name)) return false;
        }
        return true;
    }

    public static String getAvailableProfileKey() {
        List<KeyMapping> availableProfileKeys = getAvailableProfileKeys();
        if (availableProfileKeys.isEmpty()) return "";
        else return availableProfileKeys.getFirst().getName();
    }

    public static List<KeyMapping> getAvailableProfileKeys() {
        List<KeyMapping> availableProfileKeys = new ArrayList<>(Arrays.asList(KeyHandler.profileKeys));
        for (Profile profile : profiles) {
            for (KeyMapping profileKey : KeyHandler.profileKeys) {
                if (profileKey.getName().equals(profile.getKey())) availableProfileKeys.remove(profileKey);
            }
        }
        return availableProfileKeys;
    }

    private static void save() {
        JsonObject jsonObject = FileHandler.get(PROFILES_PATH).isJsonObject() ? FileHandler.get(PROFILES_PATH).getAsJsonObject() : new JsonObject();
        jsonObject.remove(InventiveInventory.getWorldName());
        jsonObject.add(InventiveInventory.getWorldName(), profilesToJson());
        FileHandler.write(ProfileHandler.PROFILES_PATH, jsonObject);
    }

    private static List<SavedSlot> createSavedSlots() {
        AbstractContainerMenu screenHandler = InventiveInventory.getMenu();
        List<SavedSlot> savedSlots = new ArrayList<>();
        if (screenHandler == null) return savedSlots;
        for (int slot : SlotRange.getPlayerSlots(SlotType.HOTBAR, SlotType.OFFHAND)) {
            ItemStack stack = screenHandler.getSlot(slot).getItem().copy();
            if (!stack.isEmpty()) savedSlots.add(new SavedSlot(slot, stack));
        }
        return savedSlots;
    }

    @SubscribeEvent
    public static void init(EntityJoinLevelEvent event) {
        if (!event.getEntity().equals(InventiveInventory.getPlayer())) return;
        profiles.clear();
        for (JsonElement jsonElement : getJsonProfiles()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            int id = jsonObject.getAsJsonPrimitive("id").getAsInt();
            String name = jsonObject.getAsJsonPrimitive("name").getAsString();
            String key = jsonObject.getAsJsonPrimitive("key").getAsString();
            JsonObject displayStack = jsonObject.getAsJsonObject("display_stack");
            JsonArray savedSlots = jsonObject.getAsJsonArray("saved_slots");
            profiles.add(new Profile(id, name, key, displayStack, savedSlots));
        }
    }

    private static JsonArray getJsonProfiles() {
        return FileHandler.get(PROFILES_PATH).isJsonObject() && FileHandler.get(PROFILES_PATH).getAsJsonObject().has(InventiveInventory.getWorldName()) ? FileHandler.get(PROFILES_PATH).getAsJsonObject().getAsJsonArray(InventiveInventory.getWorldName()) : new JsonArray();
    }

    private static JsonArray profilesToJson() {
        JsonArray jsonArray = new JsonArray();
        for (Profile profile : profiles) {
            jsonArray.add(profile.getAsJsonObject());
        }
        return jsonArray;
    }
}
