package eu.iteije.clanwar.commands.subcommands.clan;

import eu.iteije.clanwar.clans.ClanModule;
import eu.iteije.clanwar.clans.invites.ClanInviteModule;
import eu.iteije.clanwar.clans.objects.Clan;
import eu.iteije.clanwar.framework.commands.objects.SubCommand;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.messages.storage.StorageKey;
import eu.iteije.clanwar.players.PlayerModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanInviteSubCommand extends SubCommand {

    private final ClanInviteModule inviteModule;
    private final ClanModule clanModule;
    private final MessageModule messageModule;
    private final PlayerModule playerModule;

    public ClanInviteSubCommand(MessageModule messageModule, ClanModule clanModule, PlayerModule playerModule) {
        super("invite", "Invite a player", "clan");
        this.inviteModule = clanModule.getInviteModule();
        this.messageModule = messageModule;
        this.clanModule = clanModule;
        this.playerModule = playerModule;
    }

    @Override
    public void onExecute(CommandSender sender, String[] args, String label) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Clan clan = clanModule.getClan(playerModule.getPlayer(player.getUniqueId()).getClanId());
            if (clan != null) {
                // Yet again checking for the owner
                if (clan.getOwner().equals(player.getUniqueId())) {
                    if (args.length == 1) {
                        inviteModule.invite(clan, player, args[0]);
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
