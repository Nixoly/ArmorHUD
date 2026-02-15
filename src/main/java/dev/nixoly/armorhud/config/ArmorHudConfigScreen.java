package dev.nixoly.armorhud.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ArmorHudConfigScreen {

    public static Screen build(Screen parent) {
        ArmorHudConfig config = ArmorHudConfig.getInstance();
        ArmorHudConfig defaults = new ArmorHudConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("armorhud.config.title"))
                .setSavingRunnable(ArmorHudConfig::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(
                Text.translatable("armorhud.config.category.general"));

        general.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("armorhud.config.enabled"), config.enabled)
                .setDefaultValue(defaults.enabled)
                .setTooltip(Text.translatable("armorhud.config.enabled.tooltip"))
                .setSaveConsumer(val -> config.enabled = val)
                .build());

        general.addEntry(entryBuilder.startEnumSelector(
                        Text.translatable("armorhud.config.orientation"),
                        ArmorHudConfig.Orientation.class, config.orientation)
                .setDefaultValue(defaults.orientation)
                .setTooltip(Text.translatable("armorhud.config.orientation.tooltip"))
                .setEnumNameProvider(val -> Text.translatable("armorhud.enum." + val.name().toLowerCase()))
                .setSaveConsumer(val -> config.orientation = val)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("armorhud.config.show_durability_number"), config.showDurabilityNumber)
                .setDefaultValue(defaults.showDurabilityNumber)
                .setTooltip(Text.translatable("armorhud.config.show_durability_number.tooltip"))
                .setSaveConsumer(val -> config.showDurabilityNumber = val)
                .build());

        general.addEntry(entryBuilder.startEnumSelector(
                        Text.translatable("armorhud.config.durability_position"),
                        ArmorHudConfig.DurabilityPosition.class, config.durabilityPosition)
                .setDefaultValue(defaults.durabilityPosition)
                .setTooltip(Text.translatable("armorhud.config.durability_position.tooltip"))
                .setEnumNameProvider(val -> Text.translatable("armorhud.enum." + val.name().toLowerCase()))
                .setSaveConsumer(val -> config.durabilityPosition = val)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("armorhud.config.show_bar"), config.showDurabilityBar)
                .setDefaultValue(defaults.showDurabilityBar)
                .setTooltip(Text.translatable("armorhud.config.show_bar.tooltip"))
                .setSaveConsumer(val -> config.showDurabilityBar = val)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("armorhud.config.show_glint"), config.showEnchantmentGlint)
                .setDefaultValue(defaults.showEnchantmentGlint)
                .setTooltip(Text.translatable("armorhud.config.show_glint.tooltip"))
                .setSaveConsumer(val -> config.showEnchantmentGlint = val)
                .build());

        ConfigCategory display = builder.getOrCreateCategory(
                Text.translatable("armorhud.config.category.size"));

        display.addEntry(entryBuilder.startIntSlider(
                        Text.translatable("armorhud.config.scale"),
                        (int) (config.scale * 100), 50, 300)
                .setDefaultValue((int) (defaults.scale * 100))
                .setTooltip(Text.translatable("armorhud.config.scale.tooltip"))
                .setSaveConsumer(val -> config.scale = val / 100.0f)
                .setTextGetter(val -> Text.literal(val + "%"))
                .build());

        ConfigCategory colors = builder.getOrCreateCategory(
                Text.translatable("armorhud.config.category.colors"));

        colors.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("armorhud.config.static_color_mode"), config.useStaticColor)
                .setDefaultValue(defaults.useStaticColor)
                .setTooltip(Text.translatable("armorhud.config.static_color_mode.tooltip"))
                .setSaveConsumer(val -> config.useStaticColor = val)
                .build());

        colors.addEntry(entryBuilder.startColorField(
                        Text.translatable("armorhud.config.static_color"), config.staticColor)
                .setDefaultValue(defaults.staticColor)
                .setTooltip(Text.translatable("armorhud.config.static_color.tooltip"))
                .setSaveConsumer(val -> config.staticColor = val)
                .build());

        colors.addEntry(entryBuilder.startColorField(
                        Text.translatable("armorhud.config.color.good"), config.goodColor)
                .setDefaultValue(defaults.goodColor)
                .setTooltip(Text.translatable("armorhud.config.color.good.tooltip"))
                .setSaveConsumer(val -> config.goodColor = val)
                .build());

        colors.addEntry(entryBuilder.startColorField(
                        Text.translatable("armorhud.config.color.warning"), config.warningColor)
                .setDefaultValue(defaults.warningColor)
                .setTooltip(Text.translatable("armorhud.config.color.warning.tooltip"))
                .setSaveConsumer(val -> config.warningColor = val)
                .build());

        colors.addEntry(entryBuilder.startColorField(
                        Text.translatable("armorhud.config.color.danger"), config.dangerColor)
                .setDefaultValue(defaults.dangerColor)
                .setTooltip(Text.translatable("armorhud.config.color.danger.tooltip"))
                .setSaveConsumer(val -> config.dangerColor = val)
                .build());

        colors.addEntry(entryBuilder.startIntSlider(
                        Text.translatable("armorhud.config.warning_percent"),
                        config.warningPercent, 0, 100)
                .setDefaultValue(defaults.warningPercent)
                .setTooltip(Text.translatable("armorhud.config.warning_percent.tooltip"))
                .setSaveConsumer(val -> config.warningPercent = val)
                .setTextGetter(val -> Text.literal(val + "%"))
                .build());

        colors.addEntry(entryBuilder.startIntSlider(
                        Text.translatable("armorhud.config.danger_percent"),
                        config.dangerPercent, 0, 100)
                .setDefaultValue(defaults.dangerPercent)
                .setTooltip(Text.translatable("armorhud.config.danger_percent.tooltip"))
                .setSaveConsumer(val -> config.dangerPercent = val)
                .setTextGetter(val -> Text.literal(val + "%"))
                .build());

        return builder.build();
    }
}
