package eu.iteije.clanwar.players.listeners;

import eu.iteije.clanwar.games.GameModule;
import eu.iteije.clanwar.games.enums.SpawnPointType;
import eu.iteije.clanwar.players.PlayerModule;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {

    private final GameModule gameModule;
    private final PlayerModule playerModule;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        gameModule.teleport(player, SpawnPointType.LOBBY);

        // Save player
        playerModule.savePlayer(player);
    }
}
