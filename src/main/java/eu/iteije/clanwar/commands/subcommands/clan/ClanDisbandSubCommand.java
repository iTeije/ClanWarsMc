package eu.iteije.clanwar.commands.subcommands.clan;

import eu.iteije.clanwar.clans.ClanModule;
import eu.iteije.clanwar.clans.objects.Clan;
import eu.iteije.clanwar.framework.commands.objects.SubCommand;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.messages.storage.StorageKey;
import eu.iteije.clanwar.players.PlayerModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClanDisbandSubCommand extends SubCommand {

    private final ClanModule clanModule;
    private final PlayerModule playerModule;
    private final MessageModule messageModule;

    private final Map<UUID, Long> confirmCache;

    public ClanDisbandSubCommand(ClanModule clanModule, PlayerModule playerModule, MessageModule messageModule) {
        super("disband", "Disband your clan", "clan");
        this.clanModule = clanModule;
        this.playerModule = playerModule;
        this.messageModule = messageModule;

        this.confirmCache = new HashMap<>();
    }

    @Override
    public void onExecute(CommandSender sender, String[] args, String label) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            Clan clan = clanModule.getClan(playerModule.getPlayer(player.getUniqueId()).getClanId());
            if (clan != null) {
                // Make sure the player disbanding the clan is the owner of the clan
                if (clan.getOwner().equals(uuid)) {
                    // Check whether the player is in the confirmation list and if so, if it has been no more than 30 seconds from typing the command
                    if (args.length == 1 && args[0].equalsIgnoreCase("confirm") && confirmCache.containsKey(uuid)) {
                        if (System.currentTimeMillis() - (30 * 1000L) < confirmCache.get(uuid)) {
                            clanModule.disband(clan, playerModule);
                            messageModule.send(player, StorageKey.CLAN_DISBAND_SUCCESS);
                            return;
                        }
                    }

                    // Prompt the player to confirm
                    messageModule.send(player, StorageKey.CLAN_DISBAND_CONFIRM);
                    this.confirmCache.put(player.getUniqueId(), System.currentTimeMillis());
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
