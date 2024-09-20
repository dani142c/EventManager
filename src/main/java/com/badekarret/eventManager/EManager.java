package com.badekarret.eventManager;

import com.badekarret.eventManager.Managers.ConfigManager;
import com.badekarret.eventManager.Managers.MinigameManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EManager extends JavaPlugin {

    private MinigameManager minigameManager;

    @Override
    public void onEnable() {
        ConfigManager.setupConfig(this);
        MinigameManager minigameManager = new MinigameManager(this);

    }

    public MinigameManager getMinigameManager() { return this.minigameManager; }
}
