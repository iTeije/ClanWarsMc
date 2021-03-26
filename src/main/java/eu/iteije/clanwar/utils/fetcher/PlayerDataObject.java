package eu.iteije.clanwar.utils.fetcher;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class PlayerDataObject {

    private final UUID uniqueId;
    private final String exactPlayerName;

}
