package com.badekarret.eventManager.ArenaManagement;

import com.badekarret.eventManager.Blueprints.IMinigame;
import com.badekarret.eventManager.EManager;
import com.badekarret.eventManager.GameState;
import com.badekarret.eventManager.Minigame;
import com.badekarret.eventManager.Settings.MinigameSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena {

    private EManager plugin;

    private IMinigame minigame;
    private int id;
    private Location spawn;
    private GameState state;
    private MinigameSettings settings;

    private List<UUID> players;
    private Countdown countdown;

    public Arena(EManager plugin, IMinigame minigame, int id, Location spawn, MinigameSettings settings) {
        this.plugin = plugin;

        this.minigame = minigame;
        this.id = id;
        this.spawn = spawn;
        this.state = GameState.RECRUITING;
        this.settings = settings;
        this.countdown = new Countdown(this.plugin,this.minigame, this);

        this.players = new ArrayList<>();
    }

    // GETTERS
    public int getId() { return this.id; }
    public List<UUID> getPlayers() { return this.players; }
    public Location getSpawn() { return this.spawn; }
    public IMinigame getMinigame() { return this.minigame; }
    public GameState getState() { return this.state; }
    public MinigameSettings getSettings() { return this.settings; }
    public static List<Arena> getArenas(EManager plugin, IMinigame minigame) {
        // Retrieve the list of all arenas from the MinigameManager
        List<Arena> allArenas = plugin.getMinigameManager().getArenas();

        // Create a list to store the arenas that belong to the given minigame
        List<Arena> matchingArenas = new ArrayList<>();

        // Loop through all arenas and check if they belong to the given minigame
        for (Arena arena : allArenas) {
            if (arena.getMinigame().equals(minigame)) {
                matchingArenas.add(arena);
            }
        }

        // Return the list of matching arenas
        return matchingArenas;
    }



    // TOOLS

    public void sendMessage(String message){
        for (UUID uuid : this.players) {
            Bukkit.getPlayer(uuid).sendMessage(message);
        }
    }

    public void sendTitle(String title, String subtitle){
        for (UUID uuid : this.players) {
            Bukkit.getPlayer(uuid).sendTitle(title, subtitle);
        }
    }

    public void setState(GameState state) { this.state = state; }
    public void setMinigame(IMinigame minigame) { this.minigame = minigame; }

    // PLAYER MANAGEMENT

    public void addPlayer(Player player){
        this.players.add(player.getUniqueId());
        player.teleport(this.spawn);

        if (state.equals(GameState.RECRUITING) && players.size() >= settings.getRequiredPlayers()) {
            countdown.start();
        }
    }

    public void removePlayer(Player player){
        this.players.remove(player.getUniqueId());
        player.teleport(settings.getLobbySpawn());
        player.sendTitle("", "");

        if (state == GameState.COUNTDOWN && players.size() < settings.getRequiredPlayers()) {
            sendMessage(ChatColor.RED + "Not enough players to start game! Countdown stopped.");
            reset(false);
            return;
        }

        if (state == GameState.LIVE && players.size() < settings.getRequiredPlayers()) {
            sendMessage(ChatColor.RED + "Too many players left! Game stopped.");
            reset(true);
        }
    }

    public void reset(boolean kickPlayers) {
        if (kickPlayers) {
            Location lobbySpawn = settings.getLobbySpawn();
            for (UUID uuid : this.players) {
                Bukkit.getPlayer(uuid).teleport(lobbySpawn);
            }
            players.clear();
        }
        sendTitle("", "");
        state = GameState.RECRUITING;
        countdown.cancel();
        countdown = new Countdown(this.plugin,this.minigame, this);
    }

    // STARTING

    public void start() {this.minigame.start(this);}

}
