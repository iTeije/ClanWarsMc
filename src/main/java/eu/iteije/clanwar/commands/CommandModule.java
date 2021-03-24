package eu.iteije.clanwar.commands;

import eu.iteije.clanwar.ClanWar;
import eu.iteije.clanwar.clans.ClanModule;
import eu.iteije.clanwar.commands.maincommands.CreateClanCommand;
import eu.iteije.clanwar.commands.maincommands.SetSpawnCommand;
import eu.iteije.clanwar.games.GameModule;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.players.PlayerModule;

public class CommandModule {

    public CommandModule(ClanWar instance, GameModule gameModule, MessageModule messageModule, PlayerModule playerModule, ClanModule clanModule) {
        SetSpawnCommand setSpawnCommand = new SetSpawnCommand(gameModule, messageModule);
        instance.getCommand("setspawn").setExecutor(setSpawnCommand);
        instance.getCommand("setspawn").setTabCompleter(setSpawnCommand);

        CreateClanCommand createClanCommand = new CreateClanCommand(clanModule, playerModule, messageModule);
        instance.getCommand("createclan").setExecutor(createClanCommand);
    }
}
