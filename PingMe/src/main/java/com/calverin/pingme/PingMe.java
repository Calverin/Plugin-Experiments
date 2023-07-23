package com.calverin.pingme;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

public class PingMe extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("Using PingMe by Calverin!");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("No longer using PingMe by Calverin!");
    }

    //@EventHandler
    //public void onPlayerJoin(PlayerJoinEvent event) {
    //    event.setJoinMessage("Welcome to the server! :D");
    //}

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        List<Player> players = new ArrayList<>(getServer().getOnlinePlayers());
        String msg = event.getMessage();
        for (Player player : players) {
            if (msg.contains("@" + player.getName()) || msg.contains("@" + player.getName().toLowerCase()) || msg.contains("@" + player.getName().toUpperCase()) || msg.contains(player.getName() + "@") || msg.contains(player.getName().toLowerCase() + "@") || msg.contains(player.getName().toUpperCase() + "@")) {
                player.playSound(player.getLocation(), "entity.experience_orb.pickup", 100, 0);
                msg = msg.replaceAll("@" + player.getName(), "§e@" + player.getName() + "§r");
                msg = msg.replaceAll("@" + player.getName().toLowerCase(), "§e@" + player.getName().toLowerCase() + "§r");
                msg = msg.replaceAll("@" + player.getName().toUpperCase(), "§e@" + player.getName().toUpperCase() + "§r");
                msg = msg.replaceAll(player.getName() + "@", "§e@" + player.getName() + "§r");
                msg = msg.replaceAll(player.getName().toLowerCase() + "@", "§e@" + player.getName().toLowerCase() + "§r");
                msg = msg.replaceAll(player.getName().toUpperCase() + "@", "§e@" + player.getName().toUpperCase() + "§r");
                //player.sendTitle("\u00A7d\u00A7l" + event.getPlayer().getName() + "\u00A7r\u00A7e pinged you!", msg, 2, 35, 3);
                player.sendTitle("", msg, 2, 35, 3);
            }
        }
        event.setMessage(msg);
    }
    
}
