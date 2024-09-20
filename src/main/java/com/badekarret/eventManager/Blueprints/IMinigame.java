package com.badekarret.eventManager.Blueprints;

import com.badekarret.eventManager.ArenaManagement.Arena;
import com.badekarret.eventManager.Settings.MinigameSettings;

import java.util.List;

public interface IMinigame {

    void start(Arena arena);
    List<Arena> getArenas();
    MinigameSettings getSettings();
}
