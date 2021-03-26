package eu.iteije.clanwar.clans.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KickResponse {

    private final Boolean success;
    private final String playerName;

}
