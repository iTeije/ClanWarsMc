package eu.iteije.clanwar.clans;

import eu.iteije.clanwar.ClanWar;
import eu.iteije.clanwar.clans.invites.ClanInviteModule;
import eu.iteije.clanwar.clans.objects.Clan;
import eu.iteije.clanwar.clans.objects.ClanInfo;
import eu.iteije.clanwar.clans.responses.KickResponse;
import eu.iteije.clanwar.clans.responses.TransferResponse;
import eu.iteije.clanwar.databases.DatabaseModule;
import eu.iteije.clanwar.games.GameModule;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.players.PlayerModule;
import eu.iteije.clanwar.resources.PluginFile;
import eu.iteije.clanwar.utils.fetcher.PlayerDataObject;
import eu.iteije.clanwar.utils.fetcher.PlayerFetcher;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClanModule {

    private final DatabaseModule databaseModule;
    private final PlayerModule playerModule;
    @Getter private final ClanInviteModule inviteModule;

    private final Map<Integer, Clan> clans;
    private final Map<String, Integer> clanIdForName;

    public ClanModule(DatabaseModule databaseModule, PlayerModule playerModule, MessageModule messageModule, PluginFile invitesFile, GameModule gameModule, ClanWar instance) {
        this.databaseModule = databaseModule;
        this.playerModule = playerModule;

        this.inviteModule = new ClanInviteModule(databaseModule, this, messageModule, invitesFile, instance);
        this.clans = new HashMap<>();
        this.clanIdForName = new HashMap<>();

        this.loadClans(databaseModule);
        gameModule.setActiveGame(this.clans);
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

    public void disband(Clan clan, PlayerModule playerModule) {
        Collection<UUID> members = clan.getInfo().getMembers().values();
        members.forEach(uuid -> playerModule.setClan(uuid, -1));
        playerModule.setClan(clan.getOwner(), -1);

        String clanName = clan.getName();
        int id = clan.getId();

        databaseModule.execute("DELETE FROM clans WHERE id=?", clan.getId());
        this.clans.remove(id);
        this.clanIdForName.remove(clanName);
    }

    public void remove(Clan clan, UUID uuid, String name) {
        this.playerModule.setClan(uuid, -1);
        clan.getInfo().removeMember(name);
    }

    public void remove(Clan clan, UUID uuid) {
        PlayerDataObject data = PlayerFetcher.getPlayerData(uuid);
        if (data != null) remove(clan, uuid, data.getExactPlayerName());
    }

    public void add(Clan clan, UUID uuid, String name) {
        this.playerModule.setClan(uuid, clan.getId());
        clan.getInfo().addMember(uuid, name);
    }

    public void add(Clan clan, UUID uuid) {
        PlayerDataObject data = PlayerFetcher.getPlayerData(uuid);
        if (data != null) add(clan, uuid, data.getExactPlayerName());
    }

    public KickResponse kick(Clan clan, String name) {
        PlayerDataObject data = PlayerFetcher.getPlayerData(name);
        if (data == null) return new KickResponse(false, null);

        UUID memberUniqueId = clan.getInfo().getFromName(data.getExactPlayerName());
        if (memberUniqueId == null) return new KickResponse(false, null);

        clan.getInfo().removeMember(data.getExactPlayerName());
        playerModule.setClan(memberUniqueId, -1);
        return new KickResponse(true, data.getExactPlayerName());
    }

}
