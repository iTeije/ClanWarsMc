package eu.iteije.clanwar.clans.objects;

import eu.iteije.clanwar.clans.responses.TransferResponse;
import eu.iteije.clanwar.utils.fetcher.PlayerDataObject;
import eu.iteije.clanwar.utils.fetcher.PlayerFetcher;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Clan {

    private final String name;
    private UUID owner;
    private final Integer id;

    private final ClanInfo info;

    public Clan(String name, UUID owner, Integer id, ClanInfo info) {
        this.name = name;
        this.owner = owner;
        this.id = id;
        this.info = info;
    }

    public TransferResponse transfer(String playerName) {
        PlayerDataObject data = PlayerFetcher.getPlayerData(playerName);
        if (data == null) return null;
        playerName = data.getExactPlayerName();
        UUID uuid = this.info.getMembers().get(playerName);

        if (uuid != null) {
            moveOwnerToMembers();

            this.owner = uuid;
            this.info.setOwnerName(playerName);
            this.info.removeMember(playerName);
            return new TransferResponse(uuid, playerName);
        }

        return null;
    }

    private void moveOwnerToMembers() {
        this.info.addMember(this.owner, this.info.getOwnerName());
    }

}
