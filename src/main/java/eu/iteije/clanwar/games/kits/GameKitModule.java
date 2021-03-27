package eu.iteije.clanwar.games.kits;

import eu.iteije.clanwar.games.kits.objects.Kit;
import eu.iteije.clanwar.resources.PluginFile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameKitModule {

    private final PluginFile kitFile;
    private final Map<String, Kit> kits;

    public GameKitModule(PluginFile kitFile) {
        this.kitFile = kitFile;
        this.kits = new HashMap<>();

        for (String kit : kitFile.getConfiguration().getConfigurationSection("kits").getKeys(false)) {
            this.kits.put(kit, new Kit(kit, kitFile));
        }
    }

    public Collection<Kit> getKits() {
        return this.kits.values();
    }

}
