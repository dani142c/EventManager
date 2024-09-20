package com.badekarret.eventManager.Managers;

import com.badekarret.eventManager.ArenaManagement.Arena;
import com.badekarret.eventManager.Blueprints.IMinigame;
import com.badekarret.eventManager.EManager;
import com.badekarret.eventManager.Minigame;
import com.badekarret.eventManager.Minigames.Spleef;
import com.badekarret.eventManager.Settings.MinigameSettings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinigameManager {
    private EManager plugin;
    private Map<String, MinigameSettings> minigameSettingsMap = new HashMap<>();
    private Map<Minigame, IMinigame> minigamesMap = new HashMap<>();
    private List<Arena> arenas = new ArrayList<>();

    public MinigameManager(EManager plugin) {
        this.plugin = plugin;
        loadMinigamesFromConfig();
    }

    private void loadMinigamesFromConfig() {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection minigamesSection = config.getConfigurationSection("minigames");

        if (minigamesSection != null) {
            for (String minigameKey : minigamesSection.getKeys(false)) {
                ConfigurationSection minigameConfig = minigamesSection.getConfigurationSection(minigameKey);

                int requiredPlayers = minigameConfig.getInt("required-players");
                int maxPlayers = minigameConfig.getInt("max-players");
                int countdownSeconds = minigameConfig.getInt("countdown-seconds");

                // Load lobby spawn
                Location lobbySpawn = new Location(
                        Bukkit.getWorld(minigameConfig.getString("lobby-spawn.world")),
                        minigameConfig.getDouble("lobby-spawn.x"),
                        minigameConfig.getDouble("lobby-spawn.y"),
                        minigameConfig.getDouble("lobby-spawn.z"),
                        (float) minigameConfig.getDouble("lobby-spawn.yaw"),
                        (float) minigameConfig.getDouble("lobby-spawn.pitch")
                );

                // Load arenas
                List<Location> arenaSpawns = new ArrayList<>();
                ConfigurationSection arenasSection = minigameConfig.getConfigurationSection("arenas");
                if (arenasSection != null) {
                    for (String arenaKey : arenasSection.getKeys(false)) {
                        Location arenaSpawn = new Location(
                                Bukkit.getWorld(arenasSection.getString(arenaKey + ".world")),
                                arenasSection.getDouble(arenaKey + ".x"),
                                arenasSection.getDouble(arenaKey + ".y"),
                                arenasSection.getDouble(arenaKey + ".z"),
                                (float) arenasSection.getDouble(arenaKey + ".yaw"),
                                (float) arenasSection.getDouble(arenaKey + ".pitch")
                        );
                        arenaSpawns.add(arenaSpawn);
                    }
                }
                else {
                    plugin.getLogger().warning("No arenas found for minigame " + minigameKey.toLowerCase());
                    continue;
                }

                // Create MinigameSettings and store in the map
                MinigameSettings settings = new MinigameSettings(requiredPlayers, maxPlayers, countdownSeconds, lobbySpawn, arenaSpawns);
                minigameSettingsMap.put(minigameKey.toLowerCase(), settings);

                plugin.getLogger().info("Loaded minigame " + minigameKey.toLowerCase());

                // Instantiate arenas based on these settings
                createMinigame(Minigame.valueOf(minigameKey.toLowerCase()), settings);

                plugin.getLogger().info("Created " + arenas.size() + " arenas for minigame " + minigameKey.toLowerCase());
            }
        }
        else {
            plugin.getLogger().warning("No minigames found in config.yml");
        }
    }

    private IMinigame createMinigameInstance(Minigame minigameType, List<Arena> arenas) {
        switch (minigameType) {
            case TAG:
                return new Tag(arenas);
            case SPLEEF:
                return new Spleef(arenas, minigameSettingsMap.get(minigameType.toString()));
            case SUMO:
                return new Sumo(arenas);
            case TNT_RUN:
                return new TNTRun(arenas);
            case PVP_ARENA:
                return new PVPArena(arenas);
            default:
                throw new IllegalArgumentException("Unknown minigame type: " + minigameType);
        }
    }


    private void createMinigame(Minigame minigameType, MinigameSettings settings) {
        // Null check for settings to avoid further issues if settings are missing
        if (settings == null) {
            plugin.getLogger().warning("Settings for minigame type " + minigameType + " are null. Skipping creation.");
            return;
        }

        List<Arena> arenasForMinigame = new ArrayList<>();

        // Create arenas and add them to the list
        for (int i = 0; i < settings.getArenaSpawns().size(); i++) {
            // Create the arena and pass the minigame instance as null initially
            Arena arena = new Arena(plugin, null, i, settings.getArenaSpawns().get(i), settings);
            arenasForMinigame.add(arena);
        }

        // Use the factory to create the appropriate minigame instance and pass arenas
        IMinigame minigame = createMinigameInstance(minigameType, arenasForMinigame);

        if (minigame != null) {
            // Now update each arena with the correct minigame reference
            for (Arena arena : arenasForMinigame) {
                arena.setMinigame(minigame);  // Assuming you add a setMinigame method in Arena
            }

            minigamesMap.put(minigameType, minigame);  // Store the minigame in the map
            plugin.getLogger().info("Created minigame: " + minigameType + " with " + arenasForMinigame.size() + " arenas.");
        } else {
            plugin.getLogger().warning("Could not create minigame for type: " + minigameType);
        }
    }





    // Method to get a specific minigame settings by name
    public MinigameSettings getMinigameSettings(String minigameName) {
        return minigameSettingsMap.get(minigameName.toLowerCase());
    }

    public List<Arena> getArenas() {
        return arenas;
    }
}