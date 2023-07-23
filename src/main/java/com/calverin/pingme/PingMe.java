package com.calverin.pingme;

import org.bukkit.plugin.java.JavaPlugin;

public class PingMe extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("onEnable is called!");
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }
    
}
