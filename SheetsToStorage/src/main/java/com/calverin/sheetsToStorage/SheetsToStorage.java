package com.calverin.sheetsToStorage;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.calverin.sheetsToStorage.Commands.CommandSheetsToStorage;

public class SheetsToStorage extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getLogger().info("Using SheetsToStorage by Calverin!");
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("sheets").setExecutor(new CommandSheetsToStorage());
        //this.getCommand("mimic").setExecutor(new CommandMimic()); Too much mischief
    }

    @Override
    public void onDisable() {
        getLogger().info("No longer using SheetsToStorage by Calverin!");
    }

    //@EventHandler
    //public void onPlayerJoin(PlayerJoinEvent event) {
    //    event.setJoinMessage("Welcome to the server! :D");
    //}
}
