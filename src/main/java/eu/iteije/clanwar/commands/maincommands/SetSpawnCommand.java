package eu.iteije.clanwar.commands.maincommands;

import eu.iteije.clanwar.resources.PluginFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetSpawnCommand implements CommandExecutor, TabCompleter {

    enum SpawnPointType {
        LOBBY,
        GAME
    }

    private final PluginFile configFile;

    public SetSpawnCommand(PluginFile configFile) {
        this.configFile = configFile;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("clanwar.setspawn")) {

            } else {
                // TODO permission error
            }
        } else {
            // TODO protocol error
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length <= 1) {
            return Arrays.asList(Arrays.stream(SpawnPointType.values()).map(Enum::name).toArray(String[]::new));
        }
        return new ArrayList<>();
    }
}
