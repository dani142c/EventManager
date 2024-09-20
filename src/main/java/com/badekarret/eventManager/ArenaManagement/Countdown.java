package com.badekarret.eventManager.ArenaManagement;

import com.badekarret.eventManager.Blueprints.IMinigame;
import com.badekarret.eventManager.EManager;
import com.badekarret.eventManager.GameState;
import com.badekarret.eventManager.Minigame;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {

    private EManager plugin;

    private IMinigame minigame;
    private Arena arena;
    private int countdownSeconds;

    public Countdown(EManager plugin, IMinigame minigame, Arena arena) {
        this.minigame = minigame;
        this.arena = arena;
        this.countdownSeconds = arena.getSettings().getCountdownSeconds();
    }

    public void start() {
        arena.setState(GameState.COUNTDOWN);
        this.runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void run() {
        if(this.countdownSeconds == 0) {
            cancel();
            arena.start();
            return;
        }

        if (countdownSeconds <= 10 || countdownSeconds % 15 == 0) {
            arena.sendMessage(ChatColor.GREEN + "Game will start in " + countdownSeconds + " second"
                    + (countdownSeconds == 1 ? "" : "seconds") + "." );
        }

        arena.sendTitle(ChatColor.GREEN + "Game will start in ",  countdownSeconds + " second"
                + (countdownSeconds == 1 ? "" : "s") + ".");

        this.countdownSeconds--;
    }
}
