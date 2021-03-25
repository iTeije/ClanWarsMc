package eu.iteije.clanwar.clans.objects;

import eu.iteije.clanwar.clans.responses.TransferResponse;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Getter
public class Clan {

    private final String name;
    private UUID owner;
    private final Integer id;

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

    public TransferResponse transfer(String playerName) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);

            BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = input.readLine()) != null) {
                content.append(inputLine);
            }
            input.close();
            con.disconnect();

            String output = content.toString();
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(output);

            playerName = (String) data.get("name");
            UUID uuid = this.info.getMembers().get(playerName);

            if (uuid != null) {
                moveOwnerToMembers();

                this.owner = uuid;
                this.info.setOwnerName(playerName);
                this.info.removeMember(playerName);
                return new TransferResponse(uuid, playerName);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private void moveOwnerToMembers() {
        this.info.addMember(this.owner, this.info.getOwnerName());
    }

}
