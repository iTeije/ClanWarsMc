package eu.iteije.clanwar.commands.subcommands.clan;

import eu.iteije.clanwar.clans.ClanModule;
import eu.iteije.clanwar.clans.objects.Clan;
import eu.iteije.clanwar.clans.responses.TransferResponse;
import eu.iteije.clanwar.framework.commands.objects.SubCommand;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.messages.Replacement;
import eu.iteije.clanwar.messages.storage.StorageKey;
import eu.iteije.clanwar.players.PlayerModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanTransferSubCommand extends SubCommand {

    private final ClanModule clanModule;
    private final PlayerModule playerModule;
    private final MessageModule messageModule;

    public ClanTransferSubCommand(ClanModule clanModule, PlayerModule playerModule, MessageModule messageModule) {
        super("transfer", "Transfer ownership to another player", "clan");
        this.clanModule = clanModule;
        this.playerModule = playerModule;
        this.messageModule = messageModule;
    }

    @Override
    public void onExecute(CommandSender sender, String[] args, String label) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Clan clan = clanModule.getClan(playerModule.getPlayer(player.getUniqueId()).getClanId());
            if (clan != null) {
                // Make sure the player transferring the ownership is actually the owner of the clan
                if (clan.getOwner().equals(player.getUniqueId())) {
                    if (args.length == 1) {
                        TransferResponse response = clanModule.transfer(clan, args[0]);
                        if (response != null) {
                            messageModule.send(sender, StorageKey.CLAN_TRANSFER_SUCCESS, new Replacement("%player_name%", response.getPlayerName()));
                        } else {
                            messageModule.send(sender, StorageKey.CLAN_TRANSFER_ERROR);
                        }
                    } else {
                        messageModule.send(sender, StorageKey.INVALID_ARGUMENTS);
                    }
                } else {
                    messageModule.send(sender, StorageKey.CLAN_OWNERSHIP_ERROR);
                }
            } else {
                messageModule.send(sender, StorageKey.CLAN_UNAVAILABLE);
            }
        } else {
            messageModule.send(sender, StorageKey.PROTOCOL_ERROR);
        }
    }
}
