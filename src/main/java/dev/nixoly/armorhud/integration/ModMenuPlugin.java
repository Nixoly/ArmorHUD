package dev.nixoly.armorhud.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.nixoly.armorhud.screen.ArmorHudOptionsScreen;

public class ModMenuPlugin implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ArmorHudOptionsScreen::new;
    }
}
