package eu.iteije.clanwar.commands.subcommands.clan;

import eu.iteije.clanwar.clans.ClanModule;
import eu.iteije.clanwar.clans.objects.Clan;
import eu.iteije.clanwar.framework.commands.objects.SubCommand;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.messages.Replacement;
import eu.iteije.clanwar.messages.storage.StorageKey;
import eu.iteije.clanwar.players.PlayerModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanInfoSubCommand extends SubCommand {

    private final ClanModule clanModule;
    private final MessageModule messageModule;
    private final PlayerModule playerModule;

    public ClanInfoSubCommand(ClanModule clanModule, MessageModule messageModule, PlayerModule playerModule) {
        super("info", "Get information about your clan", "clan");
        this.clanModule = clanModule;
        this.messageModule = messageModule;
        this.playerModule = playerModule;
    }

    @Override
    public void onExecute(CommandSender sender, String[] args, String label) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // If there are no arguments present, send info for the clan the player is in
            if (args.length == 0) {
                int clanId = playerModule.getPlayer(player.getUniqueId()).getClanId();
                Clan clan = clanModule.getClan(clanId);
                if (clan != null) {
                    sendClanInfo(clan, sender);
                } else {
                    messageModule.send(sender, StorageKey.CLAN_UNAVAILABLE);
                }
            } else if (args.length == 1) {
                Clan clan = clanModule.getClan(args[0]);
                if (clan != null) {
                    sendClanInfo(clan, sender);
                } else {
                    messageModule.send(sender, StorageKey.CLAN_NOT_FOUND);
                }
            } else {
                messageModule.send(sender, StorageKey.INVALID_ARGUMENTS);
            }
        } else {
            messageModule.send(sender, StorageKey.PROTOCOL_ERROR);
        }
    }

    private void sendClanInfo(Clan clan, CommandSender sender) {
        messageModule.sendHeaderFooter(true, sender, "CLAN INFO");
        messageModule.send(sender, StorageKey.CLAN_INFO_CLAN_NAME, new Replacement("%clan_name%", clan.getName()));
        messageModule.send(sender, StorageKey.CLAN_INFO_OWNER, new Replacement("%clan_owner_name%", clan.getInfo().getOwnerName()));
        messageModule.send(sender, StorageKey.CLAN_INFO_MEMBERS, new Replacement("%clan_member_names%", clan.getInfo().getMembersReadable()));
        messageModule.sendHeaderFooter(false, sender, "CLAN INFO");
    }
}
