package eu.iteije.clanwar.npcs.listeners;

import eu.iteije.clanwar.games.GameModule;
import eu.iteije.clanwar.players.PlayerModule;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCClickListener implements Listener {

    private final GameModule gameModule;
    private final PlayerModule playerModule;

    public NPCClickListener(GameModule gameModule, PlayerModule playerModule) {
        this.gameModule = gameModule;
        this.playerModule = playerModule;
    }

    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        this.onLeftClick(new NPCLeftClickEvent(event.getNPC(), event.getClicker()));
    }

    @EventHandler
    public void onLeftClick(NPCLeftClickEvent event) {
        gameModule.openKitMenu(playerModule.getPlayer(event.getClicker().getUniqueId()), event.getClicker());
    }

}
