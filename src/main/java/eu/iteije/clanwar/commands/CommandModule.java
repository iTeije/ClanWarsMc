package eu.iteije.clanwar.commands;

import eu.iteije.clanwar.ClanWar;
import eu.iteije.clanwar.commands.maincommands.SetSpawnCommand;
import eu.iteije.clanwar.resources.PluginFile;

public class CommandModule {

    public CommandModule(ClanWar instance, PluginFile configFile) {
        SetSpawnCommand setSpawnCommand = new SetSpawnCommand(configFile);
        instance.getCommand("setspawn").setExecutor(setSpawnCommand);
        instance.getCommand("setspawn").setTabCompleter(setSpawnCommand);
    }
}
