package net.inventive_mods.client.util.tooltips;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.features.profiles.Profile;
import net.inventive_mods.client.keys.KeyRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.ArrayList;
import java.util.List;

public class TooltipBuilder {
    private final static String TOOLTIP_TRANSLATION_KEY = "profiles.screen.tooltip." + InventiveInventoryClient.MOD_ID + ".";

    public static List<Component> of(TooltipType type, Profile profile) {
        if (type == TooltipType.NAME) return buildName(profile);
        else if (type == TooltipType.ITEM) return buildItem(profile);
        else if (type == TooltipType.UNKNOWN) return buildUnknown(profile);
        else if (type == TooltipType.PLUS) return buildPlus();
        else return new ArrayList<>();
    }

    private static List<Component> buildName(Profile profile) {
        List<Component> textList = new ArrayList<>();
        addTitle(Component.literal(profile.getName()), ChatFormatting.GOLD, textList);
        addKey(profile, textList);
        return textList;
    }

    private static List<Component> buildItem(Profile profile) {
        List<Component> textList = new ArrayList<>();
        addTitle(Component.literal(profile.getDisplayStack().getDisplayName().getString()), ChatFormatting.AQUA, textList);
        if (profile.getDisplayStack().isEnchanted()) {
            for (Holder<Enchantment> entry : profile.getDisplayStack().getEnchantments().keySet()) {
                textList.add(Enchantment.getFullname(entry, EnchantmentHelper.getItemEnchantmentLevel(entry, profile.getDisplayStack())));
            }
            textList.add(Component.empty());
        }
        addKey(profile, textList);
        return textList;
    }

    private static List<Component> buildUnknown(Profile profile) {
        List<Component> textList = new ArrayList<>();
        addTitle(Component.translatable(TOOLTIP_TRANSLATION_KEY + "unnamed"), ChatFormatting.GRAY, textList);
        addKey(profile, textList);
        return textList;
    }

    private static List<Component> buildPlus() {
        List<Component> textList = new ArrayList<>();
        textList.add(Component.translatable(TOOLTIP_TRANSLATION_KEY + "plus.1"));
        textList.add(Component.empty());
        textList.add(Component.translatable(TOOLTIP_TRANSLATION_KEY + "plus.2"));
        return textList;
    }

    private static void addTitle(Component title, ChatFormatting formatting, List<Component> textList) {
        textList.add(title.copy().setStyle(Style.EMPTY.withColor(formatting)));
    }

    private static void addKey(Profile profile, List<Component> textList) {
        if (profile.getKey() != null) {
            KeyMapping keyMapping = KeyRegistry.getByTranslationKey(profile.getKey());
            if (keyMapping != null)
                textList.add(Component.literal(Component.translatable(TOOLTIP_TRANSLATION_KEY + "key").getString() + ": " + keyMapping.getTranslatedKeyMessage().getString()));
        }
    }
}
