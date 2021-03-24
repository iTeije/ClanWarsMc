package eu.iteije.clanwar.players.listeners;

import eu.iteije.clanwar.games.GameModule;
import eu.iteije.clanwar.games.enums.SpawnPointType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final GameModule gameModule;

    public PlayerJoinListener(GameModule gameModule) {
        this.gameModule = gameModule;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        gameModule.teleport(event.getPlayer(), SpawnPointType.LOBBY);
    }
}
