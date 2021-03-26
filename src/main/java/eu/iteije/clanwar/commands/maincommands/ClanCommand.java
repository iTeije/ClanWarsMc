package eu.iteije.clanwar.commands.maincommands;

import eu.iteije.clanwar.clans.ClanModule;
import eu.iteije.clanwar.commands.subcommands.clan.*;
import eu.iteije.clanwar.framework.commands.CommandManager;
import eu.iteije.clanwar.framework.commands.objects.PluginCommand;
import eu.iteije.clanwar.framework.commands.objects.SubCommand;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.messages.storage.StorageKey;
import eu.iteije.clanwar.players.PlayerModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ClanCommand extends PluginCommand implements CommandExecutor, TabCompleter {

    private final MessageModule messageModule;
    private final CommandManager commandManager;

    public ClanCommand(ClanModule clanModule, PlayerModule playerModule, CommandManager commandManager, MessageModule messageModule) {
        super("clan");
        this.messageModule = messageModule;
        this.commandManager = commandManager;

        this.commandManager.registerSubCommands(this.getSyntax(),
                new ClanCreateSubCommand(clanModule, playerModule, messageModule),
                new ClanInfoSubCommand(clanModule, messageModule, playerModule),
                new ClanTransferSubCommand(clanModule, playerModule, messageModule),
                new ClanDisbandSubCommand(clanModule, playerModule, messageModule),
                new ClanInviteSubCommand(messageModule, clanModule, playerModule),
                new ClanLeaveSubCommand(clanModule, playerModule, messageModule)
        );
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            super.sendHelp(sender, commandManager.getSubCommandsHandlers(super.getSyntax()));
            return true;
        }

        SubCommand subCommand = commandManager.getSubCommand(super.getSyntax(), args[0].toLowerCase());

        if (subCommand != null) {
            if (subCommand.hasPermission(sender)) {
                String[] subArgs = new String[args.length - 1];
                if (args.length != 1) System.arraycopy(args, 1, subArgs, 0, args.length - 1);

                try {
                    subCommand.onExecute(sender, subArgs, label);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    messageModule.send(sender, StorageKey.EXECUTE_ERROR);
                }
            } else {
                messageModule.send(sender, StorageKey.PERMISSION_ERROR);
            }
        } else {
            super.sendHelp(sender, commandManager.getSubCommandsHandlers(super.getSyntax()));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        for (String subCommand : commandManager.getSubCommands(super.getSyntax())) {
            if (args.length <= 1 && subCommand.startsWith(args[0])) completions.add(subCommand);
        }

        return completions;
    }
}
