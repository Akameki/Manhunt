package me.akameki.manhunt;

import org.bukkit.plugin.java.JavaPlugin;

public class Driver extends JavaPlugin {
    public Driver() {
    }

    public void onEnable() {
        this.getCommand("tracker").setExecutor(new TrackerCommand());
        this.getServer().getPluginManager().registerEvents(new CompassEvent(), this);
    }

    public void onDisable() {
    }
}

