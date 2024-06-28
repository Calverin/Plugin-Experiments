package com.calverin.crutils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.calverin.crutils.Commands.CommandDND;
import com.calverin.crutils.Commands.CommandName;
import com.calverin.crutils.Commands.CommandSee;
import com.calverin.crutils.Commands.CommandStacking;
import com.calverin.crutils.Commands.CommandStacking.StackRule;

public class CRUtils extends JavaPlugin implements Listener {

    protected static Set<Player> doNotDisturb = new HashSet<>();
    protected StackRule stacking = StackRule.OFF;

    @Override
    public void onEnable() {
        getLogger().info("Using CRUtils by Calverin!");
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("see").setExecutor(new CommandSee());
        this.getCommand("donotdisturb").setExecutor(new CommandDND(doNotDisturb));
        this.getCommand("dnd").setExecutor(new CommandDND(doNotDisturb));
        this.getCommand("stacking").setExecutor(new CommandStacking());
        this.getCommand("name").setExecutor(new CommandName());
        //this.getCommand("mimic").setExecutor(new CommandMimic()); Too much mischief
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
        // PingMe by Calverin
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

        // Emojis
        msg = msg.replaceAll("::\\)", "§e🙂§r");
        msg = msg.replaceAll("::\\(", "§e😞§r");
        msg = msg.replaceAll("::D", "§e😃§r");
        msg = msg.replaceAll("::P", "§e😝§r");
        msg = msg.replaceAll("::p", "§e😛§r");
        msg = msg.replaceAll("::O", "§e😱§r");
        msg = msg.replaceAll("::o", "§e😮§r");
        msg = msg.replaceAll("::\\|", "§e😐§r");
        msg = msg.replaceAll("::\\/", "§e😕§r");
        msg = msg.replaceAll("::\\*", "§e😘§r");
        msg = msg.replaceAll("::\\$", "§e🤑§r");
        msg = msg.replaceAll("::B\\)", "§e😎§r");
        msg = msg.replaceAll("::eyes", "👀");
        msg = msg.replaceAll("::eye", "👁");
        msg = msg.replaceAll("::lips", "👄");
        msg = msg.replaceAll("::rofl", "🤣");
        msg = msg.replaceAll("::\\<3", "§c❤§r");
        msg = msg.replaceAll("::o\\/", "👋");
        msg = msg.replaceAll("::\\;-\\;", "ಥ_ಥ");
        msg = msg.replaceAll("::o_o", "ಠ_ಠ");
        msg = msg.replaceAll("::flip", "(╯°□°）╯︵ ┻━┻");
        msg = msg.replaceAll("::unflip", "┬─┬ ノ( ゜-゜ノ)");
        msg = msg.replaceAll("::shrug", "¯\\\\_(ツ)_/¯");
        msg = msg.replaceAll("::gib", "༼ つ ◕_◕ ༽つ");

        event.setMessage(msg);
    }

    /* Stacking
    @EventHandler
    public void onPlayerRightClickEntity(PlayerInteractEntityEvent event) {
        // Player/entity stacking
        final Player player = event.getPlayer();
        if (!event.isCancelled() && event.getHand() == EquipmentSlot.HAND && player.getInventory().getItemInMainHand().getType() == Material.AIR && !((stacking == StackRule.PLAYERS) && !(event.getRightClicked() instanceof Player)) && !((stacking == StackRule.MOBS) && (event.getRightClicked() instanceof Player)) && !(stacking == StackRule.OFF)) {
            Entity clicked = event.getRightClicked();
            if (!player.getPassengers().contains(clicked)) { // If not already riding, pickup
                if (player.getPassengers().size() > 0) {
                    clicked.addPassenger(player.getPassengers().get(0));
                }
                player.addPassenger(clicked);
                event.setCancelled(true);
                return;
            } else if (player.isSneaking()) { // If already riding and not sneaking, throw
                // Player/entity throwing
                if (player.getPassengers().size() > 0) {
                    final Entity passenger = player.getPassengers().get(0);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
                        public void run(){
                            passenger.setVelocity(player.getEyeLocation().getDirection().multiply(1.5));
                        }
                    }, 2L);
                    player.removePassenger(passenger);
                    event.setCancelled(true);
                    return;
                }
            }
        }
       
    }
    */

    @EventHandler
    public void onPlayerRightClick(final PlayerInteractEvent event) throws InterruptedException {
        final Player player = event.getPlayer();  
        // Glider
        if (event.getHand() == EquipmentSlot.HAND && event.getMaterial() == Material.PHANTOM_MEMBRANE && player.getGameMode() == GameMode.CREATIVE) {
            // Make them wear an elytra and fly
            ItemStack original = new ItemStack(Material.AIR);
            if (player.getInventory().getChestplate() != null) {
                original = player.getInventory().getChestplate();
            }
            player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
            player.sendEquipmentChange(player, EquipmentSlot.CHEST, original);
            if (!player.isGliding()) {
                player.setVelocity(new Vector(0, 1.0, 0));
            }
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run(){
                    player.setGliding(true);
                    player.setVelocity(player.getEyeLocation().getDirection().multiply(player.isSneaking() ? 7.5 : 3.0).add(player.isGliding() ? new Vector(0, 0, 0) : new Vector(0, 1.5, 0)));
                    player.playSound(player.getLocation(), "entity.ender_dragon.flap", 10, 0);
                }
            }, player.isGliding() ? 1L : 15L );
        }
    }

    public void setStackingRule(StackRule stackRule) {
        stacking = stackRule;
    }

    public StackRule getStackingRule() {
        return stacking;
    }
}
