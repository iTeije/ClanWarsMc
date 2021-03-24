package eu.iteije.clanwar.commands;

import eu.iteije.clanwar.ClanWar;
import eu.iteije.clanwar.commands.maincommands.SetSpawnCommand;
import eu.iteije.clanwar.games.GameModule;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.resources.PluginFile;

public class CommandModule {

    public CommandModule(ClanWar instance, GameModule gameModule, MessageModule messageModule) {
        SetSpawnCommand setSpawnCommand = new SetSpawnCommand(gameModule, messageModule);
        instance.getCommand("setspawn").setExecutor(setSpawnCommand);
        instance.getCommand("setspawn").setTabCompleter(setSpawnCommand);
    }
}
