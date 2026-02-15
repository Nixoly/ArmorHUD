package dev.nixoly.armorhud;

import dev.nixoly.armorhud.config.ArmorHudConfig;
import dev.nixoly.armorhud.render.ArmorHudRenderer;
import dev.nixoly.armorhud.screen.ArmorHudOptionsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ArmorHudClient implements ClientModInitializer {

    private static KeyBinding configKeyBinding;

    @Override
    public void onInitializeClient() {
        ArmorHudConfig.load();

        configKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.armorhud.config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "category.armorhud"
        ));

        HudRenderCallback.EVENT.register(ArmorHudRenderer::render);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKeyBinding.wasPressed()) {
                client.setScreen(new ArmorHudOptionsScreen(null));
            }
        });
    }
}
