package net.inventive_mods.client.integrations;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
//import net.inventive_mods.inventive_inventory.config.screens.ConfigScreen;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
//        return ConfigScreen::new;
        return null;
    }
}
