package eu.iteije.clanwar.commands.subcommands.gamemanager;

import eu.iteije.clanwar.framework.commands.objects.SubCommand;
import eu.iteije.clanwar.messages.MessageModule;
import eu.iteije.clanwar.messages.storage.StorageKey;
import eu.iteije.clanwar.npcs.NpcModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GMPlaceNpcSubCommand extends SubCommand {

    private final NpcModule npcModule;
    private final MessageModule messageModule;

    public GMPlaceNpcSubCommand(MessageModule messageModule, NpcModule npcModule) {
        super("placenpc", "Place a join NPC at your location", "gamemanager");
        this.npcModule = npcModule;
        this.messageModule = messageModule;
    }

    @Override
    public void onExecute(CommandSender sender, String[] args, String label) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            npcModule.placeNpc(player.getLocation(), true);
            messageModule.send(player, StorageKey.GAMEMANAGER_PLACENPC_SUCCESS);
        } else {
            messageModule.send(sender, StorageKey.PROTOCOL_ERROR);
        }
    }
}
