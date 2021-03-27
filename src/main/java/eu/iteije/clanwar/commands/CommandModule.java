package eu.iteije.clanwar.commands;

import eu.iteije.clanwar.ClanWar;
import eu.iteije.clanwar.clans.ClanModule;
import eu.iteije.clanwar.commands.maincommands.ClanCommand;
import eu.iteije.clanwar.commands.maincommands.GameManagerCommand;
import eu.iteije.clanwar.commands.maincommands.SetSpawnCommand;
import eu.iteije.clanwar.framework.commands.CommandManager;
import eu.iteije.clanwar.games.GameModule;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.npcs.NpcModule;
import eu.iteije.clanwar.players.PlayerModule;

public class CommandModule {

    public CommandModule(ClanWar instance, GameModule gameModule, MessageModule messageModule, PlayerModule playerModule, ClanModule clanModule, NpcModule npcModule) {
        CommandManager manager = new CommandManager();

        SetSpawnCommand setSpawnCommand = new SetSpawnCommand(gameModule, messageModule);
        instance.getCommand("setspawn").setExecutor(setSpawnCommand);
        instance.getCommand("setspawn").setTabCompleter(setSpawnCommand);

        ClanCommand clanCommand = new ClanCommand(clanModule, playerModule, manager, messageModule);
        instance.getCommand("clan").setExecutor(clanCommand);
        instance.getCommand("clan").setTabCompleter(clanCommand);

        GameManagerCommand gameManagerCommand = new GameManagerCommand(manager, messageModule, npcModule);
        instance.getCommand("gamemanager").setExecutor(gameManagerCommand);
        instance.getCommand("gamemanager").setTabCompleter(gameManagerCommand);
    }
}
