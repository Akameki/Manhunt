package me.akameki.manhunt;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.*;

public class Events implements Listener {
    Map<Player, List<ItemStack>> compassMap = new HashMap<>();

    @EventHandler
    public void onRightClickCompass(PlayerInteractEvent event) {
        if (event.getMaterial()==Material.COMPASS && (event.getAction()==Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK)) {
            Player hunter = event.getPlayer();
            Player hunted = TrackCommand.getHunted(hunter);
            if (hunted == null) {
                hunter.sendMessage(ChatColor.RED + "Did you use /track?");
            } else if (!hunted.isOnline()) {
                hunter.sendMessage(ChatColor.RED + hunted.getName() + " is offline");
            } else if (hunter.getWorld().getEnvironment() != hunted.getWorld().getEnvironment()) {
                hunter.sendMessage(ChatColor.GRAY + hunted.getName() + " is in a different dimension!");
            } else {
                Location location = hunted.getLocation();
                hunter.setCompassTarget(location);
                ItemStack compass = new ItemStack(Material.COMPASS);
                CompassMeta meta = (CompassMeta) compass.getItemMeta();
                meta.setDisplayName(hunted.getName()+" Tracker");
                if (hunted.getWorld().getEnvironment() == World.Environment.NETHER || hunted.getWorld().getEnvironment() == World.Environment.THE_END) {
                    meta.setLodestone(location);
                    meta.setLodestoneTracked(false);
                }
                compass.setItemMeta(meta);
                if (hunter.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                    hunter.getInventory().setItemInMainHand(compass);
                } else {
                    hunter.getInventory().setItemInOffHand(compass);
                }
                hunter.sendMessage(ChatColor.GREEN + "Tracking " + hunted.getName());
            }
        }
    }

    @EventHandler
    public void preventDroppingCompass(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Iterator<ItemStack> items = event.getDrops().iterator();
        while (items.hasNext()) {
            ItemStack itemStack = items.next();
            if (itemStack.getType()==Material.COMPASS) {
                compassMap.putIfAbsent(player, new ArrayList<>());
                compassMap.get(player).add(itemStack);
                items.remove();
            }
        }
    }
    @EventHandler
    public void compassOnRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (compassMap.containsKey(player) && !compassMap.get(player).isEmpty()) {
            Iterator<ItemStack> compasses = compassMap.get(player).iterator();
            while (compasses.hasNext()) {
                player.getInventory().addItem(compasses.next());
                compasses.remove();
            }
        }
    }
}
