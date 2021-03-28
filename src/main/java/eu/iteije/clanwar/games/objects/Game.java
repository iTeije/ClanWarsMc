package eu.iteije.clanwar.games.objects;

import eu.iteije.clanwar.clans.objects.Clan;

public class Game {

    private final Clan firstClan;
    private final Clan secondClan;


    public Game(Clan firstClan, Clan secondClan) {
        this.firstClan = firstClan;
        this.secondClan = secondClan;
    }

    public boolean isParticipating(int clanId) {
        return firstClan.getId() == clanId || secondClan.getId() == clanId;
    }

}
