package eu.iteije.clanwar.framework.commands.objects;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public abstract class SubCommand {

    @Getter private final String command;
    @Getter private final String description;

    private final String mainCommand;

    public SubCommand(String command, String description, String mainCommand) {
        this.command = command;
        this.description = description;
        this.mainCommand = mainCommand;

        Bukkit.getPluginManager().addPermission(new Permission("clanwar." + mainCommand + "." + command));
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("clanwar." + mainCommand + "." + command);
    }

    public abstract void onExecute(CommandSender sender, String[] args, String label);

}
