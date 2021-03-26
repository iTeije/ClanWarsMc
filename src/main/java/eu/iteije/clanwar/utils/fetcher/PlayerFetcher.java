package eu.iteije.clanwar.utils.fetcher;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class PlayerFetcher {

    public static PlayerDataObject getPlayerData(String playerName) {
        return fetch("https://api.mojang.com/users/profiles/minecraft/" + playerName);
    }

    public static PlayerDataObject getPlayerData(UUID uuid) {
        return fetch("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString());
    }

    private static PlayerDataObject fetch(String link) {
        try {
            URL url = new URL(link);
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

            String uuid = ((String) data.get("id")).replaceAll("(.{8})(.{4})(.{4})(.{4})(.+)", "$1-$2-$3-$4-$5");

            return new PlayerDataObject(UUID.fromString(uuid), (String) data.get("name"));
        } catch (ParseException ignored) {
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}
