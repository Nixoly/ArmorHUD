package dev.nixoly.armorhud.render;

import dev.nixoly.armorhud.config.ArmorHudConfig;
import dev.nixoly.armorhud.screen.HudPositionScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class ArmorHudRenderer {

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    public static void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof HudPositionScreen) {
            return;
        }
        renderHud(context);
    }

    public static void renderHud(DrawContext context) {
        ArmorHudConfig config = ArmorHudConfig.getInstance();
        if (!config.enabled) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) {
            return;
        }

        boolean hasArmor = false;
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            if (!client.player.getEquippedStack(slot).isEmpty()) {
                hasArmor = true;
                break;
            }
        }
        if (!hasArmor) {
            return;
        }

        context.getMatrices().push();
        context.getMatrices().translate(config.posX, config.posY, 0);
        context.getMatrices().scale(config.scale, config.scale, 1.0f);

        int horizontalSpacing = config.showDurabilityNumber ? 40 : 20;

        for (int i = 0; i < ARMOR_SLOTS.length; i++) {
            ItemStack stack = client.player.getEquippedStack(ARMOR_SLOTS[i]);
            if (stack.isEmpty()) {
                continue;
            }

            int x = 0;
            int y = 0;

            if (config.orientation == ArmorHudConfig.Orientation.VERTICAL) {
                y = i * 20;
            } else {
                x = i * horizontalSpacing;
            }

            renderArmorPiece(context, client, config, stack, x, y);
        }

        context.getMatrices().pop();
    }

    private static void renderArmorPiece(DrawContext context, MinecraftClient client,
                                          ArmorHudConfig config, ItemStack stack, int x, int y) {
        ItemStack displayStack = buildDisplayStack(stack, config);

        context.drawItem(displayStack, x, y);

        if (config.showDurabilityBar) {
            context.drawItemInSlot(client.textRenderer, stack, x, y);
        }

        if (!stack.isDamageable()) {
            return;
        }

        int color = getDurabilityColor(config, stack);

        if (config.showDurabilityNumber) {
            int currentDurability = stack.getMaxDamage() - stack.getDamage();
            String text = String.valueOf(currentDurability);
            int textWidth = client.textRenderer.getWidth(text);
            int textX;
            int textY = y + 4;

            if (config.durabilityPosition == ArmorHudConfig.DurabilityPosition.RIGHT) {
                textX = x + 18;
            } else {
                textX = x - textWidth - 2;
            }

            context.drawText(client.textRenderer, text, textX, textY, color | 0xFF000000, true);
        }
    }

    private static ItemStack buildDisplayStack(ItemStack stack, ArmorHudConfig config) {
        if (!config.showEnchantmentGlint && stack.hasGlint()) {
            ItemStack copy = stack.copy();
            NbtCompound nbt = copy.getNbt();
            if (nbt != null) {
                nbt.remove("Enchantments");
            }
            return copy;
        }
        return stack;
    }

    private static int getDurabilityColor(ArmorHudConfig config, ItemStack stack) {
        if (config.useStaticColor) {
            return config.staticColor;
        }
        float percent = (float) (stack.getMaxDamage() - stack.getDamage()) / stack.getMaxDamage() * 100f;
        if (percent <= config.dangerPercent) {
            return config.dangerColor;
        }
        if (percent <= config.warningPercent) {
            return config.warningColor;
        }
        return config.goodColor;
    }
}
