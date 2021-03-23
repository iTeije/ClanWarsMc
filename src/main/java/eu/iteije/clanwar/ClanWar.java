package eu.iteije.clanwar;

import eu.iteije.clanwar.commands.CommandModule;
import eu.iteije.clanwar.resources.PluginFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClanWar extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginFile configFile = new PluginFile(this, "config.yml");
        PluginFile kitsFile = new PluginFile(this, "kits.yml");

        CommandModule commandModule = new CommandModule(this, configFile);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
