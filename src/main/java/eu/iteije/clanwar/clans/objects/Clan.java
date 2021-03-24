package eu.iteije.clanwar.clans.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class Clan {

    private final String name;
    private final UUID owner;
    private final Integer id;

    @Setter @Getter
    private ClanInfo info;

    public Clan(String name, UUID owner, Integer id) {
        this.name = name;
        this.owner = owner;
        this.id = id;
    }

}
