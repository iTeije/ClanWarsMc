package eu.iteije.clanwar.commands.maincommands;

import eu.iteije.clanwar.clans.ClanModule;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.messages.Replacement;
import eu.iteije.clanwar.messages.storage.StorageKey;
import eu.iteije.clanwar.players.PlayerModule;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class CreateClanCommand implements CommandExecutor {

    private final ClanModule clanModule;
    private final PlayerModule playerModule;
    private final MessageModule messageModule;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("clanwar.createclan")) {
                if (args.length == 1) {
                    if (clanModule.getClan(args[0]) == null) {
                        // Check whether the player creating a clan is already in another clan
                        if (playerModule.getPlayer(player.getUniqueId()).getClanId() == -1) {
                            clanModule.createClan(args[0], player.getUniqueId());
                            messageModule.send(sender, StorageKey.CLAN_CREATE_SUCCESS,
                                    new Replacement("%clan_name%", args[0]),
                                    new Replacement("%player_name%", player.getName())
                            );
                        } else {
                            messageModule.send(sender, StorageKey.CLAN_CREATE_UNAVAILABLE);
                        }
                    } else {
                        messageModule.send(sender, StorageKey.CLAN_CREATE_EXISTS);
                    }
                } else {
                    messageModule.send(sender, StorageKey.INVALID_ARGUMENTS);
                }
            } else {
                messageModule.send(sender, StorageKey.PERMISSION_ERROR);
            }
        } else {
            messageModule.send(sender, StorageKey.PROTOCOL_ERROR);
        }

        return true;
    }

}
