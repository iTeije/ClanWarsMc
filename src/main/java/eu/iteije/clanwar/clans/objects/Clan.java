package eu.iteije.clanwar.clans.objects;

import lombok.Getter;

import java.util.UUID;

public class Clan {

    @Getter private final String name;
    private final UUID owner;
    private final Integer id;

    @Getter
    private final ClanInfo info;

    public Clan(String name, UUID owner, Integer id) {
        this(name, owner, id, new ClanInfo());
    }

    public Clan(String name, UUID owner, Integer id, ClanInfo info) {
        this.name = name;
        this.owner = owner;
        this.id = id;
        this.info = info;
    }

}
