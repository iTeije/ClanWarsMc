package eu.iteije.clanwar.commands.subcommands.gamemanager;

import eu.iteije.clanwar.framework.commands.objects.SubCommand;
import org.bukkit.command.CommandSender;

public class GMPlaceNpcSubCommand extends SubCommand {

    public GMPlaceNpcSubCommand() {
        super("placenpc", "Place a join NPC at your location", "gamemanager");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args, String label) {

    }
}
