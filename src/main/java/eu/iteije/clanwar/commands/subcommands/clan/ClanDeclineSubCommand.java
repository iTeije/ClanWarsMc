package eu.iteije.clanwar.commands.subcommands.clan;

import eu.iteije.clanwar.clans.ClanModule;
import eu.iteije.clanwar.clans.invites.ClanInviteModule;
import eu.iteije.clanwar.clans.objects.Clan;
import eu.iteije.clanwar.framework.commands.objects.SubCommand;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.messages.Replacement;
import eu.iteije.clanwar.messages.storage.StorageKey;
import eu.iteije.clanwar.players.PlayerModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanDeclineSubCommand extends SubCommand {

    private final ClanInviteModule inviteModule;
    private final ClanModule clanModule;
    private final MessageModule messageModule;
    private final PlayerModule playerModule;

    public ClanDeclineSubCommand(MessageModule messageModule, ClanModule clanModule, PlayerModule playerModule) {
        super("decline", "Decline a clan invitation", "clan");
        this.messageModule = messageModule;
        this.clanModule = clanModule;
        this.playerModule = playerModule;
        this.inviteModule = clanModule.getInviteModule();
    }

    @Override
    public void onExecute(CommandSender sender, String[] args, String label) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            // Check whether the player has already accepted another invitation
            if (clanModule.getClan(playerModule.getPlayer(uuid).getClanId()) == null) {
                if (args.length == 1) {
                    Clan clan = clanModule.getClan(args[0]);
                    if (clan != null) {
                        int id = clan.getId();
                        boolean hasInvite = this.inviteModule.hasInvite(id, uuid);
                        // Check if the player actually has a clan invite from the selected clan
                        if (hasInvite) {
                            // Decline the invitation from the clan
                            this.inviteModule.handleInvitation(clan, false, uuid, player.getName());
                            messageModule.send(player, StorageKey.CLAN_DECLINE_SUCCESS, new Replacement("%clan_name%", clan.getName()));
                        } else {
                            messageModule.send(player, StorageKey.CLAN_HANDLEINVITATION_NOT_FOUND);
                        }
                    } else {
                        messageModule.send(sender, StorageKey.CLAN_UNAVAILABLE);
                    }
                } else {
                    messageModule.send(player, StorageKey.INVALID_ARGUMENTS);
                }
            } else {
                messageModule.send(player, StorageKey.CLAN_HANDLEINVITATION_UNAVAILABLE);
            }
        } else {
            messageModule.send(sender, StorageKey.PROTOCOL_ERROR);
        }
    }

}
