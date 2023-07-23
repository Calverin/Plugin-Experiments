package com.calverin.crutils.Commands;

import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDND implements CommandExecutor {

    private Set<Player> doNotDisturb;

    public CommandDND(Set<Player> doNotDisturb) {
        this.doNotDisturb = doNotDisturb;
    }

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (!doNotDisturb.contains((Player) sender)) {
                doNotDisturb.add((Player) sender);
                sender.sendMessage("§cDo not disturb mode enabled!");
            } else {
                doNotDisturb.remove(((Player) sender));
                sender.sendMessage("§aDo not disturb mode disabled!");
            }
            return true;
        } else {
            return false;
        }
    }   
}
