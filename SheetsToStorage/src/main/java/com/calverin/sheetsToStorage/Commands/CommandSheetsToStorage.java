package com.calverin.sheetsToStorage.Commands;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.net.URI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.scheduler.BukkitTask;

import com.calverin.sheetsToStorage.Storage;

public class CommandSheetsToStorage implements CommandExecutor, TabCompleter {

    private static List<Storage> storages = new ArrayList<>();
    private static BukkitTask scheduledTask;

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args[0]) {
            case "help":
                sender.sendMessage(
                        "/sheets reload - §7Fetches the data from the Google Sheet cells and updates their storages");
                sender.sendMessage(
                        "/sheets add <storage> <url> - §7Starts tracking a new Google Sheet cell and stores it in the specified storage");
                sender.sendMessage(
                        "/sheets remove <storage> - §7Stops tracking the specified storage's Google Sheet cell");
                sender.sendMessage("/sheets schedule <stop|seconds> - §7Sets an update interval for the storages");
                sender.sendMessage("/sheets list - §7Lists all the storages being tracked");
                sender.sendMessage("/sheets help - §7Displays this message");
                return true;
            case "add":
                return addStorage(sender, args);
            case "remove":
                return removeStorage(sender, args);
            case "reload":
                return reload(sender, false);
            case "schedule":
                return schedule(sender, args);
            case "list":
                sender.sendMessage("§aStorages being tracked:");
                for (Storage storage : storages) {
                    sender.sendMessage("§7- " + storage.toString());
                }
                return true;
            default:
                sender.sendMessage("§cUnknown command. Use /sheets help for help");
                return true;
        }
    }

    private boolean addStorage(CommandSender sender, String[] args) {
        URL url;
        int x = 0;
        int y = 0;
        // Invalid/Duplicate input checking
        if (args.length != 3) {
            sender.sendMessage("§cUsage: /sheets add <storage> <url>");
            return true;
        }
        for (Storage storage : storages) {
            if (storage.getStorage().equals(args[1])) {
                sender.sendMessage("§cStorage already has a Google Sheet cell assigned to it");
                return true;
            }
        }
        if (!args[1].matches("^[a-z0-9_]+:[a-z0-9_]+(\\/[a-z0-9_]+)*$")) {
            sender.sendMessage("§cInvalid storage path");
            return true;
        }
        try {
            url = new URI(args[2].replace("edit?", "gviz/tq?tqx=out:csv&")).toURL();
            if (args[2].contains("range=")) {
                // x is the letter of the column converted to a number
                x = args[2].charAt(args[2].indexOf("range=") + 6) - 65;
                // y is the number of the row
                y = Integer.parseInt(args[2].substring(args[2].indexOf("range=") + 7)) - 1;
            }
        } catch (Exception e) {
            sender.sendMessage("§cInvalid URL");
            return true;
        }
        Storage storage = new Storage(url, args[1], x, y);
        storages.add(storage);
        sender.sendMessage("§aAdded storage §6" + args[1] + " §atracking Google Sheet cell §6(" + x + ", " + y + ")");
        storage.setLastValue(reloadStorage(sender, url, args[1], x, y, ""));
        return true;
    }

    private boolean removeStorage(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§cUsage: /sheets remove <storage>");
            return true;
        }
        for (Storage storage : storages) {
            if (storage.getStorage().equals(args[1])) {
                storages.remove(storage);
                sender.sendMessage("§aRemoved storage " + args[1]);
                return true;
            }
        }
        sender.sendMessage("§cStorage not found");
        return true;
    }

    private boolean reload(CommandSender sender, boolean hidden) {
        for (Storage storage : storages) {
            String data = reloadStorage(sender, storage.getUrl(), storage.getStorage(), storage.getX(), storage.getY(), storage.getLastValue());
            storage.setLastValue(data);
        }
        if (!hidden)
            sender.sendMessage("§aReloaded!");
        return true;
    }

    private String reloadStorage(CommandSender sender, URL url, String storage, int x, int y, String lastValue) {
        if (url == null)
            sender.sendMessage("§cURL not set");
        if (storage == null)
            sender.sendMessage("§cStorage not set");
        // Fetch data from Google Sheet cell
        String data = "";
        try {
            data = readData(url, x, y);
        } catch (IOException e) {
            sender.sendMessage("§cFailed to fetch data");
        }

        // Remove extra quotes from the data
        if (data.startsWith("\"") && data.endsWith("\""))
        data = data.substring(1, data.length() - 1);
        data = data.replaceAll("\"\"", "\"");
        // Needed to replaces commas with |s in the sheet so the data wouldn't get split up when the table is parsed
        data = data.replaceAll("\\|", ",");
        
        if (data.equals(lastValue))
            return data;
        
        //System.out.println(url.toString() + " returned: " + data);

        // Validate the data
        if (data.isEmpty())
            sender.sendMessage("§cNo data fetched");
        else if (!data.matches("\\{.+}"))
            sender.sendMessage("§cInvalid data format");

        // Run the data commands
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, "data merge storage " + storage + " " + data);

        // Update storage
        sender.sendMessage("§7Storage " + storage + " set to - " + data.substring(0, Math.min(40, data.length()))
                + (data.length() > 40 ? "...}" : ""));

        return data;
    }

    private String readData(URL url, int x, int y) throws IOException {
        // Fetch data from Google Sheet cell
        String data = "";
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Read the response
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine).append("\n");
            }
            data = content.toString();
        }

        // Parse the data
        String[] rows = data.split("\n");
        int numRows = rows.length;
        int numCols = rows[0].split(",").length;

        String[][] table = new String[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            table[i] = rows[i].split(",");
        }
        x = Math.min(x, numCols - 1);
        y = Math.min(y, numRows - 1);
        return table[y][x]; // Get the data from cell (x, y)
    }

    private boolean schedule(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§cUsage: /sheets schedule <stop|seconds>");
            return true;
        }
        if (scheduledTask != null) {
            Bukkit.getScheduler().cancelTask(scheduledTask.getTaskId());
            if (args[1].equals("stop")) {
                sender.sendMessage("§aStopped reload schedule");
                return true;
            }
        }
        int seconds;
        try {
            seconds = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid number of seconds");
            return true;
        }
        Runnable task = new Runnable() {
            public void run() {
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                reload(console, true);
                // Load the new player data
                Bukkit.dispatchCommand(console, "execute as @a[scores={game_state=1..}] run function cc2:admin/play_last");
            }
        };
        scheduledTask = Bukkit.getScheduler().runTaskTimer(Bukkit.getPluginManager().getPlugin("SheetsToStorage"), task, 0, seconds * 20);
        sender.sendMessage("§aScheduled to reload every " + seconds + " seconds");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<>();
        switch (args.length) {
            case 1:
                list.add("reload");
                list.add("list");
                list.add("add");
                list.add("remove");
                list.add("schedule");
                list.add("help");
                break;
            case 2:
                switch (args[0]) {
                    case "add":
                        list.add("<storage>");
                        break;
                    case "remove":
                        for (Storage storage : storages) {
                            list.add(storage.getStorage());
                        }
                        break;
                    case "schedule":
                        if (scheduledTask != null)
                            list.add("stop");
                        list.add("<seconds>");
                        break;
                }
                break;
            case 3:
                if (args[0].equals("add"))
                    list.add("<url>");
                break;
        }
        return list;
    }
}