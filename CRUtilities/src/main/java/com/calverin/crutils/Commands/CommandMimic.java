package com.calverin.crutils.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMimic implements CommandExecutor {
    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                Player player = (Player)sender;
                player.setPlayerListName(null);
                player.setPlayerListName(player.getPlayerListName().replaceAll(player.getName(),args[0]));
                player.setCustomName(player.getPlayerListName());
                player.setDisplayName(player.getPlayerListName());
                if (args[0].equals(player.getName())) { // Name was manually reset
                    sender.sendMessage("§aName reset!");
                } else {
                    sender.sendMessage("§aName changed to " + args[0] + "!");
                }
                return true;
            } else { // Reset name
                Player player = (Player)sender;
                player.setPlayerListName(null);
                player.setCustomName(null);
                player.setDisplayName(null);
                sender.sendMessage("§aName reset!");
                return true;
            }
        } else {
            return false;
        }
    }  
}
