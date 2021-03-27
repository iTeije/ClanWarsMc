package eu.iteije.clanwar.games;

import eu.iteije.clanwar.clans.objects.Clan;
import eu.iteije.clanwar.games.enums.SpawnPointType;
import eu.iteije.clanwar.games.kits.GameKitModule;
import eu.iteije.clanwar.games.menus.SelectKitMenu;
import eu.iteije.clanwar.games.objects.Game;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.messages.Replacement;
import eu.iteije.clanwar.messages.storage.StorageKey;
import eu.iteije.clanwar.players.objects.CWPlayer;
import eu.iteije.clanwar.resources.PluginFile;
import eu.iteije.clanwar.utils.serializers.LocationSerializer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GameModule {

    private final Map<SpawnPointType, Location> spawns;

    private final PluginFile configFile;
    private final MessageModule messageModule;

    private final SelectKitMenu selectKitMenu;
    @Getter
    private Game activeGame;

    public GameModule(PluginFile kitFile, PluginFile configFile, MessageModule messageModule) {
        this.spawns = new HashMap<>();

        this.configFile = configFile;

        GameKitModule kitModule = new GameKitModule(kitFile);
        this.messageModule = messageModule;
        this.selectKitMenu = new SelectKitMenu(kitModule, this);

        this.fetchSpawns();
    }

    public void setActiveGame(Map<Integer, Clan> clans) {
        String currentGame = this.configFile.getConfiguration().getString("game");
        if (currentGame != null) {
            try {
                String[] data = currentGame.split(":");
                if (data.length != 2) return;
                Clan firstClan = clans.get(Integer.parseInt(data[0]));
                Clan secondClan = clans.get(Integer.parseInt(data[1]));

                if (firstClan == null || secondClan == null) return;
                this.activeGame = new Game(firstClan, secondClan);
            } catch (NumberFormatException ignored) {}
        }

    }

    private void fetchSpawns() {
        for (SpawnPointType type : SpawnPointType.values()) {
            String input = configFile.getConfiguration().getString("spawns." + type.name());
            if (input != null) {
                this.spawns.put(type, LocationSerializer.deserializeLocation(input));
            }
        }
    }

    public void setSpawn(SpawnPointType type, Location location) {
        this.configFile.getConfiguration().set("spawns." + type.name(), LocationSerializer.serializeLocation(location));
        this.configFile.save();
        this.spawns.put(type, location);
    }

    public Location getSpawn(SpawnPointType type) {
        return this.spawns.get(type);
    }

    public void teleport(Player player, SpawnPointType type) {
        Location location = this.spawns.get(type);
        if (location != null) {
            player.teleport(location);
        } else {
            messageModule.send(player, StorageKey.SPAWN_TP_NOT_FOUND, new Replacement("%spawn_type%", type.name()));
        }
    }

    public void openKitMenu(CWPlayer player) {
        if (activeGame != null) {
            if (activeGame.isParticipating(player.getClanId())) {
                this.selectKitMenu.open(Bukkit.getPlayer(player.getUuid()));
            } else {
                // TODO not participating message
            }
        } else {
            // TODO no active game message
        }
    }

}
