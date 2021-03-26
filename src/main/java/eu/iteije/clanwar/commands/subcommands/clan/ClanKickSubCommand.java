package eu.iteije.clanwar.commands.subcommands.clan;

import eu.iteije.clanwar.clans.ClanModule;
import eu.iteije.clanwar.clans.objects.Clan;
import eu.iteije.clanwar.clans.responses.KickResponse;
import eu.iteije.clanwar.framework.commands.objects.SubCommand;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.messages.Replacement;
import eu.iteije.clanwar.messages.storage.StorageKey;
import eu.iteije.clanwar.players.PlayerModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanKickSubCommand extends SubCommand {

    private final ClanModule clanModule;
    private final MessageModule messageModule;
    private final PlayerModule playerModule;

    public ClanKickSubCommand(MessageModule messageModule, ClanModule clanModule, PlayerModule playerModule) {
        super("kick", "Kick a player from your clan", "clan");
        this.clanModule = clanModule;
        this.messageModule = messageModule;
        this.playerModule = playerModule;
    }

    @Override
    public void onExecute(CommandSender sender, String[] args, String label) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Clan clan = clanModule.getClan(playerModule.getPlayer(player.getUniqueId()).getClanId());
            if (clan != null) {
                // Check whether the sender is the owner of the clan
                if (clan.getOwner().equals(player.getUniqueId())) {
                    if (args.length == 1) {
                        KickResponse response = clanModule.kick(clan, args[0]);

                        if (response.getSuccess()) {
                            messageModule.send(player, StorageKey.CLAN_KICK_SUCCESS, new Replacement("%player_name%", response.getPlayerName()));
                        } else {
                            messageModule.send(player, StorageKey.CLAN_KICK_ERROR);
                        }
                    } else {
                        messageModule.send(player, StorageKey.INVALID_ARGUMENTS);
                    }
                } else {
                    messageModule.send(player, StorageKey.CLAN_OWNERSHIP_ERROR);
                }
            } else {
                messageModule.send(player, StorageKey.CLAN_UNAVAILABLE);
            }
        } else {
            messageModule.send(sender, StorageKey.PROTOCOL_ERROR);
        }
    }
}
