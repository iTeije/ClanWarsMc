package eu.iteije.clanwar.commands.subcommands;

import eu.iteije.clanwar.clans.ClanModule;
import eu.iteije.clanwar.framework.commands.objects.SubCommand;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.messages.Replacement;
import eu.iteije.clanwar.messages.storage.StorageKey;
import eu.iteije.clanwar.players.PlayerModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateClanSubCommand extends SubCommand {

    private final ClanModule clanModule;
    private final PlayerModule playerModule;
    private final MessageModule messageModule;

    public CreateClanSubCommand(ClanModule clanModule, PlayerModule playerModule, MessageModule messageModule) {
        super("create", "Create a new clan", "clan");
        this.clanModule = clanModule;
        this.playerModule = playerModule;
        this.messageModule = messageModule;
    }

    @Override
    public void onExecute(CommandSender sender, String[] args, String label) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
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
            messageModule.send(sender, StorageKey.PROTOCOL_ERROR);
        }
    }

}
