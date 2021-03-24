package eu.iteije.clanwar;

import eu.iteije.clanwar.commands.CommandModule;
import eu.iteije.clanwar.games.GameModule;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.resources.PluginFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClanWar extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginFile configFile = new PluginFile(this, "config.yml");
        PluginFile kitsFile = new PluginFile(this, "kits.yml");
        PluginFile messagesFile = new PluginFile(this, "messages.yml");

        MessageModule messageModule = new MessageModule(messagesFile);
        GameModule gameModule = new GameModule(kitsFile, configFile, messageModule);
        CommandModule commandModule = new CommandModule(this, gameModule, messageModule);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}