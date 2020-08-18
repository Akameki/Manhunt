package me.akameki.manhunt;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public Main() {
    }

    @Override
    public void onEnable() {
        this.getCommand("tracker").setExecutor(new TrackerCommand());
        this.getServer().getPluginManager().registerEvents(new Events(), this);
    }

    @Override
    public void onDisable() {
    }
}

