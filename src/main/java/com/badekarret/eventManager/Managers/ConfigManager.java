package com.badekarret.eventManager.Managers;

import com.badekarret.eventManager.EManager;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private static FileConfiguration config;

    public static void setupConfig(EManager plugin) {
        ConfigManager.config = plugin.getConfig();
        plugin.saveDefaultConfig();
    }

    public static FileConfiguration getConfig() {
        return config;
    }
}