package com.calverin.crutils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.calverin.crutils.Commands.CommandDND;
import com.calverin.crutils.Commands.CommandSee;

public class CRUtils extends JavaPlugin implements Listener {

    protected static Set<Player> doNotDisturb = new HashSet<>();

    @Override
    public void onEnable() {
        getLogger().info("Using CRUtils by Calverin!");
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("see").setExecutor(new CommandSee());
        this.getCommand("donotdisturb").setExecutor(new CommandDND(doNotDisturb));
        this.getCommand("dnd").setExecutor(new CommandDND(doNotDisturb));
    }

    @Override
    public void onDisable() {
        getLogger().info("No longer using CRUtils by Calverin!");
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
                msg = msg.replaceAll("@" + player.getName(), "§e@" + player.getName() + "§r");
                msg = msg.replaceAll("@" + player.getName().toLowerCase(), "§e@" + player.getName().toLowerCase() + "§r");
                msg = msg.replaceAll("@" + player.getName().toUpperCase(), "§e@" + player.getName().toUpperCase() + "§r");
                msg = msg.replaceAll(player.getName() + "@", "§e@" + player.getName() + "§r");
                msg = msg.replaceAll(player.getName().toLowerCase() + "@", "§e@" + player.getName().toLowerCase() + "§r");
                msg = msg.replaceAll(player.getName().toUpperCase() + "@", "§e@" + player.getName().toUpperCase() + "§r");
                //player.sendTitle("\u00A7d\u00A7l" + event.getPlayer().getName() + "\u00A7r\u00A7e pinged you!", msg, 2, 35, 3);
                if (!doNotDisturb.contains(player)) {
                    player.playSound(player.getLocation(), "entity.experience_orb.pickup", 100, 0);
                    player.sendTitle("", msg, 2, 35, 3);
                }
            }
        }
        event.setMessage(msg);
    }

    @EventHandler
    public void onPlayerRightClickEntity(PlayerInteractEntityEvent event) {
        // Player stacking
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof Player) {
            Player clicked = (Player) event.getRightClicked();
            player.addPassenger(clicked);
        }
    }

    @EventHandler
    public void onPlayerOffhand(final PlayerSwapHandItemsEvent event) throws InterruptedException {
        // Player throwing
        final Player player = event.getPlayer();
        if (player.getPassengers().size() > 0) {
            final Entity passenger = player.getPassengers().get(0);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
                public void run(){
                    passenger.setVelocity(player.getEyeLocation().getDirection().multiply(2.0));
                }
            }, 2L);
            player.removePassenger(passenger);
        }
    }

    //@EventHandler
    //public void onPlayerDismount(VehicleExitEvent event) {
    //    // Player dismounting
    //    if (event.getExited() instanceof Player) {
    //        Player player = (Player) event.getExited();
    //        if (event.getVehicle() instanceof Player) {
    //            Player vehicle = (Player) event.getVehicle();
    //            player.setVelocity(vehicle.getEyeLocation().getDirection().multiply(10.0));
    //            getServer().broadcastMessage(player.getEyeLocation().getDirection().multiply(10.0).toString());
    //        }
    //    }
    //}
}
