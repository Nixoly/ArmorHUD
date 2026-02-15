package dev.nixoly.armorhud.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.nixoly.armorhud.ArmorHud;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ArmorHudConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("armorhudconfig.json");

    private static ArmorHudConfig INSTANCE = new ArmorHudConfig();

    public boolean enabled = true;
    public Orientation orientation = Orientation.VERTICAL;
    public boolean showDurabilityNumber = true;
    public DurabilityPosition durabilityPosition = DurabilityPosition.RIGHT;
    public boolean showDurabilityBar = true;
    public boolean showEnchantmentGlint = true;
    public int posX = 5;
    public int posY = 5;
    public float scale = 1.0f;

    public boolean useStaticColor = false;
    public int staticColor = 0xFFFFFF;
    public int goodColor = 0x55FF55;
    public int warningColor = 0xFFFF55;
    public int dangerColor = 0xFF5555;
    public int warningPercent = 50;
    public int dangerPercent = 25;

    public enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    public enum DurabilityPosition {
        LEFT,
        RIGHT
    }

    public static ArmorHudConfig getInstance() {
        return INSTANCE;
    }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                INSTANCE = GSON.fromJson(json, ArmorHudConfig.class);
                if (INSTANCE == null) {
                    INSTANCE = new ArmorHudConfig();
                }
            } catch (Exception e) {
                ArmorHud.LOGGER.error("Failed to load config", e);
                INSTANCE = new ArmorHudConfig();
            }
        }
        save();
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(INSTANCE));
        } catch (IOException e) {
            ArmorHud.LOGGER.error("Failed to save config", e);
        }
    }
}
