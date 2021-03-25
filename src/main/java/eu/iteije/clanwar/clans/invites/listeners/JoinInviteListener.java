package eu.iteije.clanwar.clans.invites.listeners;

import eu.iteije.clanwar.clans.invites.ClanInviteModule;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@AllArgsConstructor
public class JoinInviteListener implements Listener {

    private final ClanInviteModule inviteModule;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        inviteModule.join(event.getPlayer());
    }
}
