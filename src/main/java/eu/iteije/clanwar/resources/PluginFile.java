package eu.iteije.clanwar.resources;

import eu.iteije.clanwar.ClanWar;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PluginFile {

    @Getter private final FileConfiguration configuration;
    private final File file;

    public PluginFile(ClanWar instance, String fileName) {
        this.loadFile(instance, fileName);

        this.file = new File(instance.getDataFolder(), fileName);
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            this.configuration.save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void loadFile(ClanWar instance, String fileName) {
        if (!instance.getDataFolder().exists()) instance.getDataFolder().mkdir();

        File file = new File(instance.getDataFolder(), fileName);

        if (!file.exists()) {
            instance.saveResource(fileName, true);
        }
    }

}
