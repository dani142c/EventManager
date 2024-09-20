package com.badekarret.eventManager.Minigames;

import com.badekarret.eventManager.ArenaManagement.Arena;
import com.badekarret.eventManager.Blueprints.IMinigame;
import com.badekarret.eventManager.Settings.MinigameSettings;

import java.util.List;

public class Spleef implements IMinigame {

    private List<Arena> arenas;
    private MinigameSettings settings;

    public Spleef(List<Arena> arenas, MinigameSettings settings) {
        this.arenas = arenas;
        this.settings = settings;
    }

    // GETTERS

    @Override
    public List<Arena> getArenas() {
        return this.arenas;
    }

    @Override
    public MinigameSettings getSettings() {
        return this.settings;
    }

    // LOGIC

    @Override
    public void start(Arena arena) {
        int nig = 1;
    }


}
