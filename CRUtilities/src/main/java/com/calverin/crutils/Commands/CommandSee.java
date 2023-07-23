package com.calverin.crutils.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CommandSee implements CommandExecutor {
    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (((Player) sender).hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                ((Player) sender).removePotionEffect(PotionEffectType.NIGHT_VISION);
                sender.sendMessage("§cNight vision disabled!");
                return true;
            }
            ((Player) sender).addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, true, false));
            sender.sendMessage("§aNight vision enabled!");
            return true;
        } else {
            return false;
        }
    }   
}
