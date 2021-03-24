package eu.iteije.clanwar.players.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
public class CWPlayer {

    private final UUID uuid;
    @Setter @Getter
    private int clanId;

}
