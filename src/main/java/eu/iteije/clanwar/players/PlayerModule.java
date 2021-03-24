package eu.iteije.clanwar.players;

import eu.iteije.clanwar.ClanWar;
import eu.iteije.clanwar.databases.DatabaseModule;
import eu.iteije.clanwar.games.GameModule;
import eu.iteije.clanwar.players.listeners.PlayerJoinListener;
import eu.iteije.clanwar.players.listeners.PlayerQuitListener;
import eu.iteije.clanwar.players.objects.CWPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerModule {

    private final Map<UUID, CWPlayer> players;
    private final DatabaseModule databaseModule;

    public PlayerModule(ClanWar instance, GameModule gameModule, DatabaseModule databaseModule) {
        PluginManager manager = instance.getServer().getPluginManager();
        manager.registerEvents(new PlayerJoinListener(gameModule, this), instance);
        manager.registerEvents(new PlayerQuitListener(this), instance);

        this.databaseModule = databaseModule;
        this.players = new HashMap<>();
    }

    public void savePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        databaseModule.fetch(rowSet -> {
            try {
                ResultSet set = rowSet.getOriginal();

                int index = 0;
                while (set.next()) {
                    index++;
                    int clanId = set.getInt("clan_id");
                    String username = set.getString("username");
                    if (!username.equals(player.getName())) {
                        databaseModule.execute("UPDATE players SET username=? WHERE uuid=?", player.getName(), uuid.toString());
                    }

                    this.players.put(uuid, new CWPlayer(uuid, clanId));
                }

                if (index == 0) {
                    databaseModule.execute("INSERT INTO players (uuid, username, clan_id) VALUES (?, ?, ?)", uuid.toString(), player.getName(), -1);
                    this.players.put(uuid, new CWPlayer(uuid, -1));
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }, "SELECT * FROM players WHERE uuid=?", uuid.toString());
    }

    public void removePlayer(UUID uuid) {
        this.players.remove(uuid);
    }

    public CWPlayer getPlayer(UUID uuid) {
        return this.players.get(uuid);
    }

    public void setClan(UUID player, int clanId) {
        this.players.get(player).setClanId(clanId);

        databaseModule.execute("UPDATE players SET clan_id=? WHERE uuid=?", clanId, player.toString());
    }

}
