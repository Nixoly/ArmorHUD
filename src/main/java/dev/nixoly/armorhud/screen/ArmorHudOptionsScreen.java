package dev.nixoly.armorhud.screen;

import dev.nixoly.armorhud.config.ArmorHudConfigScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ArmorHudOptionsScreen extends Screen {

    private final Screen parent;

    public ArmorHudOptionsScreen(Screen parent) {
        super(Text.translatable("armorhud.options.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = width / 2 - 100;
        int centerY = height / 2 - 40;

        boolean clothConfigLoaded = FabricLoader.getInstance().isModLoaded("cloth-config2");

        ButtonWidget settingsButton = ButtonWidget.builder(
                Text.translatable("armorhud.options.settings"),
                button -> openSettings()
        ).dimensions(centerX, centerY, 200, 20).build();
        settingsButton.active = clothConfigLoaded;
        addDrawableChild(settingsButton);

        addDrawableChild(ButtonWidget.builder(
                Text.translatable("armorhud.options.position"),
                button -> client.setScreen(new HudPositionScreen(this))
        ).dimensions(centerX, centerY + 30, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.translatable("armorhud.options.done"),
                button -> close()
        ).dimensions(centerX, centerY + 70, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    private void openSettings() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config2")) {
            client.setScreen(ArmorHudConfigScreen.build(this));
        }
    }
}
