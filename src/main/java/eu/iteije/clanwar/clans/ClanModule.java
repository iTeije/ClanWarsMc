package eu.iteije.clanwar.clans;

import eu.iteije.clanwar.clans.objects.Clan;
import eu.iteije.clanwar.clans.objects.ClanInfo;
import eu.iteije.clanwar.clans.responses.TransferResponse;
import eu.iteije.clanwar.databases.DatabaseModule;
import eu.iteije.clanwar.players.PlayerModule;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClanModule {

    private final DatabaseModule databaseModule;
    private final PlayerModule playerModule;

    private final Map<Integer, Clan> clans;
    private final Map<String, Integer> clanIdForName;

    public ClanModule(DatabaseModule databaseModule, PlayerModule playerModule) {
        this.databaseModule = databaseModule;
        this.playerModule = playerModule;

        this.clans = new HashMap<>();
        this.clanIdForName = new HashMap<>();

        this.loadClans(databaseModule);
    }

    private void loadClans(DatabaseModule databaseModule) {
        databaseModule.fetch(rowSet -> {
            try (ResultSet set = rowSet.getOriginal()) {
                while (set.next()) {
                    int clanId = set.getInt("id");
                    String name = set.getString("clan_name");
                    UUID uuid = UUID.fromString(set.getString("owner_uuid"));

                    ClanInfo info = new ClanInfo();
                    databaseModule.fetch(userRowSet -> {
                        try {
                            ResultSet members = userRowSet.getOriginal();

                            while (members.next()) {
                                String memberUniqueId = members.getString("uuid");
                                String memberUsername = members.getString("username");

                                if (uuid.toString().equals(memberUniqueId)) {
                                    info.setOwnerName(memberUsername);
                                } else {
                                    info.addMember(UUID.fromString(memberUniqueId), memberUsername);
                                }
                            }
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                        }
                    }, "SELECT uuid, username FROM players WHERE clan_id=?", clanId);

                    Clan clan = new Clan(name, uuid, clanId, info);

                    this.clans.put(clanId, clan);
                    this.clanIdForName.put(name.toLowerCase(), clanId);
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }, "SELECT * FROM clans");
    }

    public Clan getClan(String name) {
        return this.clans.get(this.clanIdForName.getOrDefault(name.toLowerCase(), -1));
    }

    public Clan getClan(int id) {
        return this.clans.get(id);
    }

    public void createClan(String name, Player player) {
        UUID owner = player.getUniqueId();
        databaseModule.execute(cachedRowSet -> {
            databaseModule.fetch(rowSet -> {
                try (ResultSet set = rowSet.getOriginal()) {
                    while (set.next()) {
                        int id = set.getInt("id");

                        ClanInfo info = new ClanInfo();
                        info.setOwnerName(player.getName());

                        this.clans.put(id, new Clan(name, owner, id, info));
                        this.clanIdForName.put(name.toLowerCase(), id);

                        this.playerModule.setClan(owner, id);
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }, "SELECT * FROM clans WHERE owner_uuid=?", owner.toString());
        }, "INSERT INTO clans (clan_name, owner_uuid) VALUES (?, ?)", name, owner.toString());
    }

    public TransferResponse transfer(Clan clan, String playerName) {
        TransferResponse response = clan.transfer(playerName);

        if (response == null) return null;

        databaseModule.execute("UPDATE clans SET owner_uuid=? WHERE id=?", response.getUuid().toString(), clan.getId());
        return response;
    }

}
