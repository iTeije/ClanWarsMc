package eu.iteije.clanwar.games;

import eu.iteije.clanwar.games.enums.SpawnPointType;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.messages.Replacement;
import eu.iteije.clanwar.messages.storage.StorageKey;
import eu.iteije.clanwar.resources.PluginFile;
import eu.iteije.clanwar.utils.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GameModule {

    private final Map<SpawnPointType, Location> spawns;

    private final PluginFile kitFile;
    private final PluginFile configFile;

    private final MessageModule messageModule;

    public GameModule(PluginFile kitFile, PluginFile configFile, MessageModule messageModule) {
        this.spawns = new HashMap<>();

        this.kitFile = kitFile;
        this.configFile = configFile;

        this.messageModule = messageModule;

        this.fetchSpawns();
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

}
