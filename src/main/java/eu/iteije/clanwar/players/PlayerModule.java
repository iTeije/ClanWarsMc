package eu.iteije.clanwar.players;

import eu.iteije.clanwar.ClanWar;
import eu.iteije.clanwar.games.GameModule;
import eu.iteije.clanwar.players.listeners.PlayerJoinListener;
import org.bukkit.plugin.PluginManager;

public class PlayerModule {

    public PlayerModule(ClanWar instance, GameModule gameModule) {
        PluginManager manager = instance.getServer().getPluginManager();
        manager.registerEvents(new PlayerJoinListener(gameModule), instance);

    }
}
