package eu.iteije.clanwar.players.listeners;

import eu.iteije.clanwar.utils.menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getClickedInventory().getHolder() != null && event.getClickedInventory().getHolder() instanceof Menu) {
                event.setCancelled(true);

                Menu menu = (Menu) event.getClickedInventory().getHolder();
                menu.onClick(event.getSlot(), (Player) event.getWhoClicked());
            }
        }
    }

}
