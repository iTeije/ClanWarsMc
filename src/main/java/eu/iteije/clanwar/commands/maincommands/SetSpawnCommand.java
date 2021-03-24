package eu.iteije.clanwar.commands.maincommands;

import eu.iteije.clanwar.games.GameModule;
import eu.iteije.clanwar.games.enums.SpawnPointType;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.messages.Replacement;
import eu.iteije.clanwar.messages.storage.StorageKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetSpawnCommand implements CommandExecutor, TabCompleter {

    private final GameModule gameModule;
    private final MessageModule messageModule;

    public SetSpawnCommand(GameModule gameModule, MessageModule messageModule) {
        this.gameModule = gameModule;
        this.messageModule = messageModule;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("clanwar.setspawn")) {
                if (args.length == 1) {
                    try {
                        SpawnPointType type = SpawnPointType.valueOf(args[0].toUpperCase());

                        Player player = (Player) sender;
                        gameModule.setSpawn(type, player.getLocation());

                        messageModule.send(player, StorageKey.SPAWN_SET_SUCCESS, new Replacement("%spawn_type%", type.name()));
                    } catch (IllegalArgumentException | NullPointerException exception) {
                        // Invalid arguments
                        messageModule.send(sender, StorageKey.INVALID_ARGUMENTS);
                    }
                } else {
                    // Missing arguments
                    messageModule.send(sender, StorageKey.INVALID_ARGUMENTS);
                }
            } else {
                // Send permission error
                messageModule.send(sender, StorageKey.PERMISSION_ERROR);
            }
        } else {
            // Command is player only, send protocol error
            messageModule.send(sender, StorageKey.PROTOCOL_ERROR);
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
