package com.calverin.crutils.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandName implements CommandExecutor {
    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length > 0) {
            ItemMeta meta = ((Player)sender).getInventory().getItemInMainHand().getItemMeta();
            String name = "§r";
            for (String arg : args) {
                name += arg + " ";
            }
            meta.setDisplayName(name);
            meta.setLocalizedName(name);
            ((Player)sender).getInventory().getItemInMainHand().setItemMeta(meta);
            sender.sendMessage("§aName changed to " + name + "!");
            return true;
        } else {
            return false;
        }
    }   
}
