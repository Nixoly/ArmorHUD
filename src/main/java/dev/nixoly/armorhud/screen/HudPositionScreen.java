package dev.nixoly.armorhud.screen;

import dev.nixoly.armorhud.config.ArmorHudConfig;
import dev.nixoly.armorhud.render.ArmorHudRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class HudPositionScreen extends Screen {

    private final Screen parent;
    private boolean dragging;
    private int dragOffsetX;
    private int dragOffsetY;

    public HudPositionScreen(Screen parent) {
        super(Text.translatable("armorhud.position.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        addDrawableChild(ButtonWidget.builder(
                Text.translatable("armorhud.position.done"),
                button -> close()
        ).dimensions(width / 2 - 50, height - 30, 100, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.translatable("armorhud.position.reset"),
                button -> {
                    ArmorHudConfig config = ArmorHudConfig.getInstance();
                    config.posX = 5;
                    config.posY = 5;
                    ArmorHudConfig.save();
                }
        ).dimensions(width / 2 - 50, height - 55, 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x80000000);

        ArmorHudConfig config = ArmorHudConfig.getInstance();

        String posText = "X: " + config.posX + "  Y: " + config.posY;
        context.drawCenteredTextWithShadow(textRenderer, posText, width / 2, 10, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer,
                Text.translatable("armorhud.position.hint").getString(),
                width / 2, 25, 0xAAAAAA);

        int previewWidth = getPreviewWidth(config);
        int previewHeight = getPreviewHeight(config);
        int pw = (int) (previewWidth * config.scale) + 4;
        int ph = (int) (previewHeight * config.scale) + 4;
        int px = config.posX - 2;
        int py = config.posY - 2;
        int borderColor = dragging ? 0xFFFFFF00 : 0xFFFFFFFF;
        drawBorder(context, px, py, pw, ph, borderColor);

        ArmorHudRenderer.renderHud(context);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        if (button == 0) {
            ArmorHudConfig config = ArmorHudConfig.getInstance();
            int previewWidth = (int) (getPreviewWidth(config) * config.scale) + 4;
            int previewHeight = (int) (getPreviewHeight(config) * config.scale) + 4;

            if (mouseX >= config.posX - 2 && mouseX <= config.posX + previewWidth
                    && mouseY >= config.posY - 2 && mouseY <= config.posY + previewHeight) {
                dragging = true;
                dragOffsetX = (int) mouseX - config.posX;
                dragOffsetY = (int) mouseY - config.posY;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging && button == 0) {
            ArmorHudConfig config = ArmorHudConfig.getInstance();
            config.posX = Math.max(0, (int) mouseX - dragOffsetX);
            config.posY = Math.max(0, (int) mouseY - dragOffsetY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && dragging) {
            dragging = false;
            ArmorHudConfig.save();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        ArmorHudConfig.save();
        client.setScreen(parent);
    }

    private void drawBorder(DrawContext context, int x, int y, int w, int h, int color) {
        context.fill(x, y, x + w, y + 1, color);
        context.fill(x, y + h - 1, x + w, y + h, color);
        context.fill(x, y + 1, x + 1, y + h - 1, color);
        context.fill(x + w - 1, y + 1, x + w, y + h - 1, color);
    }

    private int getPreviewWidth(ArmorHudConfig config) {
        if (config.orientation == ArmorHudConfig.Orientation.HORIZONTAL) {
            int spacing = config.showDurabilityNumber ? 40 : 20;
            return spacing * 3 + 16;
        }
        int itemWidth = 16;
        if (config.showDurabilityNumber && config.durabilityPosition == ArmorHudConfig.DurabilityPosition.RIGHT) {
            itemWidth += 30;
        }
        return itemWidth;
    }

    private int getPreviewHeight(ArmorHudConfig config) {
        if (config.orientation == ArmorHudConfig.Orientation.VERTICAL) {
            return 3 * 20 + 16;
        }
        return 16;
    }
}
