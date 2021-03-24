package eu.iteije.clanwar.clans;

import eu.iteije.clanwar.clans.objects.Clan;
import eu.iteije.clanwar.clans.objects.ClanInfo;
import eu.iteije.clanwar.databases.DatabaseModule;
import eu.iteije.clanwar.players.PlayerModule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ClanModule {

    private final DatabaseModule databaseModule;
    private final PlayerModule playerModule;
    private final Map<String, Clan> clans;

    public ClanModule(DatabaseModule databaseModule, PlayerModule playerModule) {
        this.databaseModule = databaseModule;
        this.playerModule = playerModule;
        this.clans = new HashMap<>();

        this.loadClans(databaseModule);
    }

    private void loadClans(DatabaseModule databaseModule) {
        databaseModule.fetch(rowSet -> {
            try (ResultSet set = rowSet.getOriginal()) {
                while (set.next()) {
                    int clanId = set.getInt("id");
                    String name = set.getString("clan_name");
                    UUID uuid = UUID.fromString(set.getString("owner_uuid"));

                    Clan clan = new Clan(name, uuid, clanId);

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
                                    info.addMember(memberUsername);
                                }
                            }
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                        }
                    }, "SELECT uuid, username FROM players WHERE clan_id=?", clanId);

                    clan.setInfo(info);
                    this.clans.put(name.toLowerCase(), clan);
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }, "SELECT * FROM clans");
    }

    public Clan getClan(String name) {
        return this.clans.get(name.toLowerCase());
    }


    public void createClan(String name, UUID owner) {
        databaseModule.execute("INSERT INTO clans (clan_name, owner_uuid) VALUES (?, ?)", name, owner.toString());

        AtomicInteger id = new AtomicInteger(-1);
        databaseModule.fetch(rowSet -> {
            try (ResultSet set = rowSet.getOriginal()) {
                while (set.next()) {
                    playerModule.getPlayer(owner).setClanId(set.getInt("id"));
                    id.set(set.getInt("id"));
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }, "SELECT * FROM clans WHERE owner_uuid=?", owner.toString());

        this.clans.put(name.toLowerCase(), new Clan(name, owner, id.get()));
        this.playerModule.setClan(owner, id.get());
    }

}
