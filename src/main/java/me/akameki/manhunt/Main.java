package me.akameki.manhunt;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public Main() {
    }

    @Override
    public void onEnable() {
        TrackCommand track = new TrackCommand();
        this.getCommand("tracker").setExecutor(track);
        this.getCommand("tracker").setTabCompleter(track);
        this.getServer().getPluginManager().registerEvents(new Events(), this);
    }

    @Override
    public void onDisable() {
    }
}

