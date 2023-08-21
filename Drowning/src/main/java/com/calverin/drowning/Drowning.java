package com.calverin.drowning;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Drowning extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("Using Drowning by Calverin!");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("No longer using Drowning by Calverin!");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
		if(event.isCancelled()) return;
		Player p = event.getPlayer();
		if (p.getRemainingAir() <= 0 && p.getLocation().getBlock().getType() == Material.WATER) {
            Location to = event.getTo().clone();
            to.setY(Math.min(event.getTo().getY(), event.getFrom().getY()));
            event.setTo(to);
            p.setVelocity(new Vector(p.getVelocity().getX(), p.getVelocity().getY() - 0.025, p.getVelocity().getZ()));
        }
	}
    
}
