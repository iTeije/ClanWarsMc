package eu.iteije.clanwar;

import eu.iteije.clanwar.clans.ClanModule;
import eu.iteije.clanwar.commands.CommandModule;
import eu.iteije.clanwar.databases.DatabaseModule;
import eu.iteije.clanwar.games.GameModule;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.npcs.NpcModule;
import eu.iteije.clanwar.players.PlayerModule;
import eu.iteije.clanwar.resources.PluginFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClanWar extends JavaPlugin {

    private DatabaseModule databaseModule;
    private NpcModule npcModule;

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginFile configFile = new PluginFile(this, "config.yml");
        PluginFile kitsFile = new PluginFile(this, "kits.yml");
        PluginFile messagesFile = new PluginFile(this, "messages.yml");
        PluginFile invitesFile = new PluginFile(this, "invites.yml");

        this.databaseModule = new DatabaseModule(this, configFile);
        this.databaseModule.createTables();

        MessageModule messageModule = new MessageModule(messagesFile);
        GameModule gameModule = new GameModule(kitsFile, configFile, messageModule);
        PlayerModule playerModule = new PlayerModule(this, gameModule, databaseModule);
        ClanModule clanModule = new ClanModule(databaseModule, playerModule, messageModule, invitesFile, this);
        this.npcModule = new NpcModule(this, configFile, gameModule);
        CommandModule commandModule = new CommandModule(this, gameModule, messageModule, playerModule, clanModule, npcModule);

        // In case some fucking idiot decides to reload the server
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerModule.savePlayer(player);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.npcModule.unload();

        this.databaseModule.close();
    }
}
