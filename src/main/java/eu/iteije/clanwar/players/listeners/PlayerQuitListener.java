package eu.iteije.clanwar.players.listeners;

import eu.iteije.clanwar.players.PlayerModule;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class PlayerQuitListener implements Listener {

    private final PlayerModule playerModule;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Remove player
        playerModule.removePlayer(player.getUniqueId());
    }

}
