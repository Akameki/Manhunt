package me.akameki.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrackCommand implements CommandExecutor {
    private static final Map<UUID, UUID> huntersMap = new HashMap<>();

    public static Player getHunted(Player hunter) {
        UUID hunterUUID = hunter.getUniqueId();
        UUID huntedUUID = huntersMap.get(hunterUUID);
        return Bukkit.getServer().getPlayer(huntedUUID);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("track")) {
            if (args.length != 1) {
                return false;
            }

            //initialize player and target
            Player player, target;
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                return true;
            }
            try {
                target = Bukkit.getPlayer(args[0]);
            } catch (Exception e) {
                return false;
            }

            //give player compass if target online
            if (target.isOnline()) {
                if (!player.getInventory().contains(Material.COMPASS)) {
                    player.getInventory().addItem(new ItemStack(Material.COMPASS));
                }
                huntersMap.put(player.getUniqueId(), target.getUniqueId());
                player.sendMessage(ChatColor.GOLD + "Your compass now tracks " + target.getName() + "!");
            } else {
                player.sendMessage(ChatColor.RED + target.getName() + " is not online");
            }
            return true;
        }
        return false;
    }
}