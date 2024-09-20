package com.badekarret.eventManager.Settings;

import org.bukkit.Location;

import java.util.List;

public class MinigameSettings {
    private int requiredPlayers;
    private int maxPlayers;
    private int countdownSeconds;
    private Location lobbySpawn;
    private List<Location> arenaSpawns;

    public MinigameSettings(int requiredPlayers, int maxPlayers, int countdownSeconds, Location lobbySpawn, List<Location> arenaSpawns) {
        this.requiredPlayers = requiredPlayers;
        this.maxPlayers = maxPlayers;
        this.countdownSeconds = countdownSeconds;
        this.lobbySpawn = lobbySpawn;
        this.arenaSpawns = arenaSpawns;
    }

    // Getters
    public int getRequiredPlayers() { return requiredPlayers; }
    public int getMaxPlayers() { return maxPlayers; }
    public int getCountdownSeconds() { return countdownSeconds; }
    public Location getLobbySpawn() { return lobbySpawn; }
    public List<Location> getArenaSpawns() { return arenaSpawns; }

}
