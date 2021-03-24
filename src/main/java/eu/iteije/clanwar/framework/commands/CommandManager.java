package eu.iteije.clanwar.framework.commands;

import eu.iteije.clanwar.framework.commands.objects.SubCommand;

import java.util.*;

public class CommandManager {

    // Map<//primary command//, Map<//subcommand syntax//, //subcommand object//>>
    private final Map<String, Map<String, SubCommand>> subCommands;

    public CommandManager() {
        this.subCommands = new HashMap<>();
    }

    /**
     * @param mainCommand primary command, as registered in plugin.yml
     * @return the syntax for all registered subcommands of the given main command
     */
    public List<String> getSubCommands(String mainCommand) {
        if (subCommands.get(mainCommand) != null) return new ArrayList<>(subCommands.get(mainCommand).keySet());
        return new ArrayList<>();
    }

    /**
     * @param mainCommand primary command, as registered in the plugin.yml file
     * @return all registered subcommand objects
     */
    public List<SubCommand> getSubCommandsHandlers(String mainCommand) {
        return new ArrayList<>(subCommands.get(mainCommand).values());
    }

    /**
     * @param mainCommand primary command, as registered in the plugin.yml file
     * @param subCommands subcommands to register
     */
    public void registerSubCommands(String mainCommand, SubCommand... subCommands) {
        Map<String, SubCommand> subCommandMap = this.subCommands.getOrDefault(mainCommand, new HashMap<>());
        for (SubCommand subCommand : subCommands) {
            subCommandMap.put(subCommand.getCommand(), subCommand);
        }
        this.subCommands.put(mainCommand, subCommandMap);
    }

    /**
     * @param mainCommand primary command, as registered in the plugin.yml file
     * @param syntax sub command syntax to look for
     * @return found sub command, or null when none was found
     */
    public SubCommand getSubCommand(String mainCommand, String syntax) {
        return this.subCommands.getOrDefault(mainCommand, new HashMap<>()).get(syntax);
    }

}
