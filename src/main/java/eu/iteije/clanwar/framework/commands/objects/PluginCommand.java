package eu.iteije.clanwar.framework.commands.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

@AllArgsConstructor
public class PluginCommand {

    @Getter private final String syntax;

    public void sendHelp(CommandSender sender, List<SubCommand> subCommands) {
        sendMessage(sender, "&7");
        sendMessage(sender, "&7---------- &e&lCLAN WAR &7----------");
        sendMessage(sender, "&7");
        for (SubCommand subCommand : subCommands) {
            sendMessage(sender, "&a/" + syntax + " " + subCommand.getCommand() + " &7" + subCommand.getDescription());
        }
        sendMessage(sender, "&7");
        sendMessage(sender, "&7---------- &e&lCLAN WAR &7----------");
    }

    private void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
