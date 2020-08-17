package me.akameki.manhunt;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

public class CompassEvent implements Listener {
    @EventHandler
    public void onRightClickCompass(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) && event.getMaterial().equals(Material.COMPASS)) {
            Player hunter = event.getPlayer();
            Player hunted = TrackerCommand.getHunted(hunter);
            if (hunted == null) {
                hunter.sendMessage(ChatColor.RED + "Did you use /tracker?");
                return;
            }

            if (!hunted.isOnline()) {
                hunter.sendMessage(ChatColor.YELLOW + hunted.getName() + " is offline");
            } else {
                Location location = hunted.getLocation();
                if (hunter.getWorld().getEnvironment() != hunted.getWorld().getEnvironment()) {
                    return;
                }
                ItemStack compass = new ItemStack(Material.COMPASS);
                if (hunted.getWorld().getEnvironment() == World.Environment.NETHER || hunted.getWorld().getEnvironment() == World.Environment.THE_END) {
                    CompassMeta meta = (CompassMeta) compass.getItemMeta();
                    meta.setLodestone(location);
                    meta.setLodestoneTracked(false);
                    compass.setItemMeta(meta);

                } else { //overworld
                    hunter.setCompassTarget(location);
                }

                //replace compass
                if (hunter.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                    hunter.getInventory().setItemInMainHand(compass);
                } else {
                    hunter.getInventory().setItemInOffHand(compass);
                }
                hunter.sendMessage(ChatColor.GREEN + "Tracking " + hunted.getName());
            }
        }
    }
}
