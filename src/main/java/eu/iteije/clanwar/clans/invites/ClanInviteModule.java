package eu.iteije.clanwar.clans.invites;

import eu.iteije.clanwar.ClanWar;
import eu.iteije.clanwar.clans.ClanModule;
import eu.iteije.clanwar.clans.invites.listeners.JoinInviteListener;
import eu.iteije.clanwar.clans.objects.Clan;
import eu.iteije.clanwar.databases.DatabaseModule;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.messages.Replacement;
import eu.iteije.clanwar.messages.storage.StorageKey;
import eu.iteije.clanwar.resources.PluginFile;
import eu.iteije.clanwar.utils.fetcher.PlayerDataObject;
import eu.iteije.clanwar.utils.fetcher.PlayerFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ClanInviteModule {

    private final DatabaseModule databaseModule;
    private final MessageModule messageModule;
    private final ClanModule clanModule;

    private final PluginFile invitesFile;

    public ClanInviteModule(DatabaseModule databaseModule, ClanModule clanModule, MessageModule messageModule, PluginFile invitesFile, ClanWar instance) {
        this.databaseModule = databaseModule;
        this.messageModule = messageModule;
        this.clanModule = clanModule;

        this.invitesFile = invitesFile;

        PluginManager manager = instance.getServer().getPluginManager();
        manager.registerEvents(new JoinInviteListener(this), instance);
    }

    public void invite(Clan clan, Player inviter, String playerName) {
        PlayerDataObject data = PlayerFetcher.getPlayerData(playerName);
        if (data == null) {
            messageModule.send(inviter, StorageKey.PLAYER_NOT_FOUND);
            return;
        }

        if (hasInvite(clan.getId(), data.getUniqueId())) {
            messageModule.send(inviter, StorageKey.CLAN_INVITE_PENDING);
            return;
        }

        databaseModule.fetch(cachedRowSet -> {
            try {
                ResultSet set = cachedRowSet.getOriginal();

                boolean has = false;
                while (set.next()) {
                    has = true;
                    int clanId = set.getInt("clan_id");
                    if (clanId == -1) {
                        // Send invite
                        int inviterId = clan.getId();
                        saveInvite(inviterId, data.getUniqueId().toString());

                        try {
                            UUID uuid = data.getUniqueId();
                            Player player = Bukkit.getPlayer(uuid);

                            if (player != null) {
                                messageModule.send(player, StorageKey.CLAN_INVITE_TARGET,
                                        new Replacement("%clan_name%", clan.getName()),
                                        new Replacement("%clan_owner_name%", clan.getInfo().getOwnerName())
                                );
                            }

                        } catch (IllegalArgumentException ignored) {}

                        messageModule.send(inviter, StorageKey.CLAN_INVITE_SUCCESS, new Replacement("%player_name%", data.getExactPlayerName()));
                    } else {
                        // Player is in another clan
                        messageModule.send(inviter, StorageKey.CLAN_INVITE_ANOTHER_CLAN);
                    }
                }

                if (!has) {
                    messageModule.send(inviter, StorageKey.PLAYER_NOT_FOUND);
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }, "SELECT clan_id FROM players WHERE username=?", data.getExactPlayerName());
    }

    private void saveInvite(int id, String uuid) {
        List<Integer> currentInvites = invitesFile.getConfiguration().getIntegerList(uuid);
        currentInvites.add(id);
        this.invitesFile.getConfiguration().set(uuid, currentInvites);
        this.invitesFile.save();
    }

    public void join(Player player) {
        List<Integer> invites = invitesFile.getConfiguration().getIntegerList(player.getUniqueId().toString());

        for (Integer clanId : invites) {
            Clan clan = this.clanModule.getClan(clanId);
            messageModule.send(player, StorageKey.CLAN_INVITE_TARGET,
                    new Replacement("%clan_name%", clan.getName()),
                    new Replacement("%clan_owner_name%", clan.getInfo().getOwnerName())
            );
        }
    }

    public boolean hasInvite(int id, UUID uuid) {
        List<Integer> invites = invitesFile.getConfiguration().getIntegerList(uuid.toString());
        if (invites.size() == 0) return false;
        return invites.contains(id);
    }

    public void handleInvitation(Clan clan, boolean accept, UUID uuid, String playerName) {
        if (accept) {
            invitesFile.getConfiguration().set(uuid.toString(), null);
            clanModule.add(clan, uuid, playerName);
        } else {
            List<Integer> invites = invitesFile.getConfiguration().getIntegerList(uuid.toString());
            invites.removeAll(Collections.singletonList(clan.getId()));
            invitesFile.getConfiguration().set(uuid.toString(), invites);
        }

        invitesFile.save();
    }

}
