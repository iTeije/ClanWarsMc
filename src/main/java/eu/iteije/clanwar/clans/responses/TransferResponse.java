package eu.iteije.clanwar.clans.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class TransferResponse {

    private final UUID uuid;
    private final String playerName;

}
