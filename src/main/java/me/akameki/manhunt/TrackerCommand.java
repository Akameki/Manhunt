package me.akameki.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TrackerCommand implements CommandExecutor {
    private static final Map<Player, Player> map = new HashMap<>();

    public static Player getHunted(Player hunter) {
        return map.get(hunter);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tracker")) {
            if (args.length != 1) return false;
            Player player, target;

            //initialize player
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                return true;
            }

            //initialize target
            try {
                target = Bukkit.getPlayer(args[0]);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Invalid target");
                return false;
            }

            //give player compass if target online
            if (target.isOnline()) {
                map.put(player, target);
                player.getInventory().addItem(new ItemStack(Material.COMPASS));
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "That player is not online");
                return true;
            }
        }
        return false;
    }
}