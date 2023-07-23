package com.calverin.crutils.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.calverin.crutils.CRUtils;

public class CommandStacking implements CommandExecutor, TabCompleter {

    public enum StackRule {
        ON,
        OFF,
        MOBS,
        PLAYERS
    }

    // Probably not the best way to do this
    private CRUtils plugin = (CRUtils) Bukkit.getPluginManager().getPlugin("CRUtilities");

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 0) {
            if (plugin.getStackingRule() == StackRule.OFF) {
                plugin.setStackingRule(StackRule.ON);
                sender.sendMessage("§aStacking toggled on!");
                return true;
            } else {
                plugin.setStackingRule(StackRule.OFF);
                sender.sendMessage("§cStacking toggled off!");
                return true;
            }
        }
        switch (args[0]) {
            case "on":
                plugin.setStackingRule(StackRule.ON);
                sender.sendMessage("§aStacking enabled!");
                return true;
            case "off":
                plugin.setStackingRule(StackRule.OFF);
                sender.sendMessage("§cStacking disabled!");
                return true;
            case "mobs":
                plugin.setStackingRule(StackRule.MOBS);
                sender.sendMessage("§aStacking only mobs enabled!");
                return true;
            case "players":
                plugin.setStackingRule( StackRule.PLAYERS);
                sender.sendMessage("§aStacking only players enabled!");
                return true;
            default:
                if (plugin.getStackingRule() == StackRule.OFF) {
                    plugin.setStackingRule(StackRule.ON);
                    sender.sendMessage("§aStacking toggled on!");
                    return true;
                } else {
                    plugin.setStackingRule(StackRule.OFF);
                    sender.sendMessage("§cStacking toggled off!");
                }
                return true;
        }
    }  

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("off");
            list.add("on");
            list.add("players");
            list.add("mobs");
        }
        return list;
    }
}
