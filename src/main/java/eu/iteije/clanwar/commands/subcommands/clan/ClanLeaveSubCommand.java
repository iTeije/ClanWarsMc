package eu.iteije.clanwar.commands.subcommands.clan;

import eu.iteije.clanwar.clans.ClanModule;
import eu.iteije.clanwar.clans.objects.Clan;
import eu.iteije.clanwar.framework.commands.objects.SubCommand;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.messages.storage.StorageKey;
import eu.iteije.clanwar.players.PlayerModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanLeaveSubCommand extends SubCommand {

    private final ClanModule clanModule;
    private final PlayerModule playerModule;
    private final MessageModule messageModule;

    public ClanLeaveSubCommand(ClanModule clanModule, PlayerModule playerModule, MessageModule messageModule) {
        super("leave", "Leave your clan", "clan");
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
                // Make sure the player is NOT the owner of the clan
                if (!clan.getOwner().equals(player.getUniqueId())) {
                    clanModule.remove(clan, player.getUniqueId(), player.getName());
                    messageModule.send(player, StorageKey.CLAN_LEAVE_SUCCESS);
                } else {
                    messageModule.send(player, StorageKey.CLAN_LEAVE_OWNERSHIP);
                }
            } else {
                messageModule.send(player, StorageKey.CLAN_UNAVAILABLE);
            }
        } else {
            messageModule.send(sender, StorageKey.PROTOCOL_ERROR);
        }
    }
}
